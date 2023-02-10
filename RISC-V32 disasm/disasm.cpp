#include <iostream>
#include <fstream>
#include <string>
#include <filesystem>
#include <cassert>
#include "constants.h"


inline uint32_t getFileSize(const std::string& filename) {
	std::filesystem::path p(filename);
	return std::filesystem::file_size(p);
}

inline bool fileExists(const std::string& filename) {
	std::filesystem::path p;
	try{
		p = std::filesystem::path(filename);
	}
	catch (...){
		return 0;
	}
	return std::filesystem::exists(p);
}

std::vector<uint8_t> input;

uint32_t read(int offset, int size) {
	if (!(0 <= size || size <= 32)){
		std::cout << "Wrong size given: " + size << '\n';
		return 0;
	}
	uint32_t output = 0;
	for (int i = offset; i < offset + size; ++i){
		int ind = i / 8;
		int lPos = i % 8;
		output |= ((1 & input[ind] >> lPos) << i - offset);
	}
	return output;
}

struct section {
	uint32_t offset;
	uint32_t size;
};

struct symbol{
	uint32_t num;
	uint32_t value;
	uint32_t size;
	std::string type;
	std::string bind;
	std::string vis;
	std::string ndx;
	std::string name;
};

struct instruction{
	std::string name;
	uint32_t raw;
	std::string rd;
	std::string rs1;
	std::string rs2;
	int imm;
	std::string type;
};

int toSigned(int n, int size){
	if ((n & (1 << (size - 1)))){
		return(n | ~((1 << size) - 1));
	}
	else{
		return(n);
	}
}

int main(int argc, char* argv[]) {
	std::vector<std::string> args;
	for (int i = 0; i < argc; i++) {
		args.push_back(argv[i]);
	}
	if (args.size() < 3){
		std::cout << "Expected 2 arguments, but " + std::to_string(args.size() - 1) + " provided\n";
		return 0;
	}
	std::string inputName = args[1];
	std::string outputName = args[2];
	if (!fileExists(inputName)) {
		std::cout << "Error! File doesn't exist\n";
		return 0;
	}
	int inputSize = getFileSize(inputName);
	if (inputSize < 52 + 32) {
		std::cout << "File is too small to be ELF\n";
		return 0;
	}
	std::ifstream inputStream(inputName, std::ios_base::binary);
	input = std::vector<uint8_t>(inputSize);
	try{
		inputStream.read(reinterpret_cast<char*>(&input[0]), inputSize);
	}
	catch (...){
		std::cout << "Can't read this file!";
		inputStream.close();
		return 0;
	}
	inputStream.close();

	uint32_t fileSignature = read(0, 32);
	if (fileSignature != elf_signature) {
		std::cout << "It's not an ELF file!\n";
		return 0;
	}
	uint16_t fileArchitecture = read(e_machine_off * 8, 16);
	if (fileArchitecture != riscv_signature) {
		std::cout << "Architecture is not RISC-V, can't handle it...\n";
		return 0;
	}
	uint32_t sectionHeadersTableOffset = read(e_shoff_off * 8, 32);
	uint16_t sectionSize = read(e_shentsize_off * 8, 16);
	uint16_t sectionCount = read(e_shnum_off * 8, 16);
	uint16_t sectionOfNamesIndex = read(e_shstrndx_off * 8, 16);
	std::unordered_map<std::string, section> sections;
	auto parseSection = [&](uint16_t i, uint32_t& Offset, uint32_t& NameOffset, uint32_t& size) {
		int sechOff = sectionHeadersTableOffset + i * sectionSize;
		NameOffset = read(sechOff * 8, 32);
		Offset = read(sechOff * 8 + 16 * 8, 32);
		size = read(sechOff * 8 + 20 * 8, 32);
	};
	uint32_t namesTableOffset = -1, namesTableNameOffsetxD = -1, namesTableSize;
	parseSection(sectionOfNamesIndex, namesTableOffset, namesTableNameOffsetxD, namesTableSize);
	for (int i = 0; i < sectionCount; ++i) {
		uint32_t sectionOffset = 0, sectionNameOffset = 0, sectionSize = 0;
		parseSection(i, sectionOffset, sectionNameOffset, sectionSize);
		std::string name = "";
		int k = namesTableOffset + sectionNameOffset;
		while (read(k * 8, 8) != '\0') {
			name += (char)read(k * 8, 8);
			++k;
		}
		sections[name] = section{sectionOffset, sectionSize};
	}
	int symtabOff = sections[".symtab"].offset;
	int symSize = sections[".symtab"].size;
	int strtabOff = sections[".strtab"].offset;
	std::vector<symbol> symbols(symSize / 16);
	for (int symOff = symtabOff, num = 0; symOff < symtabOff + symSize; symOff += 16, ++num){
		uint32_t st_name = read(symOff * 8, 32);
		uint32_t st_value = read((symOff + 4) * 8, 32);
		uint32_t st_size = read((symOff + 8) * 8, 32);
		uint16_t st_shndx = read((symOff + 14) * 8, 16);
		std::string ndx = ndx_state.contains(st_shndx) ? ndx_state[st_shndx] : std::to_string(st_shndx);
		std::string type = st_type[read((symOff + 12) * 8, 4)];
		std::string bind = st_binding[read((symOff + 12) * 8 + 4, 4)];
		std::string visibility = st_visibility[read((symOff + 13) * 8, 8)];
		int k = strtabOff + st_name;
		std::string name;
		while (read(k * 8, 8) != '\0'){
			name += (char)read(k * 8, 8);
			++k;
		}
		symbols[num] = {(uint32_t)num, st_value, st_size, type, bind, visibility, ndx, name};
	}
	std::unordered_map<uint32_t, std::string> bigMarkers, smallMarkers;
	int markersCount = 0;
	for (const auto& v : symbols){
		bigMarkers[v.value] = "<" + v.name + ">";
	}
	int textOff = sections[".text"].offset;
	int textSize = sections[".text"].size;
	uint32_t entryPoint = read(e_entry_off * 8, 32);
	std::map<uint32_t, instruction> instructions;
	for (int off = textOff * 8, addr = entryPoint; off < (textOff + textSize) * 8; off += 32, addr += 4){
		uint8_t op = read(off, 7);
		uint32_t raw = read(off, 32);
		if (opCode[op] == "R"){
			uint8_t rd = read(off + 7, 5);
			uint8_t funct3 = read(off + 12, 3);
			uint8_t rs1 = read(off + 15, 5);
			uint8_t rs2 = read(off + 20, 5);
			uint8_t funct7 = read(off + 25, 7);
			std::string name = R_instruction[{funct3, funct7}];
			instructions[addr] = {name,raw,reg[rd], reg[rs1], reg[rs2], 0, opCode[op]};
		}
		else if (opCode[op][0] == 'I'){
			uint8_t rd = read(off + 7, 5);
			uint8_t funct3 = read(off + 12, 3);
			uint8_t funct7 = 0;
			uint8_t rs1 = read(off + 15, 5);
			int imm = read(off + 20, 12);
			if (opCode[op] == "I" && funct3 == 0x5){
				funct7 = read(off + 25, 7);
			}
			if (opCode[op] == "Ie"){
				funct7 = read(off + 20, 12);
			}
			std::string name;
			if (opCode[op] == "I"){
				name = I_instruction[{funct3, funct7}];
			}
			else if (opCode[op] == "Il"){
				name = Il_instruction[{funct3, 0}];
			}
			else if (opCode[op] == "Ij"){
				name = Ij_instruction[{0, 0}];
			}
			else if (opCode[op] == "Ie"){
				name = Ie_instruction[{0, funct7}];
			}
			imm = toSigned(imm, 12);
			instructions[addr] = {name, raw, reg[rd], reg[rs1],"", imm, opCode[op]};
		}
		else if (opCode[op] == "S"){
			int imm = 0;
			for (int i = 11; i >= 5; --i){
				imm |= (read(off + 20 + i, 1) << i);
			}
			for (int i = 4; i >= 0; --i){
				imm |= (read(off + 7 + i, 1) << i);
			}
			uint8_t funct3 = read(off + 12, 3);
			uint8_t rs1 = read(off + 15, 5);
			uint8_t rs2 = read(off + 20, 5);
			std::string name = S_instruction[{funct3, 0}];
			imm = toSigned(imm, 12);
			instructions[addr] = {name, raw, "", reg[rs1],reg[rs2], imm, opCode[op]};
		}
		else if (opCode[op] == "B"){
			uint8_t funct3 = read(off + 12, 3);
			uint8_t rs1 = read(off + 15, 5);
			uint8_t rs2 = read(off + 20, 5);
			int imm = 0;
			imm |= (read(off + 31, 1) << 12);
			for (int i = 10; i >= 5; i--){
				imm |= (read(off + 20 + i, 1) << i);
			}
			for (int i = 4; i >= 1; i--){
				imm |= (read(off + 7 + i, 1) << i);
			}
			imm |= (read(off + 7, 1) << 11);
			std::string name = B_instruction[{funct3, 0}];
			imm = toSigned(imm, 13);
			if (bigMarkers.contains(addr + imm)){
				smallMarkers[addr] = bigMarkers[addr + imm];
			}
			else{
				std::string marker = "<L" + std::to_string(markersCount) + ">";
				smallMarkers[addr] = marker;
				bigMarkers[addr + imm] = marker;
				++markersCount;
			}
			instructions[addr] = {name, raw, "", reg[rs1],reg[rs2], imm, opCode[op]};
		}
		else if (opCode[op][0] == 'U'){
			int imm = read(off + 12, 20);
			uint8_t rd = read(off + 7, 5);
			std::string name;
			if (opCode[op] == "Ul"){
				name = Ul_instruction[{0, 0}];
			}
			else{
				name = Ua_instruction[{0, 0}];
			}
			imm = toSigned(imm, 21);
			instructions[addr] = {name, raw, reg[rd], "", "", imm, opCode[op]};
		}
		else if (opCode[op] == "J"){
			int imm = 0;
			imm |= (read(off + 31, 1) << 20);
			for (int i = 10; i >= 1; i--){
				imm |= (read(off + 20 + i, 1) << i);
			}
			imm |= (read(off + 20, 1) << 11);
			for (int i = 19; i >= 12; i--){
				imm |= (read(off + i, 1) << i);
			}
			uint8_t rd = read(off + 7, 5);
			std::string name = J_instruction[{0, 0}];
			imm = toSigned(imm, 21);
			if (name == "jal"){
				if (bigMarkers.contains(addr + imm)){
					smallMarkers[addr] = bigMarkers[addr + imm];
				}
				else{
					std::string marker = "<L" + std::to_string(markersCount) + ">";
					smallMarkers[addr] = marker;
					bigMarkers[addr + imm] = marker;
					++markersCount;
				}
			}
			imm = toSigned(imm, 21);
			instructions[addr] = {name, raw, reg[rd], "", "", imm, opCode[op]};
		}
		else{
			instructions[addr] = {"",0,"","","",0, "unknown_instruction"};
		}
	}
	FILE* file = 0;
	int errCode = fopen_s(&file, outputName.c_str(), "w");
	if (errCode){
		std::cout << "Can't open file " + outputName + " for write, error code: " + std::to_string(errCode) << '\n';
	}
	for (auto& [addr, inst] : instructions){
		if (bigMarkers.contains(addr)){
			fprintf(file, "%08x   %s:\n", addr, bigMarkers[addr].c_str());
		}
		if (inst.type == "R"){
			fprintf(file, "   %05x:\t%08x\t%7s\t%s, %s, %s\n",
					addr, inst.raw, inst.name.c_str(), inst.rd.c_str(), inst.rs1.c_str(), inst.rs2.c_str());
		}
		else if (inst.type[0] == 'I'){
			if (inst.type == "Il" || inst.type == "Ij"){
				fprintf(file, "   %05x:\t%08x\t%7s\t%s, %d(%s)\n",
						addr, inst.raw, inst.name.c_str(), inst.rd.c_str(), inst.imm, inst.rs1.c_str());
			}
			else if (inst.type == "Ie"){
				fprintf(file, "   %05x:\t%08x\t%7s\n", addr, inst.raw, inst.name.c_str());
			}
			else{
				fprintf(file, "   %05x:\t%08x\t%7s\t%s, %s %d\n",
						addr, inst.raw, inst.name.c_str(), inst.rd.c_str(), inst.rs1.c_str(), inst.imm);
			}
		}
		else if (inst.type == "S"){
			fprintf(file, "   %05x:\t%08x\t%7s\t%s, %d(%s)\n",
					addr, inst.raw, inst.name.c_str(), inst.rs2.c_str(), inst.imm, inst.rs1.c_str());
		}
		else if (inst.type == "B"){
			fprintf(file, "   %05x:\t%08x\t%7s\t%s, %s 0x%x %s\n",
					addr, inst.raw, inst.name.c_str(), inst.rs1.c_str(), inst.rs2.c_str(), addr + inst.imm, smallMarkers[addr].c_str());
		}
		else if (inst.type == "J"){
			fprintf(file, "   %05x:\t%08x\t%7s\t%s, 0x%x %s\n",
					addr, inst.raw, inst.name.c_str(), inst.rd.c_str(), addr + inst.imm, smallMarkers[addr].c_str());
		}
		else if (inst.type[0] == 'U'){
			fprintf(file, "   %05x:\t%08x\t%7s\t%s, %d\n",
					addr, inst.raw, inst.name.c_str(), inst.rd.c_str(), inst.imm);
		}
		else if (inst.type == "unknown_instruction"){
			fprintf(file, "   %05x:\t%08x\t\t%7s\n",
					addr, inst.raw, inst.type.c_str());
		}
	}
	fprintf(file, "\n.symtab\nSymbol Value          	Size Type 	Bind 	Vis   	Index Name\n");
	for (const auto& v : symbols){
		fprintf(file, "[%4i] 0x%-15X %5i %-8s %-8s %-8s %6s %s\n", v.num, v.value, v.size, v.type.c_str(), v.bind.c_str(), v.vis.c_str(), v.ndx.c_str(), v.name.c_str());
	}
	fclose(file);
}

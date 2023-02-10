#include <algorithm>
#include <unordered_map>
#include <map>
constexpr int elf_signature = 0x464c457f;
constexpr int riscv_signature = 0xf3;

constexpr int e_machine_off = 18;
constexpr int e_shoff_off = 32;
constexpr int e_shentsize_off = 46;
constexpr int e_shnum_off = 48;
constexpr int e_shstrndx_off = 50;
constexpr int e_entry_off = 24;

inline std::unordered_map<uint8_t, std::string> opCode = {
	{0b0110011, "R"},
	{0b0010011, "I"},
	{0b0000011, "Il"},
	{0b0100011, "S"},
	{0b1100011, "B"},
	{0b1101111, "J"},
	{0b1100111, "Ij"},
	{0b0110111, "Ul"},
	{0b0010111, "Ua"},
	{0b1110011, "Ie"}
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> R_instruction = {
	{{0,0}, "add"},
	{{0, 0x20}, "sub"},
	{{0x4, 0}, "xor"},
	{{0x6, 0}, "or"},
	{{0x7, 0}, "and"},
	{{0x1, 0}, "sli"},
	{{0x5, 0}, "srl"},
	{{0x5, 0x20}, "sra"},
	{{0x2, 0}, "slt"},
	{{0x3, 0}, "sltu"},
	{{0x0, 0x1}, "mul"},
	{{0x1, 0x1}, "mulh"},
	{{0x2, 0x1}, "mulsu"},
	{{0x3, 0x1}, "mulu"},
	{{0x4, 0x1}, "div"},
	{{0x5, 0x1}, "divu"},
	{{0x6, 0x1}, "rem"},
	{{0x7, 0x1}, "remu"}
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> I_instruction = {
	{{0,0}, "addi"},
	{{0x4, 0}, "xori"},
	{{0x6, 0}, "ori"},
	{{0x7, 0}, "0"},
	{{0x1, 0}, "slli"},
	{{0x5, 0}, "srli"},
	{{0x5, 0x20}, "srai"},
	{{0x2, 0}, "slti"},
	{{0x3, 0}, "sltiu"}
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> Il_instruction = {
	{{0,0}, "lb"},
	{{0x1,0}, "lh"},
	{{0x2,0}, "lw"},
	{{0x4,0}, "lbu"},
	{{0x5,0}, "lhu"}
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> Ij_instruction = {
	{{0,0}, "jalr"},
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> Ie_instruction = {
	{{0,0}, "ecall"},
	{{0, 0x1}, "ebreak"}
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> S_instruction = {
	{{0,0}, "sb"},
	{{0x1,0}, "sh"},
	{{0x2,0}, "sw"},
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> B_instruction = {
	{{0,0}, "beq"},
	{{0x1, 0}, "bne"},
	{{0x4, 0}, "blt"},
	{{0x5, 0}, "bge"},
	{{0x6, 0}, "bltu"},
	{{0x7, 0}, "bgeu"}
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> J_instruction = {
	{{0,0}, "jal"},
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> Ul_instruction = {
	{{0,0}, "lui"},
};
inline std::map<std::pair<uint16_t, uint16_t>, std::string> Ua_instruction = {
	{{0,0}, "auipc"},
};
inline std::unordered_map<uint8_t, std::string> reg = {
	{0, "zero"},
	{1, "ra"},
	{2, "sp"},
	{3, "gp"},
	{4, "tp"},
	{5, "t0"},
	{6, "t1"},
	{7, "t2"},
	{8, "s0 / fp"},
	{9, "s1"},
	{10, "a0"},
	{11, "a1"},
	{12, "a2"},
	{13, "a3"},
	{14, "a4"},
	{15, "a5"},
	{16, "a6"},
	{17, "a7"},
	{18, "s2"},
	{19, "s3"},
	{20, "s4"},
	{21, "s5"},
	{22, "s6"},
	{23, "s7"},
	{24, "s8"},
	{25, "s9"},
	{26, "s10"},
	{27, "s11"},
	{28, "t3"},
	{29, "t4"},
	{30, "t5"},
	{31, "t6"}
};

inline std::unordered_map<uint8_t, std::string> st_binding = {
	{0, "LOCAL"},
	{1, "GLOBAL"},
	{2, "WEAK"},
	{10, "LOOS"},
	{12, "HIOS"},
	{13, "LOPROC"},
	{15, "HIPROC"}
};
inline std::unordered_map<uint8_t, std::string> st_type = {
	{0, "NOTYPE"},
	{1, "OBJECT"},
	{2, "FUNC"},
	{3, "SECTION"},
	{4, "FILE"},
	{5, "COMMON"},
	{6, "TLS"},
	{10, "LOOS"},
	{12, "HIOS"},
	{13, "LOPROC"},
	{15, "HIPROC"}
};
inline std::unordered_map<uint8_t, std::string> st_visibility = {
	{0, "DEFAULT"},
	{1, "INTERNAL"},
	{2, "HIDDEN"},
	{3, "PROTECTED"}
};
inline std::unordered_map<uint16_t, std::string> ndx_state = {
	{0xfff1, "ABS"},
	{0, "UNDEF"},
	{0xffff, "XINDEX"}
};
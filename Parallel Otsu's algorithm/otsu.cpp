#include <iostream>
#include <omp.h>
#include <vector>
#include <filesystem>
#include <fstream>
#include <string>

constexpr int clustersCount = 4;

inline uintmax_t getFileSize(const std::string& filename) {
	try {
		std::filesystem::path p(filename);
		return std::filesystem::file_size(p);
	}
	catch (std::exception ex) {
		std::cerr << "Unable to get \" " << filename << "\" size\n" << ex.what();
		return 0;
	}
}

inline bool fileExists(const std::string& filename) {
	std::filesystem::path p;
	try {
		p = std::filesystem::path(filename);
	}
	catch (...) {
		return false;
	}
	return std::filesystem::exists(p);
}

inline bool parse(const std::string& fileName, uint8_t*& data, int& width, int& height) {
	std::string fileFormat(3, '\0');
	std::string maxBrightness(4, '\0');
	std::ifstream ifstream;
	try {
		ifstream = std::ifstream(fileName, std::ios_base::binary);
	}
	catch (std::exception& ex) {
		std::cerr << ex.what() << "\nUnable to open input stream from file " << fileName << '\n';
		return false;
	}
	try {
		ifstream >> fileFormat >> width >> height >> maxBrightness;
		ifstream.get();
		if (fileFormat != "P5") {
			std::cerr << "Wrong file format, exptected P5, but got \"" << fileFormat << "\"\n";
		}
		else if (maxBrightness != "255") {
			std::cerr << "Wrong max brightness, exptected 255, but got " << maxBrightness << '\n';
		}
		else if (width <= 0 || height <= 0 || ((long long)width * height > INT32_MAX)) {
			std::cerr << "Invalid dimensions: width: " << width << " height: " << height << '\n';
		}
		else {
			data = reinterpret_cast<uint8_t*>(malloc(width * height));
			ifstream.read(reinterpret_cast<char*>(data), width * height);
			ifstream.close();
			return true;
		}
		ifstream.close();
	}
	catch (std::exception& ex) {
		std::cerr << "Error while reading from " << fileName << '\n' << ex.what() << '\n';
		ifstream.close();
	}
	return false;
}

int main(int argc, char* argv[]) {
	std::vector <std::string> args(argc);
	for (int i = 0; i < argc; i++) {
		args[i] = argv[i];
	}
	if (argc < 4) {
		std::cerr << "Error! Expected 3 arguments but " << std::to_string(args.size() - 1) << " provided";
		return 0;
	}
	std::string inputFileName = args[2];
	std::string outputFileName = args[3];
	int numThreads = omp_get_max_threads();
	bool useOmp = true;
	try {
		int arg1 = std::stoi(args[1]);
		if (arg1 == -1) {
			useOmp = false;
			numThreads = 1;
		}
		else if (arg1 > 0) {
			if (arg1 > omp_get_max_threads()) {
				std::cerr << "Warning! I can only run " << std::to_string(omp_get_max_threads())
					<< " threads, but asked to run " << std::to_string(arg1) << " threads\n";
			}
			numThreads = arg1;
		}
		else if (arg1 < -1) {
			std::cerr << "Incorrect first argument, expected an integer in range[-1, "
				<< std::to_string(omp_get_max_threads()) << "] but got " << args[1] << '\n';
			return 0;
		}
	}
	catch (std::exception& ex) {
		std::cerr << ex.what() << "\nIncorrect first argument, expected an integer in range [-1, "
			<< std::to_string(omp_get_max_threads()) << "] but got " << args[1] << '\n';
		return 0;
	}
	if (!fileExists(inputFileName)) {
		std::cerr << "File \"" << inputFileName << "\" doesn't exists!";
		return 0;
	}
	uintmax_t inputSize = getFileSize(inputFileName);
	uint8_t* data = nullptr;
	int width = 0;
	int height = 0;
	if (!parse(inputFileName, data, width, height)) {
		std::cerr << "Can't parse data, quitting...\n";
		return 0;
	}
	int desiredSize = width * height + std::to_string(width).size() + std::to_string(height).size() + 9;
	if (inputSize != desiredSize) {
		std::cerr << "Error! Expected file size is " << desiredSize << " but actual is " << inputSize << '\n';
		return 0;
	}
	double time0 = omp_get_wtime();
	int pixelsNum = width * height;
	int histo[256]{};
	int histoNumPrefixes[257]{};
	int histoBrightPrefixes[257]{};
	double maxDisp = 0;
	int F0 = -1, F1 = -1, F2 = -1;
	int minBrightness = 255, maxBrightness = 0;
#pragma omp parallel num_threads(numThreads) if (useOmp)
	{
		int lHisto[256]{};
#pragma omp for schedule(static, 1)
		for (int i = 0; i < pixelsNum; ++i) {
			++lHisto[data[i]];
		}
#pragma omp critical
		{
			for (int i = 0; i < 256; ++i) {
				histo[i] += lHisto[i];
			}
		}
#pragma omp barrier
#pragma omp single
		{
			for (int i = 1; i < 257; ++i) {
				histoNumPrefixes[i] = histoNumPrefixes[i - 1] + histo[i - 1];
				histoBrightPrefixes[i] = histoBrightPrefixes[i - 1] + histo[i - 1] * (i - 1);
				if (histo[i - 1] && minBrightness > i - 1){
					minBrightness = i - 1;
				}
				if (histo[i - 1] && maxBrightness < i - 1){
					maxBrightness = i - 1;
				}
			}
		}
		double lMaxDisp = 0;
		int lF0 = -1, lF1 = -1, lF2 = -1;
#pragma omp for schedule(dynamic)
		for (int f0 = 0; f0 <= maxBrightness; ++f0) {
			if (histoNumPrefixes[f0 + 1] == histoNumPrefixes[f0])
				continue;
			for (int f1 = f0 + 1; f1 <= maxBrightness; ++f1) {
				if (histoNumPrefixes[f1 + 1] == histoNumPrefixes[f1])
					continue;
				for (int f2 = f1 + 1; f2 <= maxBrightness; ++f2) {
					if (histoNumPrefixes[f2 + 1] == histoNumPrefixes[f2])
						continue;
					double disp = 0;
					for (int i = 0; i < clustersCount; ++i) {
						int l, r;
						if (i == 0) {
							l = minBrightness, r = f0;
						}
						else if (i == 1) {
							l = f0 + 1, r = f1;
						}
						else if (i == 2) {
							l = f1 + 1, r = f2;
						}
						else {
							l = f2 + 1, r = maxBrightness;
						}
						int cPixelsNum = histoNumPrefixes[r + 1] - histoNumPrefixes[l];
						int cBrightSum = histoBrightPrefixes[r + 1] - histoBrightPrefixes[l];
						double cProb = (double)cPixelsNum / pixelsNum;
						double cAvgBrightness = (double)cBrightSum / cPixelsNum;
						disp += cProb * cAvgBrightness * cAvgBrightness;
					}
					if (disp >= lMaxDisp) {
						lMaxDisp = disp;
						lF0 = f0, lF1 = f1, lF2 = f2;
					}
				}
			}
		}
#pragma omp critical
		{
			if (lMaxDisp >= maxDisp) {
				maxDisp = lMaxDisp;
				F0 = lF0, F1 = lF1, F2 = lF2;
			}
		}
#pragma omp barrier
#pragma omp for schedule(static)
		for (int i = 0; i < width * height; ++i) {
			if (data[i] <= F0) {
				data[i] = 0;
			}
			else if (data[i] <= F1) {
				data[i] = 84;
			}
			else if (data[i] <= F2) {
				data[i] = 170;
			}
			else {
				data[i] = 255;
			}
		}
	}
	double time1 = omp_get_wtime();
	printf("Time (%i thread(s)): %g ms\n%u %u %u\n", numThreads, (time1 - time0) * 1000, F0, F1, F2);
	std::ofstream outputStream;
	try {
		outputStream = std::ofstream(outputFileName, std::ios_base::binary);
	}
	catch (std::exception& ex) {
		std::cerr << "Unable to create output filestream to " << outputFileName << '\n' << ex.what() << '\n';
		return 0;
	}
	try {
		outputStream << "P5\n" << width << ' ' << height << '\n' << "255\n";
		outputStream.write(reinterpret_cast<char*>(data), height * width);
	}
	catch (std::exception& ex) {
		std::cerr << "Error while writing to the " << outputFileName << '\n' << ex.what() << '\n';
		outputStream.close();
		return 0;
	}
	outputStream.close();
}

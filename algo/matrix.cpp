#include <vector>
#include <iostream>
using namespace std;
vector<vector<int>> mul(const vector<vector<int>>& a, const vector<vector<int>>& b) {
	int a_rows = a.size();
	int a_cols = a[0].size();
	int b_cols = b[0].size();
	vector<vector<int>> result(a_rows, vector<int>(b_cols));
	for (int i = 0; i < a_rows; i++) {
		for (int j = 0; j < b_cols; j++) {
			for (int k = 0; k < a_cols; k++) {
				result[i][j] += a[i][k] * b[k][j];
			}
		}
	}
	return result;
}

vector<vector<int>> binpow(const vector<vector<int>>& a, int n){
	vector<vector<int>> res(a.size(), vector<int>(a.size()));
	vector<vector<int>> current = a;
	for (int i = 0; i < a.size(); ++i){
		res[i][i] = 1;
	}
	while (n > 0){
		if (n & 1){
			res = mul(current, res);
		}
		current = mul(current, current);
		n >>= 1;
	}
	return res;
}

int main() {
	vector<vector<int>> a{{1,1},{1,0}};
	vector<vector<int>> result = binpow(a, 15);
	for (const auto& row : result) {
		for (auto val : row) {
			cout << val << ' ';
		}
		cout << '\n';
	}
}
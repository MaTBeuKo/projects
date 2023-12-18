#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <random>
#include <chrono>
#include <string>
#include <set>
#include <numeric>
#include <map>
#include <fstream>
#include <stack>
#include <unordered_set>
#include <assert.h>
#include <bitset>
#include <iomanip>
#include <unordered_map>

using namespace std;
#define all(x) (x).begin(), (x).end()
#define cyes cout << "YES\n"
#define cno cout << "NO\n"
#define mat(type, n, m, name) vector<vector<type>> name(n, vector<type>(m))
#define pb push_back
#define gen(x) generate(all(x), nxt)
#define MIN(a, other) a = min(a, other)
#define MAX(a, other) a = max(a, other)
#define int int64_t
int nxt(){
	int x;
	cin >> x;
	return x;
}
map<char, vector<string>> rule;
int d[28][101][101];
int mod = 1e9 + 7;
int32_t main() {
	int p;
	char start;
	string t;
	ifstream ifs("nfc.in");
	ofstream ofs("nfc.out");
	set<char> usedNons;
	ifs >> p >> start >> ws;
	for (int i = 0; i < p; ++i){
		getline(ifs, t);
		vector<string> part;
		int j = 0;
		part.pb("");
		while (j < t.size()){
			if (t[j] == ' '){
				if (part.back() != ""){
					part.pb("");
				}
			}
			else{
				part.back() += t[j];
			}
			++j;
		}
		if (part.size() == 2 || part[2].empty()){
			rule[part[0][0]].pb("-");
		}
		else{
			rule[part[0][0]].pb(part[2]);
		}
	}
	string word;
	getline(ifs, word);
	int m = word.size();
	for (int i = 0; i < m; ++i){
		for (auto& [l, r] : rule){
			for (auto& s : r){
				char c = s[0];
				if (c == word[i]){
					d[l - 'A'][i][i] = 1;
				}
			}
		}
	}

	for (int n = 1; n < m; ++n){
		for (int j = 0; j < m-1; ++j){
			for (auto& [l, r] : rule){
				for (auto& s : r){
					if (s.size() == 1){
						continue;
					}
					int A = l - 'A', B = s[0] - 'A', C = s[1] - 'A';
					for (int k = j; k <= j+n; ++k){
						d[A][j][j+n] += d[B][j][k] * d[C][k + 1][j+n];
						d[A][j][j+n] %= mod;
					}
				}
			}
		}
	}
	ofs << d[start - 'A'][0][m - 1] << '\n';
}
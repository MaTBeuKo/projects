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
#include <unordered_map>
#include <assert.h>
#include <bitset>
#include <iomanip>

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
bool go(char currRule, string& word, int pos){
	if (pos >= word.size()){
		return false;
	}
	bool res = false;
	for (string& s : rule[currRule]){
		if (s[0] == word[pos]){
			if (s.size() == 1){
				if (pos == word.size() - 1){
					return true;
				}
			}
			else{
				res |= go(s[1], word, pos + 1);
			}
		}
	}
	return res;
}

int32_t main() {
	int p;
	char start;
	string t;
	ifstream ifs("automaton.in");
	ofstream ofs("automaton.out");
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
		if (part.size() == 2){
			rule[part[0][0]].pb("-");
		}
		else{
			rule[part[0][0]].pb(part[2]);
		}
	}
	int m;
	getline(ifs, t);
	m = stoi(t);
	for (int i = 0; i < m; ++i){
		string word;
		getline(ifs, word);
		if (go(start, word, 0)){
			ofs << "yes\n";
		}
		else{
			ofs << "no\n";
		}
	}
}
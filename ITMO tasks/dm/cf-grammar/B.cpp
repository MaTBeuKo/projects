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

unordered_map<char, vector<string>> rule;

int32_t main() {
	int p;
	char start;
	string t;
	ifstream ifs("epsilon.in");
	ofstream ofs("epsilon.out");
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
	set<char> makeEps;
	while (true){
		int sz = makeEps.size();
		for (auto& [l, r] : rule){
			for (auto& s : r){
				if (s == "-"){
					makeEps.insert(l);
				}
				else{
					bool good = true;
					for (auto c : s){
						good &= 'A' <= c && c <= 'Z' && makeEps.count(c);
					}
					if (good){
						makeEps.insert(l);
					}
				}
			}
		}
		if (makeEps.size() == sz){
			break;
		}
		sz = makeEps.size();
	}
	for (auto v : makeEps){
		ofs << v << ' ';
	}
}
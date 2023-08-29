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
map<char, bool> been;
set<char> canEnd;
void go(char currRule){
	if (been[currRule]){
		return;
	}
	been[currRule] = 1;
	for (string& s : rule[currRule]){
		for (char c : s){
			if ('A' <= c && c <= 'Z'){
				go(c);
			}
		}
	}
}

int32_t main() {
	int p;
	char start;
	string t;
	ifstream ifs("useless.in");
	ofstream ofs("useless.out");
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
		usedNons.insert(part[0][0]);
		if (part.size() == 2 || part[2].empty()){
			rule[part[0][0]].pb("a");
		}
		else{
			rule[part[0][0]].pb(part[2]);
			for (auto v : part[2]){
				if ('A' <= v && v <= 'Z'){
					usedNons.insert(v);
				}
			}
		}
	}
	if ('A' <= start && start <= 'Z'){
		usedNons.insert(start);
	}
	while (true){
		int sz = canEnd.size();
		for (auto& [l, r] : rule){
			bool hasTerm = false;
			for (auto& s : r){
				bool term = true;
				for (auto c : s){
					term &= (('a' <= c && c <= 'z') || canEnd.count(c));
				}
				hasTerm |= term;
			}
			if (hasTerm){
				canEnd.insert(l);
			}
		}
		if (canEnd.size() == sz){
			break;
		}
	}
	for (auto it = rule.begin(); it != rule.end();){
		auto& l = (*it).first;
		auto& r = (*it).second;
		if (!canEnd.count(l)){
			it = rule.erase(it);
			if (it == rule.end()){
				break;
			}
		}
		else{
			vector<string> t;
			for (int i = 0; i < r.size(); ++i){
				bool bad = false;
				for (auto v : r[i]){
					if ('A' <= v && v <= 'Z' && !canEnd.count(v)){
						bad = true;
					}
				}
				if (!bad){
					t.pb(r[i]);
				}
			}
			r = t;
			++it;
		}
	}
	if (rule[start].size()){
		go(start);
	}
	for (char c : usedNons){
		if (!been[c]){
			ofs << c << ' ';
		}
	}
}
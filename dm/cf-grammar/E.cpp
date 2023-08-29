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
map<int, vector<vector<int>>> rule;
vector<pair<int, vector<int>>> ruleP;
int d[2000][101][101];
map<int, bool> been;
set<int> canEnd;
bool nterm(int c){
	return c >= 1000;
}

void go(int currRule){
	if (been[currRule]){
		return;
	}
	been[currRule] = 1;
	for (vector<int>& s : rule[currRule]){
		for (int c : s){
			if (nterm(c)){
				go(c);
			}
		}
	}
}

int32_t main() {
	int p;
	char tStart;
	string t;
	ifstream ifs("cf.in");
	ofstream ofs("cf.out");
	ifs >> p >> tStart >> ws;
	int start = (int)tStart - 'A' + 1000;
	int last = 0;
	//ввод
	for (int i = 0; i < p; ++i){
		getline(ifs, t);
		vector<vector<int>> part;
		int j = 0;
		part.pb({});
		while (j < t.size()){
			if (t[j] == ' '){
				if (!part.back().empty()){
					part.pb({});
				}
			}
			else{
				int v = t[j];
				if ('A' <= t[j] && t[j] <= 'Z'){
					v = (int)t[j] - 'A' + 1000;
					MAX(last, v);
				}
				part.back().pb(v);
			}
			++j;
		}
		if (part.size() < 3 || part[2].empty()){
			ruleP.pb({part[0][0], {0}});
		}
		else{
			ruleP.pb({part[0][0], part[2]});
		}
	}
	string w;
	getline(ifs, w);
	++last;
	int n = w.size();
	//для каждого терминала создаем персональный нетерминал
	for (int i = 0; i < ruleP.size(); ++i){
		int& A = ruleP[i].first;
		vector<int>& alpha = ruleP[i].second;
		if (ruleP[i].second.size() == 1 && !nterm(ruleP[i].second[0])){
			continue;
		}
		for (int j = 0; j < ruleP[i].second.size(); ++j){
			if (!nterm(ruleP[i].second[j])){
				ruleP.pb({last++, {ruleP[i].second[j]}});
				ruleP[i].second[j] = last - 1;
			}
		}
	}
	//разворачиваем длинные правила
	for (auto it = ruleP.begin(); it != ruleP.end(); ){
		int sz = (*it).second.size();
		if (sz > 2){
			int A = (*it).first;
			vector<int> alpha = (*it).second;
			ruleP.erase(it);
			ruleP.pb({A, {alpha[0], last++}});
			for (int i = 1; i < sz - 2; ++i){
				ruleP.pb({last - 1,{alpha[i], last++}});
			}
			ruleP.pb({last - 1, {alpha[sz - 2], alpha[sz - 1]}});
			it = ruleP.begin();
		}
		else{
			++it;
		}
	}

	//нахожу нетерминалы из которых можно получить епсилон
	set<int> makeEps;
	while (true){
		int sz = makeEps.size();
		for (auto& [A, alpha] : ruleP){
			if (alpha.size() == 1 && alpha[0] == 0){
				makeEps.insert(A);
			}
			else{
				bool good = true;
				for (auto c : alpha){
					good &= nterm(c) && makeEps.count(c);
				}
				if (good){
					makeEps.insert(A);
				}
			}
		}
		if (makeEps.size() == sz){
			break;
		}
		sz = makeEps.size();
	}
	//фикшу
	for (auto it = ruleP.begin(); it != ruleP.end();){
		vector<int> alpha = (*it).second;
		if (alpha.size() == 1 && alpha[0] == 0){
			ruleP.erase(it);
			it = ruleP.begin();
		}
		else{
			++it;
		}
	}
	int currSize = ruleP.size();
	for (int i = 0; i < currSize; ++i){
		int &A = ruleP[i].first;
		vector<int> &alpha = ruleP[i].second;
		vector<int> epsIds;
		for (int i = 0; i < alpha.size(); ++i){
			if (makeEps.count(alpha[i])){
				epsIds.pb(i);
			}
		}
		for (int i = 0; i < (1ll << epsIds.size()) - 1; ++i){
			vector<int> s;
			for (int j = 0, id = 0; j < alpha.size(); ++j){
				if (id < epsIds.size() && j == epsIds[id]){
					if (i >> id & 1){
						s.pb(alpha[j]);
					}
					++id;
				}
				else{
					s.pb(alpha[j]);
				}
			}
			if (!s.empty()){
				ruleP.pb({A, s});
			}
		}
	}

	//цепные правила
	set<pair<int, int>> units;
	for (auto& [A, alpha] : ruleP){
		units.insert({A,A});
	}
	while (true){
		int sz = units.size();
		for (auto& [l, r] : units){
			for (auto& [A, alpha] : ruleP){
				if (r == A && alpha.size() == 1 && nterm(alpha[0])){
					units.insert({l, alpha[0]});
				}
			}
		}
		if (sz == units.size()){
			break;
		}
	}
	vector<pair<int, vector<int>>> newRules;
	for (auto& [A, r] : units){
		for (auto& [B, alpha] : ruleP){
			if (r == B){
				if (alpha.size() != 1 || !units.count({B, alpha[0]})){
					newRules.pb({A, alpha});
				}
			}
		}
	}
	for (auto it = ruleP.begin(); it != ruleP.end();){
		int A = (*it).first;
		vector<int> alpha = (*it).second;
		if (alpha.size() == 1 && units.count({A, alpha[0]})){
			ruleP.erase(it);
			it = ruleP.begin();
		}
		else{
			++it;
		}
	}
	for (auto& nRule : newRules){
		ruleP.pb(nRule);
	}
	sort(all(ruleP));
	ruleP.resize(unique(all(ruleP)) - ruleP.begin());

	//нахожу бесполезные нетерминалы
	/*for (int i = 0; i < ruleP.size(); ++i){
		rule[ruleP[i].first].pb(ruleP[i].second);
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
			vector<vector<int>> t;
			for (int i = 0; i < r.size(); ++i){
				bool bad = false;
				for (auto v : r[i]){
					if (nterm(v) && !canEnd.count(v)){
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
	}*/
	//удаляю бесполезные нетерминалы

	//ruleP.clear();
	//for (auto& [A, ve] : rule){
	//	if (been[A]){
	//		for (auto& s : ve){
	//			ruleP.pb({A, s});
	//		}
	//	}
	//}
	sort(all(ruleP));
	ruleP.resize(unique(all(ruleP)) - ruleP.begin());
	//разворачиваю правила с двумя терминалами
	for (auto i = 0; i < ruleP.size(); ++i){
		int& A = ruleP[i].first;
		vector<int>& alpha = ruleP[i].second;
		if (alpha.size() == 2 && !nterm(alpha[0]) && !nterm(alpha[1])){
			ruleP.pb({last++,{alpha[0]}});
			ruleP.pb({last++, {alpha[1]}});
			alpha[0] = last - 2;
			alpha[1] = last - 1;
		}
	}
	rule.clear();
	for (auto [A, alpha] : ruleP){
		rule[A].pb(alpha);
	}

	int m = w.size();
	for (int i = 0; i < m; ++i){
		for (auto& [l, r] : rule){
			for (auto& s : r){
				int c = s[0];
				if (c == w[i]){
					d[l][i][i] = 1;
				}
			}
		}
	}
	for (int n = 1; n < m; ++n){
		for (int j = 0; j < m - 1; ++j){
			for (auto& [l, r] : rule){
				for (auto& s : r){
					if (s.size() == 1){
						continue;
					}
					int A = l, B = s[0], C = s[1];
					for (int k = j; k <= j + n; ++k){
						d[A][j][j + n] |= (d[B][j][k] & d[C][k + 1][j + n]);
					}
				}
			}
		}
	}
	ofs << (d[start][0][m - 1] ? "yes" : "no") << '\n';
}
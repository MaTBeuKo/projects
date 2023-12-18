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
#include <unordered_set>
#include <unordered_map>
#ifdef FAST_ALLOCATOR_MEMORY
int allocator_pos = 0;
char allocator_memory[(int)FAST_ALLOCATOR_MEMORY];

inline void* operator new(size_t n) {
	// fprintf(stderr, "n=%ld\n", n);
	char* res = allocator_memory + allocator_pos;
	assert(n <= (size_t)((int)FAST_ALLOCATOR_MEMORY - allocator_pos));
	allocator_pos += n;
	return (void*)res;
}

inline void operator delete(void*) noexcept {}

inline void operator delete(void*, size_t) noexcept {}
//inline void * operator new [] (size_t) { assert(0); }
//inline void operator delete [] (void *) { assert(0); }
#endif


using namespace std;
#define all(x) (x).begin(), (x).end()
#define cyes cout << "YES\n"
#define cno cout << "NO\n"
#define mat(type, n, m, name) vector<vector<type>> name(n, vector<type>(m))
#define pb push_back
#define gen(x) generate(all(x), nxt)
#define MIN(a, other) a = min(a, other)
#define MAX(a, other) a = max(a, other)
const int MAXN = 50'500;
vector<unordered_map<char, vector<int>>> in(MAXN);
vector<unordered_map<char, int>> out(MAXN);
vector<bool> term(MAXN, 0);
vector<bool> rFromEnd(MAXN, 0);
vector<bool> rFromStart(MAXN, 0);

void reach(int id){
	if (rFromStart[id]){
		return;
	}
	rFromStart[id] = 1;
	for (auto& [c, v] : out[id]){
		reach(v);
	}
}

void reachFromEnd(int id){
	if (rFromEnd[id]){
		return;
	}
	rFromEnd[id] = 1;
	for (auto& [c, ve] : in[id]){
		for (auto v : ve){
			reachFromEnd(v);
		}
	}
}

int32_t main() {
	//cin.tie(nullptr)->ios_base::sync_with_stdio(false);
	//setvbuf(stdout, nullptr, _IOFBF, BUFSIZ);
	ifstream ifs = ifstream("minimization.in");
	ofstream ofs = ofstream("minimization.out");
	int n, m, k;
	ifs >> n >> m >> k;
	for (int i = 0; i < k; ++i){
		int id;
		ifs >> id;
		--id;
		term[id] = true;
	}
	for (int i = 0; i < m; ++i){
		int x, y;
		ifs >> x >> y;
		--x, --y;
		char c;
		ifs >> c;
		in[y][c].pb(x);
		out[x][c] = y;
	}
	reach(0);
	for (int i = 0; i < n; ++i){
		if (term[i]){
			reachFromEnd(i);
		}
	}
	for (int i = 0; i < n; ++i){
		for (auto& [ch, ve] : in[i]){
			if (rFromStart[i] && rFromEnd[i]){
				vector<int> nv;
				for (int j = 0; j < ve.size(); ++j){
					if (rFromStart[ve[j]] && rFromEnd[ve[j]]){
						nv.pb(ve[j]);
					}
				}
				ve = nv;
			}
		}
	}
	vector<int> Class(MAXN);
	vector<unordered_set<int>> P(2);
	for (int i = 0; i < n; ++i){
		if (!rFromEnd[i] || !rFromStart[i]){
			continue;
		}
		P[term[i]].insert(i);
		Class[i] = term[i];
	}

	queue<pair<int, char>> q;
	for (int i = 0; i < 2; ++i){
		for (char c = 'a'; c <= 'z'; ++c){
			q.push({i, c});
		}
	}
	while (!q.empty()){
		pair<int, char> p = q.front();
		int classId = p.first;
		char a = p.second;
		q.pop();
		map<int, vector<int>> involved;
		for (int node : P[classId]){
			for (int r : in[node][a]){
				int i = Class[r];
				if (!involved.count(i)){
					involved[i] = {};
				}
				involved[i].pb(r);
			}
		}
		for (auto& [i, value] : involved){
			if (value.size() < P[i].size()){
				P.pb({});
				int j = P.size() - 1;
				for (int r : value){
					P[i].erase(r);
					P[j].insert(r);
				}
				if (P[j].size() > P[i].size()){
					swap(P[j], P[i]);
				}
				for (int r : P[j]){
					Class[r] = j;
				}
				for (char c = 'a'; c <= 'z'; ++c){
					q.push({j,c});
				}
			}
		}
	}
	int start = Class[0];
	vector<int> classToId(P.size());
	auto merge = [&](int classId){
		vector<int> nodes;
		for (auto node : P[classId]){
			nodes.pb(node);
		}
		sort(all(nodes));
		classToId[classId] = nodes[0];
		for (char c = 'a'; c <= 'z'; ++c){
			vector<int>& to = in[nodes[0]][c];
			for (int i = 1; i < nodes.size(); ++i){
				auto& from = in[nodes[i]][c];
				if (!from.empty()){
					to.insert(to.end(), all(from));
				}
			}
			for (auto& v : to){
				v = Class[v];
			}
			sort(all(to));
			to.resize(unique(all(to)) - to.begin());
		}
		P[classId].clear();
		P[classId].insert(nodes[0]);
	};
	for (int i = 0; i < P.size(); ++i){
		if (!P[i].empty()){
			merge(i);
		}
	}
	vector<int> terminals;
	for (int i = 0; i < P.size(); ++i){
		if (!P[i].empty() && term[classToId[i]]){
			terminals.pb(i);
		}
	}
	vector<int> classToName(MAXN, -1);
	classToName[start] = 1;
	int last = 2;
	for (int i = 0; i < P.size(); ++i){
		if (i == start || P[i].empty()){
			continue;
		}
		classToName[i] = last++;
	}
	int vCnt = 0, eCnt = 0;
	for (int i = 0; i < P.size(); ++i){
		vCnt += (P[i].size() == 1);
	}

	for (int i = 0; i < P.size(); ++i){
		if (P[i].empty()){
			continue;
		}
		for (auto& [ch, edges] : in[classToId[i]]){
			eCnt += edges.size();
		}
	}
	ofs << vCnt << ' ' << eCnt << ' ' << terminals.size() << '\n';
	for (int v : terminals){
		ofs << classToName[v] << ' ';
	}
	ofs << '\n';
	for (int i = 0; i < P.size(); ++i){
		if (P[i].empty()){
			continue;
		}
		for (auto& [ch, edges] : in[classToId[i]]){
			for (int edge : edges){
				ofs << classToName[edge] << ' ' << classToName[i] << ' ' << ch << '\n';
			}
		}
	}
}
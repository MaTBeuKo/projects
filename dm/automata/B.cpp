#include <vector>
#include <iostream>
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
#define uint uint64_t

//int nxt(){
//	int x;
//	cin >> x;
//	return x;
//}

struct Node{
	bool good;
	map<char, vector<Node*>> m;
};

map<pair<Node*, int>, bool> mem;

bool go(Node* node, string& s, int pos){
	if (mem.count({node,pos})){
		return mem[{node, pos}];
	}
	bool res = false;
	if (node == nullptr){
		return false;
	}
	if (pos == s.size()-1){
		res = node->good;
	}
	else{
		for (auto& v : node->m[s[pos + 1]]){
			res |= go(v, s, pos + 1);
		}
	}
	mem[{node, pos}] = res;
	return res;
}

int32_t main() {
	//cin.tie(nullptr)->ios_base::sync_with_stdio(false);
	//setvbuf(stdout, nullptr, _IOFBF, BUFSIZ);
	ifstream ifs = ifstream("problem2.in");
	ofstream ofs = ofstream("problem2.out");
	string s;
	ifs >> s;
	int n, m, k;
	ifs >> n >> m >> k;
	vector<Node*> a(n);
	for (auto& v : a){
		v = new Node();
	}
	for (int i = 0; i < k; ++i){
		int id;
		ifs >> id;
		--id;
		a[id]->good = true;
	}
	for (int i = 0; i < m; ++i){
		int x, y;
		ifs >> x >> y;
		--x, --y;
		char c;
		ifs >> c;
		a[x]->m[c].pb(a[y]);
	}

	ofs << (go(a[0], s, -1) ? "Accepts" : "Rejects") << endl;
}
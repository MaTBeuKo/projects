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
//  int x;
//  cin >> x;
//  return x;
//}

struct Node{
	bool good;
	map<char, Node*> m;
};

map<Node*, bool> been;

bool eq(Node* u, Node* v){
	if (been[u]){
		return true;
	}
	been[u] = true;
	if (u->m.size() != v->m.size() || u->good != v->good){
		return false;
	}
	bool ans = true;
	for (auto& [l, r] : u->m){
		if (v->m.count(l) == 0){
			return false;
		}
		ans &= eq(r, v->m[l]);
	}
	return ans;
}

int32_t main() {
	cin.tie(nullptr)->ios_base::sync_with_stdio(false);
	setvbuf(stdout, nullptr, _IOFBF, BUFSIZ);
	ifstream ifs = ifstream("isomorphism.in");
	ofstream ofs = ofstream("isomorphism.out");

	vector<Node*> a,b;
	auto read = [&ifs](vector<Node*> &a){
		int n, m, k;
		ifs >> n >> m >> k;
		a = vector<Node*>(n);
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
			a[x]->m[c] = a[y];
		}
	};
	read(a), read(b);
	if (eq(a[0], b[0])){
		ofs << "YES\n";
	}
	else{
		ofs << "NO\n";
	}
}
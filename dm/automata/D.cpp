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

int MOD = 1e9 + 7;

struct Node{
	bool good = false;
	map<Node*, int> in;
};
Node* first;
int maxDepth;
map<pair<Node*, int>, int> mem;
int go(Node* node, int d){
	if (mem.count({node, d})){
		return mem[{node, d}];
	}
	if (d >= maxDepth){
		return node == first;
	}
	int ans = 0;
	for (auto& [v, weight] : node->in){
		ans += go(v, d + 1) * weight;
		ans %= MOD;
	}
	mem[{node, d}] = ans;
	return ans;
}

int32_t main() {
	cin.tie(nullptr)->ios_base::sync_with_stdio(false);
	setvbuf(stdout, nullptr, _IOFBF, BUFSIZ);
	ifstream ifs = ifstream("problem4.in");
	ofstream ofs = ofstream("problem4.out");

	vector<Node*> a;
	auto read = [&ifs](vector<Node*>& a){
		int n, m, k;
		ifs >> n >> m >> k >> maxDepth;
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
			a[y]->in[a[x]]++;
		}
	};
	read(a);
	first = a[0];
	int ans = 0;
	for (int i = 0; i < a.size(); ++i){
		if (a[i]->good){
			ans += go(a[i], 0);
			ans %= MOD;
		}
	}
	ofs << ans << '\n';
}
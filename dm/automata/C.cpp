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


struct Node{
	bool good = false;
	map<Node*, int> in;
	map<Node*, int> out;
	bool cycle = false;
	int state = 0;
	bool reachable = false;
	int cnt = 0;
};
Node* first;
map<Node*, int> mem;
void reach(Node* v){
	if (v->reachable){
		return;
	}
	v->reachable = 1;
	for (auto& [node, weight] : v->out){
		reach(node);
	}
}

int go(Node* v){
	if (v->state == 1){
		return 0;
	}
	v->state = 1;
	int ans = v == first;
	for (auto& [node, weight] : v->in){
		ans = (ans + weight * go(node));
	}
	v->state = 2;
	return ans;
}
vector<Node*> cycles;

void findCycles(Node* node){
	if (cycles.size() > 100'000){
		return;
	}
	if (node->state == 1){
		cycles.pb(node);
		return;
	}
	node->state = 1;
	for (auto& [v, weight] : node->out){
		findCycles(v);
	}
	node->state = 2;
}

int32_t main() {
	cin.tie(nullptr)->ios_base::sync_with_stdio(false);
	setvbuf(stdout, nullptr, _IOFBF, BUFSIZ);
	ifstream ifs = ifstream("problem3.in");
	ofstream ofs = ofstream("problem3.out");

	vector<Node*> a;
	auto read = [&ifs](vector<Node*>& a){
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
			a[y]->in[a[x]]++;
			a[x]->out[a[y]]++;
		}
	};
	read(a);
	a[0]->cnt = 1;
	first = a[0];
	findCycles(first);
	for (int i = 0; i < cycles.size(); ++i){
		reach(cycles[i]);
	}
	for (int i = 0; i < a.size(); ++i){
		if (a[i]->good && a[i]->reachable){
			ofs << -1 << '\n';
			return 0;
		}
	}
	reach(first);
	int ans = 0;
	for (int i = 0; i < a.size(); ++i){
		if (a[i]->good && a[i]->reachable){
			ans += go(a[i]);
		}
	}
	ofs << ans << '\n';
}
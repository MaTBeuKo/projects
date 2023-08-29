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
#define int long long
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
vector<unordered_map<char, set<int>>> out(MAXN);
int last = 0;;
map<set<int>, int> names;
vector<bool> term(MAXN);
map<int, map<char, int>> outd;

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
	//cin.tie(nullptr)->ios_base::sync_with_stdio(false);
	//setvbuf(stdout, nullptr, _IOFBF, BUFSIZ);
	ifstream ifs = ifstream("problem5.in");
	ofstream ofs = ofstream("problem5.out");
	int n, m, k;
	ifs >> n >> m >> k >> maxDepth;
	for (int i = 0; i < k; ++i){
		int x;
		ifs >> x;
		term[x - 1] = true;
	}
	for (int i = 0; i < m; ++i){
		int x, y;
		char c;
		ifs >> x >> y >> c;
		--x, --y;
		out[x][c].insert(y);
	}
	queue<set<int>> P;
	set<set<int>> Q;
	Q.insert({0});
	set<int> been;
	been.insert(last);
	P.push({0});
	names[{0}] = last++;
	while (!P.empty()){
		set<int> current = P.front();
		P.pop();
		for (char c = 'a'; c <= 'z'; ++c){
			set<int> qd;
			for (int p : current){
				qd.insert(all(out[p][c]));
			}
			if (qd.empty()){
				continue;
			}
			if (!names.count(qd)){
				names[qd] = last++;
			}
			outd[names[current]][c] = names[qd];
			if (!been.count(names[qd])){
				been.insert(names[qd]);
				P.push(qd);
				Q.insert(qd);
			}
		}
	}
	vector<int> terminals;
	for (auto& s : Q){
		for (auto v : s){
			if (term[v]){
				terminals.pb(names[s]);
				break;
			}
		}
	}
	vector<Node*> a(last);
	for (int i = 0; i < a.size(); ++i){
		a[i] = new Node();
	}
	first = a[0];
	for (int i = 0; i < last; ++i){
		for (auto& [ch, v] : outd[i]){
			a[v]->in[a[i]]++;
		}
	}
	for (auto v : terminals){
		a[v]->good = true;
	}
	int ans = 0;
	for (int i = 0; i < a.size(); ++i){
		if (a[i]->good){
			ans += go(a[i], 0);
			ans %= MOD;
		}
	}
	ofs << ans << '\n';
}

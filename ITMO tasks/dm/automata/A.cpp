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
	map<char, Node*> m;
};


int32_t main() {
	//cin.tie(nullptr)->ios_base::sync_with_stdio(false);
	//setvbuf(stdout, nullptr, _IOFBF, BUFSIZ);
	ifstream ifs = ifstream("problem1.in");
	ofstream ofs = ofstream("problem1.out");
	string s;
	ifs >> s;
	int n, m, k;
	ifs >> n >> m >> k;
	stack<char> st;
	for (int i = s.size() - 1; i >= 0; i--){
		st.push(s[i]);
	}
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
		a[x]->m[c] = a[y];
	}
	Node* node = a[0];
	while (!st.empty()){
		if (node == nullptr){
			break;
		}
		if (s.empty()){
			break;
		}
		char c = st.top();
		st.pop();
		node = node->m[c];
	}
	ofs << (node != nullptr && node->good ? "Accepts" : "Rejects") << endl;
}
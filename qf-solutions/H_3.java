import java.util.*;

public class Main {
    static final int N = 1000100;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] a = new int[N], prefix = new int[N], ind = new int[N], d = new int[N];
        int mx = 0;
        for (int i = 1; i <= n; i++) {
            a[i] = in.nextInt();
            mx = Math.max(mx, a[i]);
            prefix[i] = prefix[i - 1] + a[i];
            for (int j = prefix[i - 1]; j < prefix[i]; j++) {
                ind[j] = i - 1;
            }
        }
        for (int i = prefix[n]; i < N; i++) {
            ind[i] = n;
        }
        int qq = in.nextInt();
        for (int q = 0; q < qq; q++) {
            int x = in.nextInt();
            int s = 0, i = 0;
            if (x < mx) {
                System.out.println("Impossible");
            } else if (d[x] != 0) {
                System.out.println(d[x]);
            } else {
                while (i < n) {
                    i = ind[s + x];
                    d[x]++;
                    s = prefix[i];
                }
                System.out.println(d[x]);
            }
        }
    }
}
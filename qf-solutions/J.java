import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class J {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = Integer.parseInt(in.nextLine());
        int[][] spots = new int[n][n], ans = new int[n][n];
        for (int i = 0; i < n; i++) {
            String s = in.nextLine();
            for (int j = 0; j < n; j++) {
                spots[i][j] = s.charAt(j) - '0';
            }
        }
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                int c = 0;
                for (int k = i + 1; k < j; k++) {
                    c += spots[k][j] * ans[i][k]%10;
                    c%=10;
                }
                if (c != spots[i][j]) {
                    ans[i][j] = 1;
                }
            }
        }
        for (var ar : ans) {
            for (int v : ar) {
                System.out.print(v);
            }
            System.out.println();
        }
    }
}

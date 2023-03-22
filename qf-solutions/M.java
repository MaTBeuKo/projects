import java.util.*;

public class M {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int tt = in.nextInt();
        for (int t = 0; t < tt; t++) {
            int n = in.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.nextInt();
            }
            Map<Integer, Integer> map = new HashMap<>();
            int s = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (a[i] == a[j] && map.containsKey(a[i])) {
                        s += map.get(a[i]);
                    } else {
                        s += map.getOrDefault(2 * a[i] - a[j], 0);
                    }
                }
                if (map.containsKey(a[i])) {
                    map.put(a[i], map.get(a[i]) + 1);
                } else {
                    map.put(a[i], 1);
                }
            }
            System.out.println(s);
        }
    }
}

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextInt();
        long xr = Long.MIN_VALUE, xl = Long.MAX_VALUE, yr = Long.MIN_VALUE, yl = Long.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            long x, y, h;
            x = scanner.nextLong();
            y = scanner.nextLong();
            h = scanner.nextLong();

            xl = Math.min(xl, x - h);
            xr = Math.max(xr, x + h);
            yl = Math.min(yl, y - h);
            yr = Math.max(yr, y + h);
        }
        System.out.println((xl + xr) / 2 + " " + (yl + yr) / 2 + " " + (Math.max(xr - xl, yr - yl) + 1) / 2);
    }
}
import java.util.Scanner;

public class B {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        final int l = 710;
        for (int i = -25000; i < -25000+n; i++){
            System.out.println(l*i);
        }
    }
}

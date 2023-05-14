import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int l = 710;
        for (int i = -25000; i < -25000+n; i++){
            System.out.println(l*i);
        }
    }
}
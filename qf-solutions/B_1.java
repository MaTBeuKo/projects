import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        double l = -710*25000;
        double d = 0.00006*25000;
        for (int i = 0; i < n; i++, l += d){
            System.out.println((int)l);
        }
    }
}
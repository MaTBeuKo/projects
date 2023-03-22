import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        int a,b,n;
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        b = scanner.nextInt();
        n = scanner.nextInt();
        System.out.println(2*((n-a-1)/(b-a))+1);
    }
}
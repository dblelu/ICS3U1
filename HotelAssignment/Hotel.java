import java.io.*;
import java.nio.Buffer;
import java.util.Scanner; 

public class Hotel {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter a employee number to log in: ");
            try {
                int number = getInt();
                verify(number);
            } catch (IOException e) {
                System.out.println("User not found");
            }
        }
    }
    public static void verify(int number) {
        if (number == 0) admin();
        File file = new File(number + ".txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int password = Integer.parseInt(br.readLine());
            System.out.println("Enter your password");
            int input = getInt();
            int cnt = 0;
            while (password != input) {
                if (cnt >= 3) {
                    System.out.println("You entered the incorrect password too many times");
                    return;
                }
                cnt++; 
                System.out.println("Enter your password");
                input = getInt(); 
            }
            employee(number);
        } catch (IOException e) {
            System.out.println("Employee number not valid");
        }
    }
    public static void employee(int number) {
        
    }
    public static int getInt() {
        Scanner sc = new Scanner(System.in);
        String n = sc.nextLine();
        while (true) {
            try {
                return Integer.parseInt(n);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

}
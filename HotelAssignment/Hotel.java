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
            br.close();
        } catch (IOException e) {
            System.out.println("Employee number not valid");
        }
    }
    public static void employee(int number) {
        //Enter filler code that displays the name
        boolean goBack = false;
        while (!goBack) {
            System.out.println("Enter instructions (1, 2, 3, 4)");
            int input = getInt();
            switch (input) {
                case 1:
                    makeReservations();
                    break;
                case 2:
                    cancelReservation();
                    break;
                case 3:
                    changeReservation();
                    break;
                case 4: 
                    changePin();
                    break;
                default: 
                    goBack = true;
                    break;
            }
        }
    }
    public static boolean makeReservations(int employee) {
        int numberOfReservations, day, room, currentReservationDays[], currentReservationRooms[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            currentReservationDays = new int[numberOfReservations];
            currentReservationRooms = new int[numberOfReservations];
            for (int i = 0; i < numberOfReservations; i++) {
                currentReservationDays[i] = Integer.parseInt(br.readLine());
                currentReservationRooms[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            return false;
        }
        boolean valid = false;
        while (!valid) {
            System.out.println("Enter a day to reserve (int)");
            day = getInt();
            if (!same(currentReservationDays, day)) valid = true;
        }
        valid = false;
        while (!valid) {
            System.out.println("Enter a room to reserve (int)");
            room = getInt();
            if (!same(currentReservationRooms, room)) valid = true;
        }
        System.out.println("You have a reservation on day" + day + " in room " + room);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("reservations.txt", false));
            br.write(++numberOfReservations); br.newLine();
            for (int i = 0; i < numberOfReservations; i++) {
                br.write(currentReservationDays[i]); br.newLine();
                br.write(currentReservationRooms[i]); br.newLine();
            }
            br.close();
        } catch (IOException e) {
            return false;
        }
        return true;   
    }
    public static boolean same(int[] a, int x) {
        for (int i = 0; i < a.length; i++) if (a[i] == x) return false;
        return true;
    }
    public static boolean cancelReservations() {
        int numberOfReservations, day, room, currentReservationDays[], currentReservationRooms[];
        System.out.println("Enter a day to cancel (int)");
        day = getInt();
        System.out.println("Enter a room to cancel (int)");
        room = getInt();
        boolean valid = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            currentReservationDays = new int[numberOfReservations];
            currentReservationRooms = new int[numberOfReservations];
            for (int i = 0; i < numberOfReservations; i++) {
                currentReservationDays[i] = Integer.parseInt(br.readLine());
                currentReservationRooms[i] = Integer.parseInt(br.readLine());
                if (currentReservationDays[i] == day && currentReservationRooms[i] == room) valid = true;
            }
            br.close();
        } catch (IOException e) {
            return false;
        }
        
        if (!valid) {
            System.out.println("This reservation does not exist");
            return false;
        }
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("reservations.txt", false));
            bw.write(--numberOfReservations); br.newLine();
            for (int i = 0; i < numberOfReservations; i++) {
                if (!(room == currentReservationRooms[i] && day == currentReservationDays[i])) {
                    bw.write(currentReservationDays[i]); bw.newLine();
                    bw.write(currentReservationRooms[i]); bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;   
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
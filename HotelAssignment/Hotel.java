import java.io.*;
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
                if (input == 0) return;
                if (number == 0 && password == 1234) {
                    admin();
                    return;
                }
                cnt++; 
                System.out.println("Enter your password or 0 to go back");
                input = getInt(); 
            }
            br.close();
            employee(number);
        } catch (IOException e) {
            System.out.println("Employee number not valid");
        }
    }
    public static void employee(int number) {
        //Enter filler code that displays the name
        boolean goBack = false;
        while (!goBack) {
            System.out.println("Enter instructions:\n1. Make Reservations\n2. Cancel Reservations\n3. Change Reservations\n4. Change Pin\n5. List all rooms for date\n6. List all reservations for date\n7. Look up reservations for given name\n8. Log Out");
            int input = getInt();
            switch (input) {
                case 1:
                    System.out.println("Enter the first name and last name");
                    int first = sc.nextLine();
                    int last = sc.nextLine();
                    System.out.println("Enter a date");
                    int day = sc.nextLine();
                    makeReservations(first, last, day, -1);
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
                case 5: 
                    listRoomsDate();
                    break;
                case 6:
                    listReservationsDate();
                    break;
                case 7:
                    lookUpName();
                    break;
                default: 
                    goBack = true;
                    break;
            }
        }
    }
    public static boolean makeReservations(int first, int last, int day, int sameRoom) {
        //Get reservation info
        int numberOfReservations, firstNames[], lastNames[], reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            reservationDays = new int[numberOfReservations+1];
            reservationRooms = new int[numberOfReservations+1];
            for (int i = 0; i < numberOfReservations; i++) {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            return false;
        }

        firstNames[numberOfReservations] = sc.nextLine();
        lastNames[numberOfReservations] = sc.nextLine();
        listAvailableRooms(reservationDays, reservationRooms, day);
        System.out.println;

        if (!fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms)) return false;
    }
    public static void listAvailableRooms(int[] reservationDays, int[] reservationRooms, int day) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("rooms.txt")));
            int numRooms = Integer.parseInt(br.readLine());
            for (int i = 0; i < numRooms; i++) {
                int room = Integer.praseInt(br.readLine());
                if (available(reservationDays, reservationRooms, day, room)) {
                    System.out.println("Room " + room);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error" + e);
        }
    }
    public static boolean available(int[] reservationDays, int[] reservationRooms, int day, int room) {
        for (int i = 0; i < reservationDays.length; i++) {
            if (day == reservationDays[i] && room == reservationRooms[i]) return false;
        }
        return true;
    }
    //helper method 
    public static boolean fillReservationFile(int numberOfReservations, int[] firstNames, int[] lastNames, int[] reservationDays, int[] reservationRooms, int skip) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("reservations.txt", false));
            //fix the skip part later
            br.write(numberOfReservations); br.newLine();
            for (int i = 0; i < numberOfReservations; i++) {
                if (i != skip) {
                    br.write(firstNames[i]); br.newLine();
                    br.write(lastNames[i]); br.newLine();
                    br.write(reservationDays[i]); br.newLine();
                    br.write(reservationRooms[i]); br.newLine();
                    br.write(employeeNumber[i]); br.newLine();
                }
            }
            br.close();
        } catch (IOException e) {
            return false;
        }
        return true;   
    }
    public static boolean cancelReservations() {
       
    }
    public static boolean changeReservation() {
        if (!cancelReservations()) return false;
        if (!makeReservations()) return false;
        return true;
    }
    public static boolean changePin() {

    }
    public static int getInt() {
        Scanner sc = new Scanner(System.in);
        String n = sc.nextLine();
        while (true) {
            try {
                return Integer.parseInt(n);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. \nTry again: ");
            }
        }
    }

}
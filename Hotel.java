import java.io.*;
import java.util.Scanner; 

public class Hotel {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter a employee number to log in: ");
            
            int number = getInt();
            verify(number);
        }
    }
    public static void verify(int num) {
        if (num == 0) {
            admin();
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("employee.txt"));
            int numberOfEmployees = Integer.parseInt(br.readLine());
            
            boolean found = false;
            int number = -1;
            int password = -1;
            String first = "";
            String last = "";
            for (int i = 0; i < numberOfEmployees && !found; i++) {
                number = Integer.parseInt(br.readLine());
                password = Integer.parseInt(br.readLine());
                first = br.readLine();
                last = br.readLine();
                if (number == num) found = true;
            }
            System.out.println("Enter your password for " + number + "or 0 to go back");
            int input = getInt();
            int cnt = 0;
            while (password != input) {
                if (cnt >= 3) {
                    System.out.println("You entered the incorrect password too many times");
                    return;
                }
                if (input == 0) return;
                cnt++; 
                System.out.println("Enter your password for " + number + "or 0 to go back");
                input = getInt(); 
            }
            br.close();
            employee(number, first, last);
        } catch (IOException e) {
            System.out.println("Error" + e);
        }
    }
    public static void employee(int number, String empFirstName, String empLastName) {
        Scanner sc = new Scanner(System.in);
        //Enter filler code that displays the name
        boolean goBack = false;
        String first, last;
        int day;
        while (!goBack) {
            System.out.println("Enter instructions:\n1. Make Reservations\n2. Cancel Reservations\n3. Change Reservations\n4. Change Pin\n5. List all rooms for date\n6. List all reservations for date\n7. Look up reservations for given name\n8. Log Out");
            int input = getInt();
            switch (input) {
                case 1:
                    System.out.println("Enter the first name and last name");
                    first = sc.nextLine();
                    last = sc.nextLine();
                    System.out.println("Enter a date");
                    day = getInt();
                    makeReservations(first, last, day);
                    break;
                case 2:
                    System.out.println("Enter the first name and last name");
                    first = sc.nextLine();
                    last = sc.nextLine();
                    cancelReservations(first, last);
                    break;
                case 3:
                //impl
                    System.out.println("Enter the first name and last name");
                    first = sc.nextLine();
                    last = sc.nextLine();
                    changeReservations(first, last);    
                    break;
                case 4:
                //impl 
                    changePin();
                    break;
                case 5: 
                    System.out.println("Enter a date");
                    day = getInt();    
                    listAvailableRooms(day);
                    break;
                case 6:
                    listReservationsDate();
                    break;
                case 7:
                    listReservationsName();
                    break;
                default: 
                    goBack = true;
                    break;
            }
        }
    }
    public static boolean makeReservations(String first, String last, int day) {
        Scanner sc = new Scanner(System.in);
        //Get reservation info
        String firstNames[], lastNames[];
        int numberOfReservations,  reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations+1];
            lastNames = new String[numberOfReservations+1];
            reservationDays = new int[numberOfReservations+1];
            reservationRooms = new int[numberOfReservations+1];
            employeeNumbers = new int[numberOfReservations+1];
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
        //add the new info to the end of reservation list
        firstNames[numberOfReservations] = sc.nextLine();
        lastNames[numberOfReservations] = sc.nextLine();
        int[] rooms = getAvailableRooms(reservationDays, reservationRooms, day);
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] != -1) System.out.println("1. Room: " + rooms[i]);
        }
        System.out.println("Enter the room you want (int) ");
        int room = getInt();
        boolean valid = false;
        while (!valid) {
            if (available(reservationDays, reservationRooms, day, room)) {
                valid = true;
            } else {
                System.out.println("That room is not available");
            }
            System.out.println("Enter the room you want (int) ");
            room = getInt();
        }
        reservationDays[numberOfReservations] = day;
        reservationRooms[numberOfReservations] = room;
        //fill the reservation list with the new info
        if (!fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms, employeeNumbers, -1)) return false;
        return true;
    }
    public static int[] getAvailableRooms(int[] reservationDays, int[] reservationRooms, int day) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("rooms.txt")));
            int numRooms = Integer.parseInt(br.readLine());
            int[] ret = new int[numRooms];
            for (int i = 0; i < numRooms; i++) {
                int room = Integer.parseInt(br.readLine());
                if (available(reservationDays, reservationRooms, day, room)) {
                    ret[i] = room;
                } else {
                    ret[i] = -1;
                }
            }
            br.close();
            return ret;
        } catch (IOException e) {
            System.out.println("Error" + e);
        }
        return null;
    }
    public static boolean available(int[] reservationDays, int[] reservationRooms, int day, int room) {
        for (int i = 0; i < reservationDays.length; i++) {
            if (day == reservationDays[i] && room == reservationRooms[i]) return false;
        }
        return true;
    }
    public static boolean listAvailableRooms(int day) {
        String firstNames[], lastNames[];
        int numberOfReservations,  reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
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
        int[] rooms = getAvailableRooms(reservationDays, reservationRooms, day);
        System.out.println("Available Rooms: ");
        for (int i = 0; i < rooms.length; i++) {
            System.out.println("Room " + rooms[i]);
        }
        return true;
    }
    //helper method 
    public static boolean fillReservationFile(int numberOfReservations, String[] firstNames, String[] lastNames, int[] reservationDays, int[] reservationRooms, int[] employeeNumbers, int skip) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("reservations.txt", false));
            //fix the skip part later
            bw.write(numberOfReservations); bw.newLine();
            for (int i = 0; i < numberOfReservations; i++) {
                if (i != skip) {
                    bw.write(firstNames[i]); bw.newLine();
                    bw.write(lastNames[i]); bw.newLine();
                    bw.write(reservationDays[i]); bw.newLine();
                    bw.write(reservationRooms[i]); bw.newLine();
                    bw.write(employeeNumbers[i]); bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;   
    }
    public static boolean cancelReservations(String first, String last) {
        String firstNames[], lastNames[]; 
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
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
        boolean[] valid = getReservationsByName(first, last);
        int[] reserv = new int[valid.length + 1];
        System.out.println("Here are your reservations: ");
        for (int i = 0, cnt = 1; i < valid.length; i++) {
            if (valid[i]) {
                System.out.println("1. Date " + reservationDays[i] + "\nRoom" + reservationRooms[i]);
                reserv[cnt++] = i;
            }
        }
        System.out.println("Enter a reservation to cancel");
        int cancel = getInt();
        while (!valid[reserv[cancel]]) {
            System.out.println("That reservation doesn't exist");
            System.out.println("Enter a reservation to cancel");
            cancel = getInt();
        }
        fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms, employeeNumbers, reserv[cancel]); 
        return true;
    } 
    public static boolean[] getReservationsByName(String first, String last) {
        String firstNames[], lastNames[]; 
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
            boolean[] ret = new boolean[numberOfReservations];
            for (int i = 0; i < numberOfReservations; i++) {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
                ret[i] = (firstNames[i].equals(first) && lastNames[i].equals(last));
            }
            br.close();
            return ret;
        } catch (IOException e) {
            System.out.println("ERROR!" + e);
        }
        return null;
    }
    public static boolean listReservationsName() {
        Scanner sc = new Scanner(System.in);
        String firstNames[], lastNames[];
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
            for (int i = 0; i < numberOfReservations; i++) {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error" + e);
            return false;
        }
        System.out.println("Enter first name and last name");
        String first = sc.nextLine();
        String last = sc.nextLine();

        boolean[] valid = getReservationsByName(first, last);
        System.out.println("Here are your reservations: ");
        for (int i = 0, cnt = 1; i < valid.length; i++) {
            if (valid[i]) {
                System.out.println("1. Name: " + first + " " + last + "\n  Date " + reservationDays[i] + "\n  Room" + reservationRooms[i]);
            }
        }
        return true;
    }
    public static boolean changeReservations(String first, String last) {
        String firstNames[], lastNames[];
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
            for (int i = 0; i < numberOfReservations; i++) {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error" + e);
            return false;
        }
        return true;
    }
    public static boolean changePin() {
        return false;
    }
    public static boolean listReservationsDate() {
        return false;
    }
    public static void admin() {

    }
    // Method to take in an int as input, not allowing the user to break the system with strings. 
    public static int getInt() {
        Scanner sc = new Scanner(System.in);
        String n = sc.nextLine();
        while (true) {
            try {
                if (Integer.parseInt(n) < 0) throw new NumberFormatException();
                return Integer.parseInt(n);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. \nTry again: ");
            }
        }
    }

}
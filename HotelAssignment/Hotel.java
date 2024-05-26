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
            System.out.println("Enter your password or 0 to go back");
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
                    cancelReservation(first, last);
                    break;
                case 3:
                //impl
                    changeReservation();    
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
                    System.out.pritnln("Enter a date");
                    day = getInt();
                    listReservationsDate(date);
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
    public static boolean makeReservations(int first, int last, int day) {
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
        if (!fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms, -1)) return false;
        return true;
    }
    public static int[] getAvailableRooms(int[] reservationDays, int[] reservationRooms, int day) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("rooms.txt")));
            int numRooms = Integer.parseInt(br.readLine());
            int[] ret = new int[numRooms];
            for (int i = 0; i < numRooms; i++) {
                int room = Integer.praseInt(br.readLine());
                if (available(reservationDays, reservationRooms, day, room)) {
                    ret[i] = room;
                } else {
                    ret[i] = -1;
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
    public static boolean listAvailableRooms(int day) {
        int numberOfReservations, firstNames[], lastNames[], reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
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
    public static boolean cancelReservations(String first, String last) {
        int numberOfReservations, firstNames[], lastNames[], reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
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
        fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms, numberOfReservations, reserv[cancel]); 
    } 
    public static boolean[] getReservationsByName(String first, String last) {
        String firstNames[], lastNames[]; 
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
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
        } catch (IOException e) {
            System.out.println("ERROR!" + e);
        }
        return ret[i];
    }
    public static void listReservationsName() {
        int numberOfReservations, firstNames[], lastNames[], reservationDays[], reservationRooms[], employeeNumbers[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            numberOfReservations = Integer.parseInt(br.readLine());
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
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
        }
        System.out.println("Enter first name and last name");
        String first = sc.nextLine();
        String last = sc.nextLine();
        boolean[] valid = getReservationsByName(first, last);
        for (int i = 0; i < valid.length; i++) {
            
        }
    }
    public static boolean changeReservation() {
        
    }
    public static boolean changePin() {

    }
    // Method to take in an int as input, not allowing the user to break the system with strings. 
    public static int getInt() {
        Scanner sc = new Scanner(System.in);
        String n = sc.nextLine();
        while (true) {
            try {
                if (Interger.parseInt(n) < 0) throw NumberFormatException;
                return Integer.parseInt(n);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. \nTry again: ");
            }
        }
    }

}
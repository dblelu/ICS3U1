
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
            br.close();
            if (!found) {
                System.out.println("Employee doesn't exist");
                return;
            }
            System.out.println("Enter your password for " + number + " or 0 to go back");
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
                    if (!makeReservations(number, first, last, day)) System.out.println("Error with reservation!");
                    break;
                case 2:
                    System.out.println("Enter the first name and last name");
                    first = sc.nextLine();
                    last = sc.nextLine();
                    cancelReservations(first, last);
                    break;
                case 3:
                    System.out.println("Enter the first name and last name");
                    first = sc.nextLine();
                    last = sc.nextLine();
                    changeReservations(first, last);    
                    break;
                case 4:
                //impl 
                    changePin(number);
                    break;
                case 5: 
                    System.out.println("Enter a date");
                    day = getInt();    
                    listAvailableRooms(day);
                    break;
                case 6:
                    System.out.println("Enter a date");
                    day = getInt();    
                    listReservationsDate(day);
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
    //helper method 
    public static boolean fillReservationFile(int numberOfReservations, String[] firstNames, String[] lastNames, int[] reservationDays, int[] reservationRooms, int[] employeeNumbers, int skip) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("reservations.txt", false));
            //fix the skip part later
            if (skip == -1) {
                bw.write(numberOfReservations+""); 
            } else {
                bw.write(numberOfReservations-1+"");
            } 
            bw.newLine();
            System.out.println(numberOfReservations);
            for (int i = 0; i < numberOfReservations; i++) {
                if (i != skip) {
                    bw.write(firstNames[i]); bw.newLine();
                    bw.write(lastNames[i]); bw.newLine();
                    bw.write(reservationDays[i]+""); bw.newLine();
                    bw.write(reservationRooms[i]+""); bw.newLine();
                    bw.write(employeeNumbers[i]+""); bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;   
    }
    public static boolean makeReservations(int number, String first, String last, int day) {
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
        
        int[] rooms = getAvailableRooms(reservationDays, reservationRooms, day);
        System.out.println("Here is a list of available rooms");
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] != -1) System.out.println(i + 1 + ". Room: " + rooms[i]);
        }
        System.out.println("Enter the room you want (int) ");
        int room = getInt()-1;
        boolean valid = false;
        while (!valid) {
            if (available(reservationDays, reservationRooms, day, rooms[room])) {
                valid = true;
            } else {
                System.out.println("That room is not available");
                System.out.println("Enter the room you want (int) ");
                room = getInt()-1;
            }
        }
        //add the new info to the end of reservation list
        firstNames[numberOfReservations] = first;
        lastNames[numberOfReservations] = last;
        reservationDays[numberOfReservations] = day;
        reservationRooms[numberOfReservations] = rooms[room];
        employeeNumbers[numberOfReservations] = number;
        //fill the reservation list with the new info
        if (!fillReservationFile(numberOfReservations + 1, firstNames, lastNames, reservationDays, reservationRooms, employeeNumbers,  -1)) return false;
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
            if (rooms[i] != -1) System.out.println("Room " + rooms[i]);
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
                System.out.println(i+1+".\n Date " + reservationDays[i] + "\nRoom " + reservationRooms[i]);
                reserv[cnt++] = i;
            }
        }
        System.out.println("Enter a reservation to cancel or 0 to abort");
        int cancel = getInt()-1;
        if (cancel == 0) return false;
        while (!valid[reserv[cancel]]) {
            System.out.println("That reservation doesn't exist");
            System.out.println("Enter a reservation to cancel");
            cancel = getInt()-1;
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
                System.out.println(cnt + ". Name: " + first + " " + last + "\n  Date " + reservationDays[i] + "\n  Room" + reservationRooms[i]);
                cnt++;
            }
        }
        return true;
    }
    public static boolean changeReservations(String first, String last) {
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
        boolean[] valid = getReservationsByName(first, last);
        int[] reserv = new int[valid.length + 1];
        System.out.println("Here are your reservations: ");
        for (int i = 0, cnt = 1; i < valid.length; i++) {
            if (valid[i]) {
                System.out.println(i+1+".\n Date " + reservationDays[i] + "\nRoom " + reservationRooms[i]);
                reserv[cnt++] = i;
            }
        }
        System.out.println("Enter a reservation to change or 0 to abort");
        int idx = getInt();
        if (idx == 0) return false;
        while (!valid[reserv[idx]]) {
            System.out.println("That reservation doesn't exist");
            System.out.println("Enter a reservation to change or 0 to abort");
            idx = getInt();
        }
        
        System.out.println("What do you want to change: ");
        System.out.println("1. Name \n2. Date\n3. Room. \n4. Continue");
        int op = getInt();
        while (op != 4) {
            switch (op) {
                case 1:
                    System.out.println("Enter new first and last name");
                    firstNames[reserv[idx]] = sc.nextLine();
                    lastNames[reserv[idx]] = sc.nextLine();
                    break;
                case 2:
                    System.out.println("Enter a new day");
                    fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms, employeeNumbers, reserv[idx]);
                    makeReservations(numberOfReservations, firstNames[reserv[idx]], lastNames[reserv[idx]], reservationDays[reserv[idx]] = getInt());
                    break;
                case 3:
                    fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms, employeeNumbers, reserv[idx]);
                    makeReservations(numberOfReservations, firstNames[reserv[idx]], lastNames[reserv[idx]], reservationDays[reserv[idx]]);
                    break;
            }
            System.out.println("What do you want to change: ");
            System.out.println("1. Name \n2. Date\n3. Room\n4. Continue");
            op = getInt();
        }
        fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms, employeeNumbers, -1); 
        return true;

    }
    public static boolean changePin(int num) {
        String first[], last[];
        int numberOfEmployees, number[], password[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("employee.txt"));
            numberOfEmployees = Integer.parseInt(br.readLine());
            int found = -1;
            number = new int[numberOfEmployees];
            password = new int[numberOfEmployees];
            first = new String[numberOfEmployees];
            last = new String[numberOfEmployees];
            for (int i = 0; i < numberOfEmployees && found == -1; i++) {
                number[i] = Integer.parseInt(br.readLine());
                password[i] = Integer.parseInt(br.readLine());
                first[i] = br.readLine();
                last[i] = br.readLine();
                if (number[i] == num) found = i;
            }
            br.close();
            if (found == -1) {
                System.out.println("Employee doesn't exist");
                return false;
            }
            System.out.println("Enter your new password");
            int pass1 = getInt();
            System.out.println("Confirm your new password");
            int pass2 = getInt();
            while (pass2 != pass1) {
                System.out.println("Passwords don't match. \nConfirm your new password");
                pass2 = getInt();
            }
            password[found] = pass2;
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter("employee.txt"));
            bw.write(numberOfEmployees+""); bw.newLine();
            for (int i = 0; i < numberOfEmployees; i++) {
                bw.write(number[i]+""); bw.newLine();
                bw.write(password[i]+""); bw.newLine();
                bw.write(first[i]); bw.newLine();
                bw.write(last[i]); bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static boolean listReservationsDate(int day) {
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
        for (int i = 0; i < numberOfReservations; i++) {
            if (reservationDays[i] == day) {
                System.out.println("Name " + firstNames[i] + " " + lastNames[i] + "\nDate: " + reservationDays[i] + "\nRoom " + reservationRooms[i] + "\nEmployee " + employeeNumbers[i]);
            }
        }
        return true;
    }
    public static void admin() {
        Scanner sc = new Scanner(System.in);
        boolean logOut = false;
        System.out.println("Welcome to the admin account");
        

        String firstName, lastName;
        int number, password;
        while (!logOut) {
            System.out.println("Enter an instruction: \n1. Create Employee \n2. Delete Employee \n3. Create Room \n4. Delete Room \n5. Log Out");
            int op = getInt();
            switch (op) {
                case 1:
                    System.out.println("Enter first and last name");
                    firstName = sc.nextLine();
                    lastName = sc.nextLine();
                    System.out.println("Enter Employee Number");
                    number = getInt();
                    System.out.println("Enter password");
                    password = getInt();
                    createEmployee(firstName, lastName, number, password);
                    break;
                case 2:
                    
                    break;
                case 3:
                    System.out.println("Enter a room number you want to create");
                    number = getInt();
                    createRoom(number);
                    break;
                case 4:
                    System.out.println("Enter a room you want to remove");
                    number = getInt();
                    deleteRoom(number);
                    break;
                case 5: 
                    logOut = true;
                    break;
            }
        }
    }
    public static boolean deleteRoom(int number) {
        return false;
    }
    public static boolean createRoom(int number) {
        int numberOfRooms, rooms[];
        try {
            BufferedReader br = new BufferedReader(new FileReader("rooms.txt"));
            numberOfRooms = Integer.parseInt(br.readLine());
            rooms = new int[numberOfRooms+1];
            for (int i = 0; i < numberOfRooms; i++) {
                rooms[i] = Integer.parseInt(br.readLine());
                if (rooms[i] == number) {
                    System.out.println("A room with that number already exists");
                    return false;
                }
            }
            br.close();
        } catch (IOException e) {
            return false;
        }
        rooms[numberOfRooms] = number;
        if (!fillRoomFile(numberOfRooms+1, rooms)) return false;
        return true;
    }
    public static boolean fillRoomFile(int numberOfRooms, int[] rooms) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("rooms.txt", false));
            bw.write(numberOfRooms+""); bw.newLine();
            for (int i = 0; i < numberOfRooms; i++) {
                bw.write(rooms[i] +""); bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    public static boolean createEmployee(String first, String last, int number, int password) {
        return false;
        //implement later
        //remember to check if the employee with this number already exists
    }
    public static boolean fillEmployeeFile(int numberOfEmployees, int[] numbers, int[] passwords, String[] first, String[] last) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("employee.txt", false));
            bw.write(numberOfEmployees+""); bw.newLine();
            for (int i = 0; i < numberOfEmployees; i++) {
                bw.write(numbers[i]+"");bw.newLine();
                bw.write(passwords[i]+""); bw.newLine();
                bw.write(first[i]); bw.newLine();
                bw.write(last[i]); bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
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
            n = sc.nextLine();
        }
    }

}
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
    public static boolean makeReservations() {
        int numberOfReservations, day, room, firstNames[], lastNames[], reservationDays[], reservationRooms[];
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
            }
            br.close();
        } catch (IOException e) {
            return false;
        }


        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("reservations.txt", false));
            br.write(++numberOfReservations); br.newLine();
            for (int i = 0; i < numberOfReservations; i++) {
                br.write(firstNames[i]); br.newLine();
                br.write(lastNames[i]); br.newLine();
                br.write(reservationDays[i]); br.newLine();
                br.write(reservationRooms[i]); br.newLine();
            }
            br.close();
        } catch (IOException e) {
            return false;
        }
        return true;   
    }
    public static boolean cancelReservations() {
        int numberOfReservations, day, room, currentReservationDays[], currentReservationRooms[];
        System.out.println("Enter a day to cancel/change (int)");
        day = getInt();
        System.out.println("Enter a room to cancel/change (int)");
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
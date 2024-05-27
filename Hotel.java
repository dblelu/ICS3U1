import java.io.*;
import java.util.Scanner; 

/*==========================================================
| Hotel.java                                               |
|----------------------------------------------------------|
| Programmers:  Mohan Chen & Yanning Wang                  |
| Last modified:  May 26th, 2024                           |
|----------------------------------------------------------|
| The program is a hotel reservation booking system.       | 
==========================================================*/
public class Hotel 
{
    public static void main(String[] args) 
    {
        while (true) 
        {
            System.out.println("Enter a employee number to log in: ");
            
            int number = getInt();
            verify(number);
        }
    }
    
   /*====================================================================
   |  void verify(int num)                                              |
   |--------------------------------------------------------------------|
   |  int num - empolyee number                                         |
   |--------------------------------------------------------------------|
   |  This method is to verify if the employee number exist.            |
   ====================================================================*/
    public static void verify(int num) 
    {
        System.out.println("Please Enter the password");
        int pw = getInt();
        if (num == 0 && pw == 1234) 
        {

            admin();
            return;
        }
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("employee.txt"));
            
            int numberOfEmployees = Integer.parseInt(br.readLine());
            boolean found = false;
            int number = -1;
            int password = -1;
            String first = "";
            String last = "";
            
            for (int i = 0; i < numberOfEmployees && !found; i++) 
            {
                number = Integer.parseInt(br.readLine());
                password = Integer.parseInt(br.readLine());
                first = br.readLine();
                last = br.readLine();
                if (number == num) found = true;
            }
            br.close();
            
            if (!found) 
            {
                System.out.println("Employee doesn't exist");
                return;
            }
            System.out.println("Enter your password for " + number + " or 0 to go back");
            int input = getInt();
            int cnt = 0;
            
            while (password != input) 
            {
                if (cnt >= 3) 
                {
                    System.out.println("You entered the incorrect password too many times");
                    return;
                }
                if (input == 0) return;
                cnt++; 
                System.out.println("Enter your password for " + number + "or 0 to go back");
                input = getInt(); 
            }
            
            employee(number, first, last);
        } 
        catch (IOException e) 
        {
            System.out.println("Error" + e);
        }
    }
    
   /*====================================================================
   |  void employee(int number, String empFirstName, String empLastName)|
   |--------------------------------------------------------------------|
   |  int number - employee number                                      |
   |  String empFirstName - employee's first name                       |
   |  String empLastName -  employee's last name                        |
   |--------------------------------------------------------------------|
   |  This method is a welcome message that also ask the user type their| 
   |  name to decide the options they want to choose.                   |
   ====================================================================*/
    public static void employee(int number, String empFirstName, String empLastName) 
    {
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Welcome Employee: " + number);
        System.out.println("First Name: " + empFirstName + "\nLast Name: " + empLastName + "\n\n");

        Scanner sc = new Scanner(System.in);
        
        boolean goBack = false;
        String first, last;
        int day;
        
        while (!goBack) 
        {
            System.out.println("Enter instructions:\n1. Make Reservations\n2. Cancel Reservations\n3. Change Reservations\n4. Change Pin\n5. List all rooms for date\n6. List all reservations for date\n7. Look up reservations for given name\n8. Log Out");
            int input = getInt();
            switch (input) 
            {
                case 1:
                    System.out.println("Enter the first name and last name");
                    first = sc.nextLine();
                    last = sc.nextLine();
                    System.out.println("Enter a date");
                    day = getInt();
                    if (!makeReservations(number, first, last, day)) System.out.println("Error with reservation!");
                    else System.out.println("Sucessfully created reservation for " + first + " " + last + " on " + day);
                    break;
                    
                case 2:
                    System.out.println("Enter the first name and last name");
                    first = sc.nextLine();
                    last = sc.nextLine();
                    if (cancelReservations(first, last)) System.out.println("Erro cnaceling reservation!");
                    else System.out.println("Succesfully cancelled the reservation");
                    break;
                    
                case 3:
                    System.out.println("Enter the first name and last name");
                    first = sc.nextLine();
                    last = sc.nextLine();
                    changeReservations(first, last);    
                    break;
                    
                case 4:
                    if (changePin(number)) System.out.println("Sucessfully changed the pin");
                    else System.out.println("Error changing the pin");
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
    
   /*====================================================================
   |  boolean fillReservationFile(int numberOfReservations, String[]    |
   |  firstNames, String[] lastNames, int[] reservationDays, int[]      |
   |  reservationRooms, int[] employeeNumbers, int skip)                |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the file was successfully written,      |
   |  false otherwise                                                   |
   |--------------------------------------------------------------------|
   |  int numberOfReservations, String[]                                |
   |  firstNames, String[] lastNames, int[] reservationDays, int[]      |
   |  reservationRooms, int[] employeeNumbers, int skip                 |
   |--------------------------------------------------------------------|
   |  This method writes the reservations to a file, with an option to  |
   |  skip a specific reservation.                                      |
   ====================================================================*/
    public static boolean fillReservationFile(int numberOfReservations, String[] firstNames, String[] lastNames, int[] reservationDays, int[] reservationRooms, int[] employeeNumbers, int skip) 
    {
        try 
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter("reservations.txt", false));
            
            if (skip == -1) 
            {
                bw.write(numberOfReservations+""); 
            } 
            else 
            {
                bw.write(numberOfReservations-1+"");
            } 
            bw.newLine();
            System.out.println(numberOfReservations);
           
            for (int i = 0; i < numberOfReservations; i++) 
            {
                if (i != skip) 
                {
                    bw.write(firstNames[i]); bw.newLine();
                    bw.write(lastNames[i]); bw.newLine();
                    bw.write(reservationDays[i]+""); bw.newLine();
                    bw.write(reservationRooms[i]+""); bw.newLine();
                    bw.write(employeeNumbers[i]+""); bw.newLine();
                }
            }
            bw.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        return true;   
    }
    
   /*====================================================================
   |  boolean makeReservations(int number, String first, String last,   |
   |  int day)                                                          |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the reservation was successfully made,  |
   |  false otherwise.                                                  |
   |--------------------------------------------------------------------|
   |  int number - number the employee number                           |
   |  String first - the first name of the employee                     |
   |  String last - the last name of the employee                       |
   |  int day -  the reservation day                                    |
   |--------------------------------------------------------------------|
   |  This method makes a new reservation and adds it to the reservation| 
   |  file.                                                             |
   ====================================================================*/
    public static boolean makeReservations(int number, String first, String last, int day) 
    {
        //Get reservation info
        String firstNames[], lastNames[];
        int numberOfReservations,  reservationDays[], reservationRooms[], employeeNumbers[];
       
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations+1];
            lastNames = new String[numberOfReservations+1];
            reservationDays = new int[numberOfReservations+1];
            reservationRooms = new int[numberOfReservations+1];
            employeeNumbers = new int[numberOfReservations+1];
            
            for (int i = 0; i < numberOfReservations; i++) 
            {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        
        int[] rooms = getAvailableRooms(reservationDays, reservationRooms, day);
        System.out.println("Here is a list of available rooms");
        
        for (int i = 0; i < rooms.length; i++) 
        {
            if (rooms[i] != -1) System.out.println(i + 1 + ". Room: " + rooms[i]);
        }
        
        System.out.println("Enter the room you want (int) ");
        int room = getInt()-1;
        boolean valid = false;
        
        while (!valid) 
        {
            if (available(reservationDays, reservationRooms, day, rooms[room])) 
            {
                valid = true;
            } 
            else 
            {
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
    
   /*====================================================================
   |  int[] getAvailableRooms(int[] reservationDays, int[]              |
   |  reservationRooms, int day)                                        |
   |--------------------------------------------------------------------|
   |  returns int[] - an array of available rooms.                      |              
   |--------------------------------------------------------------------|
   |  int[] reservationDays - array of reservation days                 |
   |  int[] reservationRooms - array of reserved rooms                  | 
   |  int day - to check for available rooms                            |
   |--------------------------------------------------------------------|
   |  This method retrieves the list of available rooms for a given day.|
   ====================================================================*/
    public static int[] getAvailableRooms(int[] reservationDays, int[] reservationRooms, int day) 
    {
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader(new File("rooms.txt")));
            
            int numRooms = Integer.parseInt(br.readLine());
            int[] ret = new int[numRooms];
            
            for (int i = 0; i < numRooms; i++) 
            {
                int room = Integer.parseInt(br.readLine());
                if (available(reservationDays, reservationRooms, day, room)) 
                {
                    ret[i] = room;
                } 
                else 
                {
                    ret[i] = -1;
                }
            }
            br.close();
            return ret;
        } 
        catch (IOException e) 
        {
            System.out.println("Error" + e);
        }
        return null;
    }
    
   /*====================================================================
   |  boolean available(int[] reservationDays, int[] reservationRooms,  |
   |  int day, int room)                                                |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the room is available, false otherwise. |                   
   |--------------------------------------------------------------------|
   |  int[] reservationDays - array of reservation days                 |
   |  int[] reservationRooms - array of reservation days                |
   |  int day - day to check                                            |
   |  int room - room to check                                          |
   |--------------------------------------------------------------------|
   |  This method checks if a room is available on a given day.         |
   ====================================================================*/
    public static boolean available(int[] reservationDays, int[] reservationRooms, int day, int room) 
    {
        for (int i = 0; i < reservationDays.length; i++) 
        {
            if (day == reservationDays[i] && room == reservationRooms[i]) return false;
        }
        return true;
    }
    
   /*====================================================================
   |  boolean listAvailableRooms(int day)                               |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the operation was successful,           |
   |  false otherwise.                                                  |
   |--------------------------------------------------------------------|
   |  int day - the day to check for available rooms.                   |
   |--------------------------------------------------------------------|
   |  This method lists all available rooms for a given day.            |
   ====================================================================*/
    public static boolean listAvailableRooms(int day) 
    {
        String firstNames[], lastNames[];
        int numberOfReservations,  reservationDays[], reservationRooms[], employeeNumbers[];
       
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
            
            for (int i = 0; i < numberOfReservations; i++) 
            {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        
        int[] rooms = getAvailableRooms(reservationDays, reservationRooms, day);
        System.out.println("Available Rooms: ");
        
        for (int i = 0; i < rooms.length; i++) 
        {
            if (rooms[i] != -1) System.out.println("Room " + rooms[i]);
        }
        return true;
    }
    
   /*====================================================================
   |  boolean cancelReservations(String first, String last)             |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the reservation was successfully        |
   |  cancelled, false otherwise.                                       |
   |--------------------------------------------------------------------|
   |  String first, String Last - the name of reservation holder.       |
   |--------------------------------------------------------------------|
   |  This method cancels a reservation by name.                        |
   ====================================================================*/
    public static boolean cancelReservations(String first, String last) 
    {
        String firstNames[], lastNames[]; 
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
            
            for (int i = 0; i < numberOfReservations; i++) 
            {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        
        boolean[] valid = getReservationsByName(first, last);
        int[] reserv = new int[valid.length + 1];
        System.out.println("Here are your reservations: ");
        
        for (int i = 0, cnt = 1; i < valid.length; i++) 
        {
            if (valid[i]) 
            {
                System.out.println(i+1+".\n Date " + reservationDays[i] + "\nRoom " + reservationRooms[i]);
                reserv[cnt++] = i;
            }
        }
        System.out.println("Enter a reservation to cancel or 0 to abort");
        
        int cancel = getInt()-1;
        if (cancel == 0) return false;
        
        while (!valid[reserv[cancel]]) 
        {
            System.out.println("That reservation doesn't exist");
            System.out.println("Enter a reservation to cancel");
            cancel = getInt()-1;
        }
        fillReservationFile(numberOfReservations, firstNames, lastNames, reservationDays, reservationRooms, employeeNumbers, reserv[cancel]); 
        return true;
    } 
    
   /*====================================================================
   |  boolean[] getReservationsByName(String first, String last)        |
   |--------------------------------------------------------------------|
   |  returns boolean - a boolean array where each element is true if   |
   |  the corresponding reservation matches the given names.            |       
   |--------------------------------------------------------------------|
   |  String first, String last - the name to search for.               |
   |--------------------------------------------------------------------|
   |  This method reads reservation data from a file and returns an     |
   |  array of booleans indicating whether each reservation matches the | 
   |  given first and last names.                                       |
   ====================================================================*/
    public static boolean[] getReservationsByName(String first, String last) 
    {
        String firstNames[], lastNames[]; 
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
       
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
            boolean[] ret = new boolean[numberOfReservations];
            
            for (int i = 0; i < numberOfReservations; i++) 
            {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
                ret[i] = (firstNames[i].equals(first) && lastNames[i].equals(last));
            }
            br.close();
            return ret;
        } 
        catch (IOException e) 
        {
            System.out.println("ERROR!" + e);
        }
        return null;
    }
    
   /*====================================================================
   |  boolean listReservationsName()                                    |
   |--------------------------------------------------------------------|
   |  returns boolean - true if reservations were successfully listed,  |
   |  false if an error occurred.                                       |
   |--------------------------------------------------------------------|
   |  This method prompts the user to enter a first and last name,      |
   |  then lists all reservations that match the given names.           |
   ====================================================================*/
    public static boolean listReservationsName() 
    {
        Scanner sc = new Scanner(System.in);
        String firstNames[], lastNames[];
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
            
            for (int i = 0; i < numberOfReservations; i++) 
            {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } 
        catch (IOException e) 
        {
            System.out.println("Error" + e);
            return false;
        }
        System.out.println("Enter first name and last name");
        String first = sc.nextLine();
        String last = sc.nextLine();

        boolean[] valid = getReservationsByName(first, last);
        System.out.println("Here are your reservations: ");
        
        for (int i = 0, cnt = 1; i < valid.length; i++) 
        {
            if (valid[i]) 
            {
                System.out.println(cnt + ".\nName: " + first + " " + last + "\nDate: " + reservationDays[i] + "\nRoom: " + reservationRooms[i]);
                cnt++;
            }
        }
        return true;
    }
    
   /*====================================================================
   |  boolean changeReservations(String first, String last)             |
   |--------------------------------------------------------------------|
   |  returns boolean - True if the reservation was successfully changed| 
   |  false if an error occurred.                                       |
   |--------------------------------------------------------------------|
   |  String first, String last - the name associated with the          |
   |  reservation to change.                                            |
   |--------------------------------------------------------------------|
   |  This method prompts the user to select and change a reservation   |
   |  by entering the first and last name associated with the reservation| 
   ====================================================================*/
    public static boolean changeReservations(String first, String last) 
    {
        Scanner sc = new Scanner(System.in);
        String firstNames[], lastNames[];
        int numberOfReservations, reservationDays[], reservationRooms[], employeeNumbers[];
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations];
            lastNames = new String[numberOfReservations];
            reservationDays = new int[numberOfReservations];
            reservationRooms = new int[numberOfReservations];
            employeeNumbers = new int[numberOfReservations];
            
            for (int i = 0; i < numberOfReservations; i++) 
            {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } 
        catch (IOException e) 
        {
            System.out.println("Error" + e);
            return false;
        }
        
        boolean[] valid = getReservationsByName(first, last);
        int[] reserv = new int[valid.length + 1];
        System.out.println("Here are your reservations: ");
        
        for (int i = 0, cnt = 1; i < valid.length; i++) 
        {
            if (valid[i]) 
            {
                System.out.println(i+1+".\n Date " + reservationDays[i] + "\nRoom " + reservationRooms[i]);
                reserv[cnt++] = i;
            }
        }
        
        System.out.println("Enter a reservation to change or 0 to abort");
        int idx = getInt();
        if (idx == 0) return false;
        
        while (!valid[reserv[idx]]) 
        {
            System.out.println("That reservation doesn't exist");
            System.out.println("Enter a reservation to change or 0 to abort");
            idx = getInt();
        }
        
        System.out.println("What do you want to change: ");
        System.out.println("1. Name \n2. Date\n3. Room. \n4. Continue");
        int op = getInt();
        while (op != 4) 
        {
            switch (op) 
            {
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
    
   /*====================================================================
   |  boolean changePin(int num)                                        |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the PIN was successfully changed, false |
   |  if an error occurred or the employee does not exist.              |
   |--------------------------------------------------------------------|
   |  int num - the employee number whose PIN is to be changed.         |
   |--------------------------------------------------------------------|
   |  This method prompts the user to change the PIN of an employee     |
   |  given the employee's number.                                      |
   ====================================================================*/
    public static boolean changePin(int num) 
    {
        String first[], last[];
        int numberOfEmployees, number[], password[];
       
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("employee.txt"));
            
            numberOfEmployees = Integer.parseInt(br.readLine());
            int found = -1;
            number = new int[numberOfEmployees];
            password = new int[numberOfEmployees];
            first = new String[numberOfEmployees];
            last = new String[numberOfEmployees];
            
            for (int i = 0; i < numberOfEmployees && found == -1; i++) 
            {
                number[i] = Integer.parseInt(br.readLine());
                password[i] = Integer.parseInt(br.readLine());
                first[i] = br.readLine();
                last[i] = br.readLine();
                if (number[i] == num) found = i;
            }
            br.close();
            
            if (found == -1) 
            {
                System.out.println("Employee doesn't exist");
                return false;
            }
            
            System.out.println("Enter your new password");
            int pass1 = getInt();
            System.out.println("Confirm your new password");
            int pass2 = getInt();
           
            while (pass2 != pass1) 
            {
                System.out.println("Passwords don't match. \nConfirm your new password");
                pass2 = getInt();
            }
            password[found] = pass2;
            br.close();
            
            BufferedWriter bw = new BufferedWriter(new FileWriter("employee.txt"));
            bw.write(numberOfEmployees+""); bw.newLine();
            
            for (int i = 0; i < numberOfEmployees; i++) 
            {
                bw.write(number[i]+""); bw.newLine();
                bw.write(password[i]+""); bw.newLine();
                bw.write(first[i]); bw.newLine();
                bw.write(last[i]); bw.newLine();
            }
            bw.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        return true;
    }
    
   /*====================================================================
   |  boolean listReservationsDate(int day)                             |
   |--------------------------------------------------------------------|
   |  returns boolean - true if reservations are successfully listed,   |
   |  false if an error occurs.                                         |
   |--------------------------------------------------------------------|
   |  int day - the day to filter reservations by.                      |
   |--------------------------------------------------------------------|
   |  This method lists reservations for a specific day.                |
   ====================================================================*/
    public static boolean listReservationsDate(int day) 
    {
        String firstNames[], lastNames[];
        int numberOfReservations,  reservationDays[], reservationRooms[], employeeNumbers[];
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("reservations.txt"));
            
            numberOfReservations = Integer.parseInt(br.readLine());
            firstNames = new String[numberOfReservations+1];
            lastNames = new String[numberOfReservations+1];
            reservationDays = new int[numberOfReservations+1];
            reservationRooms = new int[numberOfReservations+1];
            employeeNumbers = new int[numberOfReservations+1];
            
            for (int i = 0; i < numberOfReservations; i++) 
            {
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                reservationDays[i] = Integer.parseInt(br.readLine());
                reservationRooms[i] = Integer.parseInt(br.readLine());
                employeeNumbers[i] = Integer.parseInt(br.readLine());
            }
            br.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        for (int i = 0; i < numberOfReservations; i++) 
        {
            if (reservationDays[i] == day) 
            {
                System.out.println("Name " + firstNames[i] + " " + lastNames[i] + "\nDate: " + reservationDays[i] + "\nRoom " + reservationRooms[i] + "\nEmployee " + employeeNumbers[i]);
            }
        }
        return true;
    }
    
   /*====================================================================
   |  void admin()                                                      |
   |--------------------------------------------------------------------|
   |  This method shows a welcome message of admin interface for        |
   |  managing employees and rooms.                                     |
   ====================================================================*/
    public static void admin() 
    {
        Scanner sc = new Scanner(System.in);

        //verify password for admin

        boolean logOut = false;
        System.out.println("\n\n\n\n\n\n\n\n\n\nWelcome to the admin account\n\n");
        
        String firstName, lastName;
        int number, password;
        
        while (!logOut) 
        {
            System.out.println("Enter an instruction: \n1. Create Employee \n2. Delete Employee \n3. Create Room \n4. Delete Room \n5. Log Out");
            int op = getInt();
            
            switch (op) 
            {
                case 1:
                    System.out.println("Enter first and last name");
                    firstName = sc.nextLine();
                    lastName = sc.nextLine();
                    System.out.println("Enter Employee Number");
                    number = getInt();
                    System.out.println("Enter password");
                    password = getInt();
                    if (createEmployee(firstName, lastName, number, password)) System.out.println("Sucessfully created employee with number: " + number);
                    else System.out.println("Error creating employee with number " + number);
                    break;
                    
                case 2:
                    System.out.println("Enter the employee you want to remove by number");
                    number = getInt();
                    if (deleteEmployee(number)) System.out.println("Sucessfully deleted employee " + number);
                    else System.out.println("Error deleting employee " + number);
                    break;
                    
                case 3:
                    System.out.println("Enter a room number you want to create");
                    number = getInt();
                    if (createRoom(number)) System.out.println("Sucessfully created room " + number);
                    else System.out.println("Error creating room " + number);
                    break;
                    
                case 4:
                    System.out.println("Enter a room you want to remove");
                    number = getInt();
                    if (deleteRoom(number)) System.out.println("Sucessfully deleted room " + number);
                    else System.out.println("Error deleting room " + number);
                    break;
                    
                case 5: 
                    System.out.println("Sucessfully Logged Out");
                    logOut = true;
                    break;
            }
        }
    }
    
   /*====================================================================
   |  boolean deleteRoom(int number)                                    |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the room is successfully deleted,       |
   |  false otherwise.                                                  |
   |--------------------------------------------------------------------|
   |  int number - the number of the room to delete.                    |
   |--------------------------------------------------------------------|
   |  This method deletes a room given its number.                      |
   ====================================================================*/
    public static boolean deleteRoom(int number) 
    {
        int numberOfRooms, rooms[];
        int valid = -1;
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("rooms.txt"));
            numberOfRooms = Integer.parseInt(br.readLine());
            rooms = new int[numberOfRooms+1];
           
            for (int i = 0; i < numberOfRooms; i++) 
            {
                rooms[i] = Integer.parseInt(br.readLine());
                if (rooms[i] == number) 
                {
                    valid = i;
                }
            }
            br.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
       
        if (valid == -1) 
        {
            System.out.println("A room with that number does not exist");
            return false;
        }
        if (!fillRoomFile(numberOfRooms, rooms, valid)) return false;
        return true;
    }
    
   /*====================================================================
   |  boolean createRoom(int number)                                    |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the room is successfully created,       |
   |  false otherwise.                                                  |
   |--------------------------------------------------------------------|
   |  int number - the number of the room to create.                    |
   |--------------------------------------------------------------------|
   |  This method creates a new room with the given number.             |
   ====================================================================*/
    public static boolean createRoom(int number) 
    {
        int numberOfRooms, rooms[];
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("rooms.txt"));
            numberOfRooms = Integer.parseInt(br.readLine());
            rooms = new int[numberOfRooms+1];
            
            for (int i = 0; i < numberOfRooms; i++) 
            {
                rooms[i] = Integer.parseInt(br.readLine());
                
                if (rooms[i] == number) 
                {
                    System.out.println("A room with that number already exists");
                    br.close();
                    return false;
                }
            }
            br.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        rooms[numberOfRooms] = number;
        if (!fillRoomFile(numberOfRooms+1, rooms, -1)) return false;
        return true;
    }
    
   /*====================================================================
   |  boolean fillRoomFile(int numberOfRooms, int[] rooms, int skip)    |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the file is successfully written,       |
   |  false otherwise.                                                  |
   |--------------------------------------------------------------------|
   |  int numberOfRooms - the total number of rooms                     |
   |  int[] rooms - the array of room numbers                           |
   |  int skip - the index of the room to skip (for deletion), or -1 if |
   |  none                                                              |
   |--------------------------------------------------------------------|
   |  This method writes the room data to the rooms.txt file.           |
   ====================================================================*/
    public static boolean fillRoomFile(int numberOfRooms, int[] rooms, int skip) 
    {
        try 
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter("rooms.txt", false));
            if (skip == -1) bw.write(numberOfRooms+""); 
            else bw.write(numberOfRooms-1+"");
            bw.newLine();
            
            for (int i = 0; i < numberOfRooms; i++) 
            {
                if (skip != i) 
                {
                    bw.write(rooms[i] +""); bw.newLine();
                }
            }
            bw.close();
            
        } 
        catch (IOException e) 
        {
            return false;
        }
        return true;
    }
    
   /*====================================================================
   |  boolean createEmployee(String first, String last, int number,     |
   |  int password)                                                     |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the employee is successfully created,   |
   |  false otherwise.                                                  |
   |--------------------------------------------------------------------|
   |  String first - the first name of the employee                     |
   |  String last - the last name of the employee                       |
   |  int number - the employee number                                  |
   |  int password - the employee password                              |
   |--------------------------------------------------------------------|
   |  This method creates a new employee with the given details.        |
   ====================================================================*/
    public static boolean createEmployee(String first, String last, int number, int password) 
    {
        int numberOfEmployees, nums[], pass[];
        String firstNames[], lastNames[];
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("employee.txt"));
            
            numberOfEmployees = Integer.parseInt(br.readLine());
            nums = new int[numberOfEmployees+1];
            pass = new int[numberOfEmployees+1];
            firstNames = new String[numberOfEmployees+1];
            lastNames = new String[numberOfEmployees+1];
           
            for (int i = 0; i < numberOfEmployees; i++) 
            {
                nums[i] = Integer.parseInt(br.readLine());
                pass[i] = Integer.parseInt(br.readLine());
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                
                if (nums[i] == number) 
                {
                    System.out.println("A employee with that number already exists");
                    br.close();
                    return false;
                }
            }
            br.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        
        nums[numberOfEmployees] = number;
        pass[numberOfEmployees] = password;
        firstNames[numberOfEmployees] = first;
        lastNames[numberOfEmployees] = last;
        if (!fillEmployeeFile(numberOfEmployees+1, nums, pass, firstNames, lastNames, -1)) return false;
        return true;
    }
    
   /*====================================================================
   |  boolean deleteEmployee(int number)                                |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the employee is successfully deleted,   |
   |  false otherwise.                                                  |
   |--------------------------------------------------------------------|
   |  int number - the number of the employee to delete                 |
   |--------------------------------------------------------------------|
   |  This method deletes an employee given their number.               |
   ====================================================================*/
    public static boolean deleteEmployee(int number) 
    {
        int numberOfEmployees, nums[], pass[], valid = -1;
        String firstNames[], lastNames[];
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("employee.txt"));
            
            numberOfEmployees = Integer.parseInt(br.readLine());
            nums = new int[numberOfEmployees+1];
            pass = new int[numberOfEmployees+1];
            firstNames = new String[numberOfEmployees+1];
            lastNames = new String[numberOfEmployees+1];
            
            for (int i = 0; i < numberOfEmployees; i++) 
            {
                nums[i] = Integer.parseInt(br.readLine());
                pass[i] = Integer.parseInt(br.readLine());
                firstNames[i] = br.readLine();
                lastNames[i] = br.readLine();
                
                if (nums[i] == number) 
                {
                    valid = i;
                }
            }
            br.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        
        if (valid == -1) 
        {
            System.out.println("A employee with that number does not exist");
            return false;
        }
        if (!fillEmployeeFile(numberOfEmployees, nums, pass, firstNames, lastNames, valid)) return false;
        return true;
    }
    
   /*====================================================================
   |  boolean fillEmployeeFile(int numberOfEmployees, int[] numbers,    |
   |  int[] passwords, String[] first, String[] last, int skip)         |
   |--------------------------------------------------------------------|
   |  returns boolean - true if the file is successfully written,       |
   |  false otherwise.                                                  |
   |--------------------------------------------------------------------|
   |  int numberOfEmployees - the total number of employees             |
   |  int[] numbers - the array of employee numbers                     |
   |  int[] passwords - the array of employee passwords                 |
   |  String[] first - the array of employee first names                |
   |  String[] last - the array of employee last names                  |
   |  int skip - skip the index of the employee to skip (for deletion), |
   |  or -1 if none                                                     |
   |--------------------------------------------------------------------|
   |  This method writes the employee data to the employee.txt file.    |
   ====================================================================*/
    public static boolean fillEmployeeFile(int numberOfEmployees, int[] numbers, int[] passwords, String[] first, String[] last, int skip) 
    {
        
        try 
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter("employee.txt", false));
            
            if (skip == -1) bw.write(numberOfEmployees+"");
            else bw.write(numberOfEmployees-1+""); 
            bw.newLine();
            
            for (int i = 0; i < numberOfEmployees; i++) 
            {
                if (i != skip) 
                {
                    bw.write(numbers[i]+"");bw.newLine();
                    bw.write(passwords[i]+""); bw.newLine();
                    bw.write(first[i]); bw.newLine();
                    bw.write(last[i]); bw.newLine();
                }
            }
            bw.close();
        } 
        catch (IOException e) 
        {
            return false;
        }
        return true;
    }
    
   /*====================================================================
   |  int getInt()                                                      |
   |--------------------------------------------------------------------|
   |  returns int - the valid integer input from the user.              |
   |--------------------------------------------------------------------|
   |  This method takes in an int as input, not allowing the user to    |
   |  break the system with strings.                                    |
   ====================================================================*/ 
    public static int getInt() 
    {
        Scanner sc = new Scanner(System.in);
        String n = sc.nextLine();
        
        while (true) 
        {
            try 
            {
                if (Integer.parseInt(n) < 0) throw new NumberFormatException();
                return Integer.parseInt(n);
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Invalid input. \nTry again: ");
            }
            n = sc.nextLine();
        }
    }
    
}
import java.util.*;
import java.io.*;
/*==========================================================
| WriteChars.java                                          |
|----------------------------------------------------------|
| Programmer:  Mohan Chen                                  |
| Last modified:  May 07th, 2024                           |
|----------------------------------------------------------|
| The program continues to ask the user to enter a String, |
| until “stop” is entered.                                 | 
==========================================================*/
public class WriteChars 
{
    public static void main(String[] args) 
    {
        // create scanner
        Scanner sc = new Scanner(System.in);
        String input;
        final String FILE_NAME = "writeChars.txt";
        System.out.println("Please enter a string you want to write into the file and enter stop when you want to end. ");
        input = sc.nextLine();
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter (FILE_NAME, false));
            while (!input.equals("stop")) 
            {
                for (int i = 0; i < input.length(); i++)
                {
                    bw.write(input.charAt(i));
                    bw.newLine(); 
                }
                bw.newLine();
                System.out.println("Please enter a string you want to write into the file and enter stop when you want to end. ");
                input = sc.nextLine();
            }
            bw.close();
        }
        catch (IOException e)
        {
            System.out.println(e + "Problem reading" + FILE_NAME);
        }
    }
}
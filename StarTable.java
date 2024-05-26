import java.util.*;
import java.io.*;
/*==========================================================
| StaarTable.java                                          |
|----------------------------------------------------------|
| Programmer:  Mohan Chen                                  |
| Last modified:  May 07th, 2024                           |
|----------------------------------------------------------|
| The program users for the dimensions of a table. Make it |
| into a startable into file "star.txt"                    | 
==========================================================*/
public class StarTable 
{
    public static void main(String[] args) 
    {
        // create Scanners.
        Scanner sc = new Scanner(System.in);

        //create variables.
        int r;
        int c;
        char a[][];
        final String FILE_NAME = "star.txt";
        final char C = '*';
        System.out.println("Please give me the rows you want the table to be:");
        r = sc.nextInt();
        System.out.println("Please give me the columns you want the table to be:");
        c = sc.nextInt();
        a = new char[r][c];
        
        //inter value for arary. 
        for (int i = 0; i < r; i++) 
        {
            for (int j = 0; j < c; j++) 
            {
                a[i][j] = 'C';
            }
        }

        // wrtie the startable in the file. 
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, false));
            for (int i = 0; i < r; i++)
            {
                for (int j = 0; j < c; j ++)
                {
                    bw.write(a[i][j]);
                }
                bw.newLine();
            }
            bw.close();
        }
        catch(IOException e)
        {
            System.out.println(e + "Problem writing" + FILE_NAME);
        }
    }
}

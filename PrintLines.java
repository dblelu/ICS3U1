import java.io.*;

public class PrintLines {
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("line.txt"));
            //use the buffered reader to read from line.txt till you reach EOF
            String line = br.readLine();
            while(line != null){
                System.out.println(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e + "  Problem reading " + "line.txt");
        }
  
    }
}
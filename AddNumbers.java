import java.io.*;

public class AddNumbers {
    public static void main(String[] args) {
        final String FILE_NAME = "numbers.txt";
        int sum = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line = br.readLine();
            while (line != null) {
                try {
                    int num = Integer.parseInt(line);
                    sum += num;
                } catch (NumberFormatException e) {
                    System.out.println(line + " is not a number!");
                }
                line = br.readLine();
            }
            br.close();
            System.out.println(sum);
        } catch (IOException e) {

        }
  
    }
}
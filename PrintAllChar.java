import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PrintAllChar {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("allChar.txt"));  
            int c = br.read();
            while(c != -1) {
                if (!(c == ' ' || c == '\n' || c == '\r')) {
                    System.out.println((char) c);
                }
                c = br.read();
            }
            br.close();
        } catch (IOException e) {

        }
    }
}

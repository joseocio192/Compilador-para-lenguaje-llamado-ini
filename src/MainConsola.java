import java.io.*;

public class MainConsola {
    public static void main(String[] args) {
        File file = new File("src/ejemplo.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String st;
            StringBuilder source = new StringBuilder(); // Use StringBuilder instead of String
            while ((st = br.readLine()) != null) {
                source.append(st).append("\n"); // Append to StringBuilder
            }
            Scanner scanner = new Scanner();
            Token[] tokens = scanner.scanear(source.toString()); // Convert StringBuilder to String
            for (int i = 0; i < tokens.length; i++) {
                System.out.println(tokens[i].getValor() + " " + tokens[i].getTipo().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
}


/**
 * Main
 */
import javax.swing.*;
import java.awt.event.*;
 public class Main extends JFrame implements ActionListener {

    JTextArea programaText;
    JTable tabla;

    public Main() {
        JPanel contentPane = new JPanel();
        add(contentPane);
        contentPane.setLayout(null);

        JLabel label = new JLabel("program");
        label.setBounds(60, 54, 100, 20);
        contentPane.add(label);
                
        programaText = new JTextArea();
        programaText.setBounds(60, 80, 200, 250);
        contentPane.add(programaText);
        programaText.transferFocus();

        JButton button = new JButton("Scanner");
        button.setBounds(400, 52, 100, 20);
        contentPane.add(button);
        button.addActionListener(this);

        tabla = new JTable();
        tabla.setBounds(386, 80, 200, 250);
        contentPane.add(tabla);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Compilador");
        setSize(1000, 800);
        setVisible(true);

        
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Main::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Scanner scanner = new Scanner();
        Token[] tokens = scanner.scanear(programaText.getText());
        for (Token token : tokens) {
            System.out.println(token);
        }

        String[] columnas = {"Tipo", "Valor"};
        String[][] datos = new String[tokens.length][2];
        for (int i = 0; i < tokens.length; i++) {
            datos[i][0] = tokens[i].getTipo().toString();
            datos[i][1] = tokens[i].getValor();
        }
        tabla = new JTable(datos, columnas);
        tabla.setBounds(386, 80, 200, 250);
        add(tabla);


        
    }
    
}

/**
 * The Main class represents the entry point of the program.
 * It creates a graphical user interface (GUI) window for the compiler application.
 */
import javax.swing.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        JFrame miVentana = new JFrame("Compilador");
        miVentana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        miVentana.setSize(1600, 1000);
        miVentana.setVisible(true);
        miVentana.setResizable(false);

        JLabel label = new JLabel("Programa");
        miVentana.add(label);
        label.setBounds(40, 40, 100, 30);
        //panel Programa
        JLayeredPane panelPrograma = new JLayeredPane();
        miVentana.add(panelPrograma);
        int posx = 40;
        int posy = 80;
        int ancho = 250;
        int alto = 400;
        panelPrograma.setBounds(posx, posy, ancho, alto);
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(posx, posy, ancho, alto);
        panelPrograma.add(scrollPane);

        JButton botonCompilar = new JButton("Compilar");
        miVentana.add(botonCompilar);
        botonCompilar.setBounds(350, 40, 100, 30);
        botonCompilar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                ScannerNewIdea scanner = new ScannerNewIdea();
                String texto = textArea.getText();
                Token[] tokens = scanner.scanear(texto);
                String[] columnas = {"Token", "Token Type"};
                String[][] datos = new String[tokens.length][2];
                for (int i = 0; i < tokens.length; i++) {
                    datos[i][1] = tokens[i].getTipo().toString();
                    datos[i][0] = tokens[i].getValor();
                }
                JTable tabla = new JTable(datos, columnas);
                JScrollPane scrollTabla = new JScrollPane(tabla);
                miVentana.add(scrollTabla);
                scrollTabla.setBounds(350, 80, 300, 400);

                JButton botonLimpiar = new JButton("Limpiar");
                miVentana.add(botonLimpiar);
                botonLimpiar.setBounds(500, 40, 100, 30);
                botonLimpiar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                                miVentana.remove(scrollTabla);
                                miVentana.remove(botonLimpiar);
                                miVentana.repaint();
                    }
                });
            }
        });
        
    }
}
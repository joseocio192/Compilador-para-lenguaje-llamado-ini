import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Main {
    public Main() {
        //la ventana
        JFrame miVentana = new JFrame("Compilador");
        miVentana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        miVentana.setSize(1600, 1000);
        //miVentana.setResizable(false);
        StringBuilder texto = new StringBuilder(); // Texto que se agregará al JTextArea
        //menu
        JMenuBar menu = new JMenuBar();
        miVentana.setJMenuBar(menu);
        JMenu archivo = new JMenu("Archivo");
        menu.add(archivo);
        JMenuItem abrir = new JMenuItem("Abrir");
        archivo.add(abrir);
        JMenuItem guardar = new JMenuItem("Guardar");
        archivo.add(guardar);
        JMenuItem salir = new JMenuItem("Salir");
        archivo.add(salir);

        //label y botones
        JLayeredPane paneStart = new JLayeredPane();
        miVentana.add(paneStart);

        JButton botonCompilar = new JButton("Compilar");
        paneStart.add(botonCompilar);
        botonCompilar.setBounds(350, 40, 100, 30);

        JButton botonLimpiar = new JButton("Limpiar");
        paneStart.add(botonLimpiar);
        botonLimpiar.setBounds(500, 40, 100, 30);

        JLabel label = new JLabel("Programa");
        label.setBounds(40, 40, 100, 30);
        paneStart.add(label);

        //panel Programa
        JTextArea textAreaProgram = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textAreaProgram);
        scrollPane.setBounds(40, 80, 250, 400);
        paneStart.add(scrollPane);

        //acciones
                //menu abrir
                abrir.addActionListener(e -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
                    fileChooser.showOpenDialog(miVentana);
                    File x = fileChooser.getSelectedFile();
                    StringBuilder textox = new StringBuilder(); // Use StringBuilder instead of concatenating strings
                    try (FileReader fr = new FileReader(x); BufferedReader br = new BufferedReader(fr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            textox.append(line).append("\n"); // Append line to StringBuilder
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    texto.append(textox); // Append textox to texto
                    textAreaProgram.setText(""); // Clear textAreaProgram
                    textAreaProgram.setText(texto.toString()); // Set textArea to texto
                    texto.setLength(0); // Clear texto
                    textox.setLength(0); // Clear textox
                });
                //menu guardar
                guardar.addActionListener(e -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.showSaveDialog(miVentana);
                    File x = fileChooser.getSelectedFile();
                    try {
                        if (x.createNewFile()) {
                            System.out.println("File created: " + x.getName());
                        } else {
                            System.out.println("File already exists.");
                        } 
                        // Create a FileWriter object
                        FileWriter writer = new FileWriter(x);
                        // Write the content of textAreaProgram to the file
                        writer.write(textAreaProgram.getText());
                        // Close the writer
                        writer.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                //menu salir
                salir.addActionListener(e -> System.exit(0));

        //boton compilar
        botonCompilar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                Scanner scanner = new Scanner();
                String texto = textAreaProgram.getText() + " ";
                Token[] tokens = scanner.scanear(texto);
                String[] columnas = {"Token", "Token Type"};
                String[][] datos = new String[tokens.length][2];
                for (int i = 0; i < tokens.length; i++) {
                    datos[i][1] = tokens[i].getTipo().toString();
                    datos[i][0] = tokens[i].getValor();
                }
                JTable tabla = new JTable(datos, columnas);
                JScrollPane scrollTabla = new JScrollPane(tabla);
                paneStart.add(scrollTabla);
                scrollTabla.setBounds(350, 80, 300, 400);
                botonLimpiar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                                paneStart.remove(scrollTabla);
                                miVentana.repaint();
                    }
                });
            }
        });
        miVentana.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}

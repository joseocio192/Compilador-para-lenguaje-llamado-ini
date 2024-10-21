package src;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {
  public Main() {
    // la ventana
    JFrame miVentana = new JFrame("Compilador");
    miVentana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    miVentana.setSize(1600, 1000);
    //miVentana.setIconImage(new javax.swing.ImageIcon(getClass().getResource("icon2.png")).getImage());
    // miVentana.setResizable(false);
    StringBuilder texto = new StringBuilder(); // Texto que se agregará al JTextArea
    // menu
    JMenuBar menu = new JMenuBar();
    miVentana.setJMenuBar(menu);
    JMenu archivo = new JMenu("Archivo");
    menu.add(archivo);
    JMenuItem nuevo = new JMenuItem("Nuevo");
    archivo.add(nuevo);
    JMenuItem abrir = new JMenuItem("Abrir");
    archivo.add(abrir);
    JMenuItem guardar = new JMenuItem("Guardar");
    archivo.add(guardar);
    JMenuItem salir = new JMenuItem("Salir");
    archivo.add(salir);

    // label y botones
    JLayeredPane paneStart = new JLayeredPane();
    miVentana.add(paneStart);

    JButton botonLexico = new JButton("Lexico");
    paneStart.add(botonLexico);
    botonLexico.setBounds(350, 40, 100, 30);

    JButton botonParser = new JButton("Parser");
    botonParser.setEnabled(false);
    paneStart.add(botonParser);
    botonParser.setBounds(650, 40, 100, 30);

    JButton botonLimpiar = new JButton("Limpiar");
    paneStart.add(botonLimpiar);
    botonLimpiar.setBounds(500, 40, 100, 30);

    JButton botonSemantico = new JButton("Semantico");
    botonSemantico.setEnabled(false);
    paneStart.add(botonSemantico);
    botonSemantico.setBounds(800, 40, 100, 30);

    JButton botonIntermedio = new JButton("Intermedio");
    botonIntermedio.setEnabled(false);
    paneStart.add(botonIntermedio);
    botonIntermedio.setBounds(950, 40, 100, 30);

    JLabel label = new JLabel("Programa");
    label.setBounds(40, 40, 100, 30);
    paneStart.add(label);

    // panel Programa
    JTextArea textAreaProgram = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textAreaProgram);
    scrollPane.setBounds(40, 80, 250, 400);
    paneStart.add(scrollPane);

    JTextArea textAreaIntermedio = new JTextArea();
    JScrollPane scrollPaneIntermedio = new JScrollPane(textAreaIntermedio);
    scrollPaneIntermedio.setBounds(950, 80, 250, 400);
    paneStart.add(scrollPaneIntermedio);

    textAreaProgram.setFont(new Font("Arial", Font.PLAIN, 18));

    textAreaProgram.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        resetButtons();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        resetButtons();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        resetButtons();
      }

      private void resetButtons() {
        // Reset the states of the buttons
        botonLexico.setEnabled(true);
        botonParser.setEnabled(false);
        botonSemantico.setEnabled(false);
        botonLimpiar.setEnabled(false);
      }
    });

    // acciones
    // menu abrir
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
    // menu guardar
    guardar.addActionListener(e -> {
      if (textAreaProgram.getText().equals("")) {
        JOptionPane.showMessageDialog(null, "No hay nada que guardar", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      } else {
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
      }

    });
    // menu salir
    salir.addActionListener(e -> System.exit(0));
    // menu nuevo
    nuevo.addActionListener(e -> {
      textAreaProgram.setText("");
      botonParser.setEnabled(false);
      botonLexico.setEnabled(true);
      miVentana.repaint();
    });

    // boton lexico
    botonLexico.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (textAreaProgram.getText().equals("")) {
          JOptionPane.showMessageDialog(null, "No hay nada que Analizar", "Error lexico", JOptionPane.ERROR_MESSAGE);
        } else {
          Scanner scanner = new Scanner();
          String texto = textAreaProgram.getText() + " ";
          try {
            Token[] tokens = scanner.scanear(texto);
            String[] columnas = { "Token", "Token Type" };
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
                botonLimpiar.setEnabled(false);
                paneStart.remove(scrollTabla);
                botonParser.setEnabled(false);
                botonLexico.setEnabled(true);
                miVentana.repaint();

              }
            });
            botonLimpiar.setEnabled(true);
            botonLexico.setEnabled(false);
            botonParser.setEnabled(true);
          } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, "Algo a salido mal: " + ex.getLocalizedMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);

          }
        }
      }
    });

    // boton parser
    botonParser.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String texto = textAreaProgram.getText() + " ";
        Scanner scanner = new Scanner();
        Token[] tokens = scanner.scanear(texto);
        Parser parser = new Parser(tokens);
        try {
          boolean x = parser.parse();
          if (x) {
            JOptionPane.showMessageDialog(null, "El programa es correcto", "Correcto", JOptionPane.INFORMATION_MESSAGE);
            botonSemantico.setEnabled(true);
            botonParser.setEnabled(false);
          } else {
            JOptionPane.showMessageDialog(null, "El programa es incorrecto", "Incorrecto", JOptionPane.ERROR_MESSAGE);
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(null, "Algo a salido mal: " + ex.getLocalizedMessage(), "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // boton semantico
    botonSemantico.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Boton semantico");
        String texto = textAreaProgram.getText() + " ";
        Scanner scanner = new Scanner();
        Token[] tokens = scanner.scanear(texto);
        Semantico semantico = new Semantico(tokens);
        try {
          boolean x = semantico.isSemanticallyCorrect();
          if (x) {
            JOptionPane.showMessageDialog(null, "El programa es correcto", "Correcto", JOptionPane.INFORMATION_MESSAGE);
            botonSemantico.setEnabled(false);
            botonIntermedio.setEnabled(true);
          } else {
            JOptionPane.showMessageDialog(null, "El programa es incorrecto", "Incorrecto", JOptionPane.ERROR_MESSAGE);
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(null, "Algo a salido mal: " + ex.getLocalizedMessage(), "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // boton intermedio
    botonIntermedio.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String texto = textAreaProgram.getText() + " ";
        Scanner scanner = new Scanner();
        Token[] tokens = scanner.scanear(texto);
        Parser parser = new Parser(tokens);
        try {
          boolean x = parser.parse();
          if (x) {
            JOptionPane.showMessageDialog(null, "El programa es correcto", "Correcto", JOptionPane.INFORMATION_MESSAGE);
            botonSemantico.setEnabled(true);
            botonParser.setEnabled(false);
          } else {
            JOptionPane.showMessageDialog(null, "El programa es incorrecto", "Incorrecto", JOptionPane.ERROR_MESSAGE);
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(null, "Algo a salido mal: " + ex.getLocalizedMessage(), "Error",
              JOptionPane.ERROR_MESSAGE);
        }
        if (botonSemantico.isEnabled()) {
          Semantico semantico = new Semantico(tokens);
          try {
            boolean x = semantico.isSemanticallyCorrect();
            if (x) {
              JOptionPane.showMessageDialog(null, "El programa es correcto", "Correcto", JOptionPane.INFORMATION_MESSAGE);
              botonSemantico.setEnabled(false);
            } else {
              JOptionPane.showMessageDialog(null, "El programa es incorrecto", "Incorrecto", JOptionPane.ERROR_MESSAGE);
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Algo a salido mal: " + ex.getLocalizedMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
          }
        }
        if (!botonSemantico.isEnabled()) {
          Intermedio intermedio = new Intermedio(tokens);
          try {
           String x = intermedio.getIntermedioCode();
            textAreaIntermedio.setText(x);
            botonIntermedio.setEnabled(false);
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Algo a salido mal: " + ex.getLocalizedMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });

    miVentana.setVisible(true);
  }

  public static void main(String[] args) {
    new Main();
  }
}

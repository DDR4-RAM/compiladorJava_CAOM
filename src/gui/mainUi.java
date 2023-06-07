package gui;

import src.AnalizadorSintactico;
import src.Scanner;
import src.Token;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JOptionPane;

public class mainUi extends JFrame {
    private JPanel panel1;
    private JLabel jLabel;
    private JTextArea ingresaTuCódigoTextArea;
    private JButton ejecutarButton;
    private JButton archivoButton;
    //Create a file chooser
    final JFileChooser fc = new JFileChooser();


    public mainUi() {
        URL iconURL = getClass().getResource("./ram.jpg");
        ImageIcon img = new ImageIcon(iconURL);
        setContentPane(panel1);
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());
        setTitle("Intérprete");
        listener();
        setVisible(true);
        archivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    int returnVal = fc.showOpenDialog(panel1);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        Path path = Paths.get(file.getPath());
                        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
                        StringBuilder codigo = new StringBuilder();
                        for (String line : allLines) {
                            System.out.println(line);
                            codigo.append("\n").append(line);
                        }
                        ingresaTuCódigoTextArea.setText(codigo.toString());
                        //This is where a real application would open the file.
                        System.out.println("Opening: " + file.getName() + ".");
                    } else {
                        JOptionPane.showMessageDialog(panel1, "Cancelado");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(panel1, "Error "+ex.getMessage());
                }
            }
        });
    }

    private void listener() {
        ejecutarButton.addActionListener(e -> {
            Scanner scanner = new Scanner(ingresaTuCódigoTextArea.getText());
            List<Token> tokens = scanner.scanTokens();
            StringBuilder tokens_string = new StringBuilder();
            for (Token token : tokens) {
                tokens_string.append(token).append("\n");
                System.out.println(token);
            }
            AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico(tokens);
            JOptionPane.showMessageDialog(panel1, "Aquí estan los tokens:\n" + tokens_string);
            String mensaje = analizadorSintactico.parse();
            JOptionPane.showMessageDialog(panel1, "Resultado del Analisis Sintactico:\n" + mensaje);

        });
    }

}

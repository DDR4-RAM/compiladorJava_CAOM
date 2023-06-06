package gui;

import src.AnalizadorSintactico;
import src.Scanner;
import src.Token;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;

public class mainUi extends JFrame {
    private JPanel panel1;
    private JLabel jLabel;
    private JTextArea ingresaTuCódigoTextArea;
    private JButton ejecutarButton;


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

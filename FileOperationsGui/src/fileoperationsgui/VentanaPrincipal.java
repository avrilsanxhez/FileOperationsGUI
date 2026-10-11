package fileoperationsgui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VentanaPrincipal extends JFrame {

    public void iniciarComponentes() {
        this.setSize(600, 500);
        this.setTitle("File Operation GUI");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        setLayout(new BorderLayout());
        JPanel pan = new JPanel(new BorderLayout(10, 10));
        pan.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField txtRuta = new JTextField();

        JTextField txtDestino = new JTextField();

        JPanel panelEntrada = new JPanel(new GridLayout(2, 2, 5, 5));
        panelEntrada.add(new JLabel("I)ngrese la ruta del archivo:"));
        panelEntrada.add(txtRuta);
        panelEntrada.add(new JLabel("Ingrese una nueva ruta:"));
        panelEntrada.add(txtDestino);

        pan.add(panelEntrada, BorderLayout.NORTH);

        JTextArea txtArea = new JTextArea();
        pan.add(new JScrollPane(txtArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        JButton btnCrear = new JButton("Crear archivo");
        JButton btnEscribir = new JButton("Escribir en archivo");
        JButton btnLeer = new JButton("Leer archivo");
        JButton btnMover = new JButton("Mover archivo");
        JButton btnEliminar = new JButton("Eliminar archivo");

        JPanel botones = new JPanel(new GridLayout(2, 2, 5, 5));

        botones.add(btnCrear);
        botones.add(btnEscribir);
        botones.add(btnLeer);
        botones.add(btnMover);
        botones.add(btnEliminar);

        panel.add(botones, BorderLayout.SOUTH);

        this.getContentPane().add(pan);
        this.getContentPane().add(panel, BorderLayout.SOUTH);

        btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path p = Paths.get(txtRuta.getText()); // leer ruta actual
                try {
                    //crear directorios padre si no existen
                    if (p.getParent() != null) {
                        Files.createDirectories(p.getParent());
                    }

                    //crear el archivo si no existe
                    if (Files.exists(p)) {
                        txtArea.setText("El archivo ya existía previamente");
                    } else {
                        Path donePath = Files.createFile(p);
                        txtArea.setText("Archivo creado correctamente en: " + donePath.toAbsolutePath());
                    }

                } catch (IOException ex) {
                    txtArea.setText(ex.getMessage());
                }
            }
        });

        btnEscribir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path p = Paths.get(txtRuta.getText());
                try (FileWriter writer = new FileWriter(p.toFile())) {
                    writer.write(txtArea.getText());
                    txtArea.setText("Archivo escrito correctamente");
                } catch (IOException ex) {
                    txtArea.setText(ex.getMessage());
                }
            }
        });

        btnLeer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path p = Paths.get(txtRuta.getText());
                txtArea.setText("");
                try (BufferedReader br = new BufferedReader(new FileReader(p.toFile()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        txtArea.append(line + "\n");
                    }
                } catch (IOException ex) {
                    txtArea.setText(ex.getMessage());
                }
            }
        });

        btnMover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path p = Paths.get(txtRuta.getText());

                Path destino = Paths.get(txtDestino.getText()); //leer la ruta actual
                try {
                    if (destino.getParent() != null) {
                        Files.createDirectories(destino.getParent());
                    }
                    Path mover = Files.move(p, destino, StandardCopyOption.REPLACE_EXISTING);
                    txtArea.setText("Archivo movido a: " + mover.toAbsolutePath());
                } catch (IOException ex) {
                    txtArea.setText(ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path p = Paths.get(txtRuta.getText());
                try {
                    boolean deleted = Files.deleteIfExists(p);
                    if (deleted) {
                        txtArea.setText("Archivo eliminado correctamente");
                    } else {
                        txtArea.setText("El archivo no existe");
                    }
                } catch (IOException ex) {
                    txtArea.setText(ex.getMessage());
                }
            }

        });

    }
}

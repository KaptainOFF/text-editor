package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextEditor extends JFrame {
    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);

        JTextArea fileNameField = new JTextArea(1, 10);
        fileNameField.setName("FilenameField");
        fileNameField.getDocument().putProperty("filterNewlines",
                Boolean.TRUE);
        fileNameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextArea jTextArea = new JTextArea();
        jTextArea.setName("TextArea");
        setTitle("The first page");
        jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton saveButton = new JButton("Save");
        saveButton.setName("SaveButton");
        saveButton.setBounds(30, 0, 80, 15);
        saveButton.addActionListener(listener -> {
            String fileName = fileNameField.getText();
            try (FileOutputStream writer = new FileOutputStream(new File(fileName))) {
                byte[] lines = jTextArea.getText().getBytes();
                writer.write(lines);
            } catch (IOException e) {
                jTextArea.setText(null);
            }
        });

        JButton loadButton = new JButton("Load");
        loadButton.setName("LoadButton");
        loadButton.setBounds(100, 0, 80, 15);
        loadButton.addActionListener(listener -> {
            String fileName = fileNameField.getText();
            try (InputStream stream = new FileInputStream(new File(fileName));) {
                byte[] file = stream.readAllBytes();
                jTextArea.setText(new String(file));
            } catch (IOException e) {
                jTextArea.setText(null);
            }
        });

        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setName("ScrollPane");
        scrollPane.setBounds(20, 40, 245, 200);

        JPanel textBodyPanel = new JPanel();
        textBodyPanel.setBounds(30, 30, 220, 220);
        textBodyPanel.add(fileNameField, BorderLayout.PAGE_START);
        textBodyPanel.add(saveButton, BorderLayout.PAGE_START);
        textBodyPanel.add(loadButton, BorderLayout.PAGE_START);
        textBodyPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JMenuBar menuBar = new JMenuBar();
        menuBar.setName("MenuBar");

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setName("MenuFile");

        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setName("MenuLoad");
        loadMenuItem.addActionListener(listener -> {
            String fileName = fileNameField.getText();
            try (InputStream stream = new FileInputStream(new File(fileName));) {
                byte[] file = stream.readAllBytes();
                jTextArea.setText(new String(file));
            } catch (IOException e) {
                jTextArea.setText(null);
            }
        });

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        saveMenuItem.addActionListener(listener -> {
            String fileName = fileNameField.getText();
            try (FileOutputStream writer = new FileOutputStream(new File(fileName))) {
                byte[] lines = jTextArea.getText().getBytes();
                writer.write(lines);
            } catch (IOException e) {
                jTextArea.setText(null);
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");
        exitMenuItem.addActionListener(event -> dispose());

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
        add(scrollPane);
        add(textBodyPanel);


        setVisible(true);

    }
}
package editor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
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
            try (FileWriter writer = new FileWriter(new File(fileName));) {
                writer.write(jTextArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        JButton loadButton = new JButton("Load");
        loadButton.setName("LoadButton");
        loadButton.setBounds(100, 0, 80, 15);
        loadButton.addActionListener(listener -> {
            String fileName = fileNameField.getText();
            try (Scanner scanner = new Scanner(new File(fileName));) {
                while (scanner.hasNext()) {
                    jTextArea.append(scanner.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
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

        add(scrollPane);
        add(textBodyPanel);


        setVisible(true);

    }
}
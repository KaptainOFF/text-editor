package editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.function.Consumer;

public class TextEditor extends JFrame {

    public TextEditor() {
        initApp();
    }

    private void initApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 420);
        setLocationRelativeTo(null);

        JTextArea searchField = new JTextArea();
        searchField.setPreferredSize(new Dimension(100, 30));
        searchField.setName("FilenameField");
        searchField.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        searchField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextArea jTextArea = new JTextArea();
        jTextArea.setName("TextArea");
        setTitle("Text Editor");
        jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setName("ScrollPane");
        scrollPane.setBounds(20, 50, 345, 300);


        JButton saveButton = new JButton();
        ImageIcon icon = new ImageIcon("C:\\Users\\Kapitanov\\Downloads\\save.png");
        Image image = icon.getImage();
        saveButton.setIcon(new ImageIcon(image.getScaledInstance(32, 32, 4)));
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(32, 32));
        saveButton.addActionListener(saveFileActionListener(jTextArea));

        JButton loadButton = new JButton();
        ImageIcon iconLoad = new ImageIcon("C:\\Users\\Kapitanov\\Downloads\\load.png");
        Image imageLoad = iconLoad.getImage();
        loadButton.setIcon(new ImageIcon(imageLoad.getScaledInstance(32, 32, 4)));
        loadButton.setName("LoadButton");
        loadButton.setPreferredSize(new Dimension(32, 32));
        loadButton.addActionListener(loadFileActionListener(jTextArea));

        JCheckBox useRegExpCheckBox = new JCheckBox("Use regex");

        JButton searchButton = new JButton();
        ImageIcon search = new ImageIcon("C:\\Users\\Kapitanov\\Downloads\\mglass.png");
        Image searchImage = search.getImage();
        searchButton.setIcon(new ImageIcon(searchImage.getScaledInstance(32, 32, 1)));
        searchButton.setPreferredSize(new Dimension(32, 32));
        searchButton.setName("SearchButton");
        searchButton.addActionListener((listener) -> {
        });

        JButton previousMatchButton = new JButton();
        ImageIcon arrLeft = new ImageIcon("C:\\Users\\Kapitanov\\Downloads\\arrLeft.png");
        Image arrLeftImage = arrLeft.getImage();
        previousMatchButton.setIcon(new ImageIcon(arrLeftImage.getScaledInstance(32, 32, 1)));
        previousMatchButton.setPreferredSize(new Dimension(32, 32));
        previousMatchButton.setName("PreviousMatchButton");

        JButton nextMatchButton = new JButton();
        ImageIcon arrRight = new ImageIcon("C:\\Users\\Kapitanov\\Downloads\\arrRight.png");
        Image arrRightImage = arrRight.getImage();
        nextMatchButton.setIcon(new ImageIcon(arrRightImage.getScaledInstance(32, 32, 1)));
        nextMatchButton.setPreferredSize(new Dimension(32, 32));
        nextMatchButton.setName("NextMatchButton");

        JPanel textBodyPanel = new JPanel();
        textBodyPanel.setBounds(30, 30, 200, 200);
        textBodyPanel.add(saveButton, BorderLayout.PAGE_START);
        textBodyPanel.add(loadButton, BorderLayout.PAGE_START);
        textBodyPanel.add(searchField, BorderLayout.PAGE_START);
        textBodyPanel.add(searchButton, BorderLayout.PAGE_START);
        textBodyPanel.add(previousMatchButton, BorderLayout.PAGE_START);
        textBodyPanel.add(nextMatchButton, BorderLayout.PAGE_START);
        textBodyPanel.add(useRegExpCheckBox, BorderLayout.PAGE_START);
        textBodyPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JMenuBar menuBar = new JMenuBar();
        menuBar.setName("MenuBar");

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setMnemonic(KeyEvent.VK_S);
        searchMenu.setName("MenuSearch");

        JMenuItem startSearch = new JMenuItem("Start search");
        startSearch.setName("MenuSearchStart");
        searchMenu.add(startSearch);

        JMenuItem previousSearch = new JMenuItem("Previous match");
        previousSearch.setName("MenuPreviousMatch");
        searchMenu.add(previousSearch);

        JMenuItem nextMatch = new JMenuItem("Next match");
        nextMatch.setName("MenuNextMatch");
        searchMenu.add(nextMatch);

        JMenuItem useRegExp = new JMenuItem("Use regular expressions");
        useRegExp.setName("MenuUseRegExp");
        searchMenu.add(useRegExp);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setName("MenuFile");

        JMenuItem loadMenuItem = new JMenuItem("Open");
        loadMenuItem.setName("MenuLoad");
        loadMenuItem.addActionListener(loadFileActionListener(jTextArea));

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        saveMenuItem.addActionListener(saveFileActionListener(jTextArea));

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");
        exitMenuItem.addActionListener(event -> dispose());

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(searchMenu);

        setJMenuBar(menuBar);
        add(scrollPane);
        add(textBodyPanel);


        setVisible(true);

    }

    private ActionListener loadFileActionListener(JTextArea jTextArea) {
        return listener -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                try (InputStream stream = new FileInputStream(selectedFile)) {
                    byte[] file = stream.readAllBytes();
                    jTextArea.setText(new String(file));
                } catch (IOException e) {
                    jTextArea.setText(null);
                }
            }

        };
    }

    private ActionListener saveFileActionListener(JTextArea jTextArea) {
        return (listener) -> {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                try (FileOutputStream writer = new FileOutputStream(selectedFile)) {
                    byte[] lines = jTextArea.getText().getBytes();
                    writer.write(lines);
                } catch (IOException e) {
                    jTextArea.setText(null);
                }
            }
        };
    }
}
package editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {

    public TextEditor() {
        initApp();
    }

    private void initApp() {
        List<Integer> occurrenceIndexes = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 420);
        setLocationRelativeTo(null);

        JTextArea searchField = new JTextArea();
        searchField.setPreferredSize(new Dimension(100, 30));
        searchField.setName("SearchField");
        searchField.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        searchField.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextArea jTextArea = new JTextArea();
        jTextArea.setName("TextArea");
        setTitle("Text Editor");
        jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setName("FileChooser");
        add(jfc);

        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setName("ScrollPane");
        scrollPane.setBounds(20, 50, 345, 300);


        JButton saveButton = new JButton();
        Image image = new ImageIcon("C:\\Users\\Kapitanov\\Downloads\\save.png").getImage();
        saveButton.setIcon(new ImageIcon(image.getScaledInstance(32, 32, 4)));
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(32, 32));
        saveButton.addActionListener(saveFileActionListener(jTextArea, jfc));

        JButton loadButton = new JButton();
        Image imageLoad = new ImageIcon("C:\\Users\\Kapitanov\\Downloads\\load.png").getImage();
        loadButton.setIcon(new ImageIcon(imageLoad.getScaledInstance(32, 32, 4)));
        loadButton.setName("OpenButton");
        loadButton.setPreferredSize(new Dimension(32, 32));
        loadButton.addActionListener(loadFileActionListener(jTextArea, jfc));

        JCheckBox useRegExpCheckBox = new JCheckBox("Use regex");
        useRegExpCheckBox.setName("UseRegExCheckbox");

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

        JButton searchButton = new JButton();
        Image searchImage = new ImageIcon("C:\\Users\\Kapitanov\\Downloads\\mglass.png").getImage();
        searchButton.setIcon(new ImageIcon(searchImage.getScaledInstance(32, 32, 1)));
        searchButton.setPreferredSize(new Dimension(32, 32));
        searchButton.setName("StartSearchButton");
        searchButton.addActionListener((listener) -> {
            Thread thread = new Thread(() -> {
                String searchWord = searchField.getText();
                String text = jTextArea.getText();
                Pattern pattern = Pattern.compile(searchWord);
                Matcher matcher = pattern.matcher(text);
                AtomicInteger index = new AtomicInteger(0);
                while (matcher.find()) {
                    occurrenceIndexes.add(matcher.start());
                    System.out.println(matcher.start() + searchWord.length());
                }
                startSearch(occurrenceIndexes.get(index.getAndIncrement()), jTextArea, searchWord);
                nextMatchButton.addActionListener(actionEvent -> {
                        try {
                            startSearch(occurrenceIndexes.get(index.getAndIncrement()), jTextArea, searchWord);
                        } catch (IndexOutOfBoundsException e) {
                            index.set(0);
                        }
                });

                previousMatchButton.addActionListener(l -> {
                    if (index.get() >= 0) {
                        startSearch(occurrenceIndexes.get(index.getAndDecrement()), jTextArea, searchWord);
                    } else {
                        index.set(occurrenceIndexes.size());
                    }
                });
                occurrenceIndexes.forEach(System.out::println);
            });
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


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

        JMenuItem previousSearch = new JMenuItem("Previous match");
        previousSearch.setName("MenuPreviousMatch");
        searchMenu.add(previousSearch);

        JMenuItem nextMatch = new JMenuItem("Next match");
        nextMatch.setName("MenuNextMatch");
        searchMenu.add(nextMatch);

        JMenuItem startSearch = new JMenuItem("Start search");
        startSearch.setName("MenuStartSearch");
        searchMenu.add(startSearch);
        startSearch.addActionListener((listener) -> {
            Thread thread = new Thread(() -> {
                String searchWord = searchField.getText();
                String text = jTextArea.getText();
                Pattern pattern = Pattern.compile(searchWord);
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    occurrenceIndexes.add(matcher.start());
                }

            });
            try {
                thread.start();
                thread.join();
                String searchWord = searchField.getText();
                AtomicInteger index = new AtomicInteger(0);
                startSearch(occurrenceIndexes.get(index.getAndIncrement()), jTextArea, searchWord);
                nextMatch.addActionListener(actionEvent -> {
                    try {
                        startSearch(occurrenceIndexes.get(index.getAndIncrement()), jTextArea, searchWord);
                    } catch (IndexOutOfBoundsException e) {
                        index.set(0);
                    }
                });

                previousSearch.addActionListener(l -> {
                    try {
                        startSearch(occurrenceIndexes.get(index.getAndDecrement()), jTextArea, searchWord);
                    } catch (IndexOutOfBoundsException e) {
                        index.set(occurrenceIndexes.size() - 1);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        JMenuItem useRegExp = new JMenuItem("Use regular expressions");
        useRegExp.setName("MenuUseRegExp");
        searchMenu.add(useRegExp);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setName("MenuFile");

        JMenuItem loadMenuItem = new JMenuItem("Open");
        loadMenuItem.setName("MenuOpen");
        loadMenuItem.addActionListener(loadFileActionListener(jTextArea, jfc));

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        saveMenuItem.addActionListener(saveFileActionListener(jTextArea, jfc));

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

    private void startSearch(int index , JTextArea jTextArea, String word) {
        jTextArea.setCaretPosition(index + word.length());
        jTextArea.select(index, index + word.length());
        jTextArea.grabFocus();
    }

    private ActionListener loadFileActionListener(JTextArea jTextArea, JFileChooser jfc) {
        return listener -> {

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

    private ActionListener saveFileActionListener(JTextArea jTextArea, JFileChooser jfc) {
        return (listener) -> {

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
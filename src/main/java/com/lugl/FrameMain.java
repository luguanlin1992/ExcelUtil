package com.lugl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luguanlin
 * 2022/5/13 10:12
 */
public class FrameMain extends JFrame {
    String curRoot = System.getProperty("user.dir");

    JPanel up, mid, low;
    JLabel excelLabel, jsonLabel, javaLabel;
    JTextField excelField, jsonField, javaField;
    JButton excelButton, jsonButton, javaButton, cJsonButton, cJavaButton;
    JPanel lowPanel;
    JScrollPane filePanel;
    JList fileList;

    public static void main(String[] args) {
        new FrameMain();
    }

    public FrameMain() throws HeadlessException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setTitle("Excel to json and java " + curRoot);
        this.setSize(screenSize.width / 2, screenSize.height / 2);
        this.setLocation(screenSize.width / 4, screenSize.height / 4);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);


        up = new JPanel();
        up.setSize(this.getWidth(), 30);
        mid = new JPanel();
        mid.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - 40));
        low = new JPanel();
        low.setSize(this.getWidth(), 10);

        excelLabel = new JLabel("excel root:");
        jsonLabel = new JLabel("json root:");
        javaLabel = new JLabel("java root:");

        excelField = new JTextField("");
        jsonField = new JTextField("");
        javaField = new JTextField("");

        excelButton = new JButton("chose dir");
        jsonButton = new JButton("chose dir");
        javaButton = new JButton("chose dir");

        excelButton.addActionListener(new ExcelButtonListener());
        jsonButton.addActionListener(new JsonButtonListener());
        javaButton.addActionListener(new JavaButtonListener());

        cJsonButton = new JButton("create json");
        cJavaButton = new JButton("create java");

        cJsonButton.addActionListener(new CJsonButtonListener());
        cJavaButton.addActionListener(new CJavaButtonListener());

        filePanel = new JScrollPane();
        filePanel.setPreferredSize(mid.getPreferredSize());
        fileList = new JList();
        fileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filePanel.setViewportView(fileList);

        lowPanel = new JPanel();
        lowPanel.add(cJsonButton);
        lowPanel.add(cJavaButton);

        GroupLayout upLayout = new GroupLayout(up);
        up.setLayout(upLayout);
        GroupLayout.SequentialGroup hGroup = upLayout.createSequentialGroup();
        hGroup.addGap(5);
        hGroup.addGroup(upLayout.createParallelGroup().addComponent(excelLabel).addComponent(jsonLabel).addComponent(javaLabel));
        hGroup.addGap(10);
        hGroup.addGroup(upLayout.createParallelGroup().addComponent(excelField).addComponent(jsonField).addComponent(javaField));
        hGroup.addGroup(upLayout.createParallelGroup().addComponent(excelButton).addComponent(jsonButton).addComponent(javaButton));
        upLayout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = upLayout.createSequentialGroup();
        vGroup.addGap(5);
        vGroup.addGroup(upLayout.createParallelGroup().addComponent(excelLabel).addComponent(excelField).addComponent(excelButton));
        vGroup.addGap(5);
        vGroup.addGroup(upLayout.createParallelGroup().addComponent(jsonLabel).addComponent(jsonField).addComponent(jsonButton));
        vGroup.addGap(5);
        vGroup.addGroup(upLayout.createParallelGroup().addComponent(javaLabel).addComponent(javaField).addComponent(javaButton));
        upLayout.setVerticalGroup(vGroup);

        mid.add(filePanel);
        low.add(lowPanel);

        this.add(up, BorderLayout.NORTH);
        this.add(mid, BorderLayout.CENTER);
        this.add(low, BorderLayout.SOUTH);

        this.setVisible(true);
        initData();
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            GlobalVar.close();
        }
        super.processWindowEvent(e);
    }

    class ExcelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String root = excelField.getText();
            if (root.isEmpty()) {
                root = curRoot;
                excelField.setText(root);
            }
            JFileChooser fc = new JFileChooser(root);
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int val = fc.showOpenDialog(null);
            if (val == fc.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                excelField.setText(file.toString());
                showFileList();
            }
        }
    }

    class JsonButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String root = jsonField.getText();
            if (root.isEmpty()) {
                root = curRoot;
                jsonField.setText(root);
            }
            JFileChooser fc = new JFileChooser(root);
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int val = fc.showOpenDialog(null);
            if (val == fc.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                jsonField.setText(file.toString());
            }
        }
    }

    class JavaButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String root = javaField.getText();
            if (root.isEmpty()) {
                root = curRoot;
                javaField.setText(root);
            }
            JFileChooser fc = new JFileChooser(root);
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int val = fc.showOpenDialog(null);
            if (val == fc.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                javaField.setText(file.toString());
            }
        }
    }

    class CJsonButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GlobalVar.jsonPath = jsonField.getText();
            ArrayList<String> list = new ArrayList<>();
            List selectedValuesList = fileList.getSelectedValuesList();
            for (int i = 0; i < selectedValuesList.size(); i++) {
                Object o = selectedValuesList.get(i);
                File file = new File(o.toString());
                if (!JsonMain.buildData(file)) {
                    list.add(file.getName());
                }
            }
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(null, "success", "create json", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, list + " failed!!!", "create json", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    class CJavaButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GlobalVar.excelPath = excelField.getText();
            GlobalVar.javaPath = javaField.getText();
            JavaBeanMain.buildCommons();
            ArrayList<String> list = new ArrayList<>();
            List selectedValuesList = fileList.getSelectedValuesList();
            for (int i = 0; i < selectedValuesList.size(); i++) {
                Object o = selectedValuesList.get(i);
                File file = new File(o.toString());
                if (!JavaBeanMain.buildData(file)) {
                    list.add(file.getName());
                }
            }
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(null, "success", "create java", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, list + " failed!!!", "create java", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void initData() {
        GlobalVar.initData();
        excelField.setText(GlobalVar.excelPath);
        jsonField.setText(GlobalVar.jsonPath);
        javaField.setText(GlobalVar.javaPath);
        excelField.setText(GlobalVar.excelPath);
        showFileList();
    }

    public void showFileList() {
        File root = new File(excelField.getText());
        File[] files = root.listFiles((dir, name) -> name.endsWith(".xlsx"));
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getPath();
        }
        fileList.setListData(names);
    }
}

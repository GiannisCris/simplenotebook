/*Java简易记事本编写
        课题：设计一个简单的Windows记事本，可以实现以下功能
        1、文件的新建、打开、保存、另存为和退出；
        2、对文件的编辑，如复制、粘贴、剪切、替换等；
        3、执行完操作后，能够顺利关闭记事本。
        源码：
        package MyPad;
        */

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
public class simplenotebook {

    private static String path = "";

    public static void main(String[] args) {
        // 设置主窗体
        JFrame jf = new JFrame("lil pad");
        jf.setBounds(220, 90, 800, 600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置替换功能窗体
        final JFrame frame = new JFrame("替换");
        frame.setBounds(220, 90, 300, 250);
        JLabel l1 = new JLabel(" 原字符串");
        frame.add(l1);
        final JTextField tf1 = new JTextField();
        tf1.setColumns(15);
        frame.add(tf1);
        JLabel l2 = new JLabel("    替换为");
        final JTextField tf2 = new JTextField();
        tf2.setColumns(15);
        JButton button = new JButton("确定");
        frame.setLayout(new FlowLayout());
        frame.add(l2);
        frame.add(tf2);
        frame.add(button);

        // 设置菜单栏
        JMenuBar jmbar = new JMenuBar();
        jf.setJMenuBar(jmbar);

        // ===============设置文件菜单=====================
        JMenu filemenu = new JMenu("文件(F)");
        JMenuItem newitem = new JMenuItem("新建(N)");
        JMenuItem openitem = new JMenuItem("打开(O)");
        // openitem.setMnemonic('O');
        JMenuItem saveitem = new JMenuItem("保存(S)");
        JMenuItem lsaveitem = new JMenuItem("另存为(A)");
        JMenuItem exititem = new JMenuItem("退出(X)");
        jmbar.add(filemenu);
        filemenu.add(newitem);
        filemenu.add(openitem);
        filemenu.add(saveitem);
        filemenu.add(lsaveitem);
        filemenu.addSeparator();
        filemenu.add(exititem);

        // ===============设置编辑菜单=====================
        JMenu editmenu = new JMenu("编辑(E)");
        JMenuItem cutitem = new JMenuItem("剪切(T)");
        JMenuItem copyitem = new JMenuItem("复制(C)");
        JMenuItem pasteitem = new JMenuItem("粘贴(P)");
        JMenuItem replaceitem = new JMenuItem("替换(R)");
        jmbar.add(editmenu);
        editmenu.add(cutitem);
        editmenu.add(copyitem);
        editmenu.add(pasteitem);
        editmenu.add(replaceitem);

        // ===============设置帮助菜单=====================
        JMenu helpmenu = new JMenu("帮助(H)");
        JMenuItem helpitem = new JMenuItem("lil pad说明(A)");
        jmbar.add(helpmenu);
        helpmenu.add(helpitem);

        final JTextArea text = new JTextArea();
        JScrollPane jsp = new JScrollPane(text);
        jf.add(jsp);
        jf.setVisible(true);

        // ===============设置快捷键=====================（使用setAccelerator方法）
        newitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.CTRL_MASK));
        openitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_MASK));
        saveitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_MASK));
        cutitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                InputEvent.CTRL_MASK));
        copyitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                InputEvent.CTRL_MASK));
        pasteitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                InputEvent.CTRL_MASK));
        replaceitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                InputEvent.CTRL_MASK));

        // ===============实现新建功能=====================
        newitem.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (text.getText().equals(""))
                    text.setText("");
                else {
                    int value = JOptionPane.showConfirmDialog(null, "是否要保存文本？",
                            "提示", JOptionPane.YES_NO_OPTION);
                    String text_ = text.getText();
                    if (value == JOptionPane.YES_OPTION)
                        try {
                            lsave(text_);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    text.setText("");
                }

            }
        });

        // ===============实现打开功能=====================
        openitem.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (text.getText().equals(""))
                    nw(text);
                else {
                    int value = JOptionPane.showConfirmDialog(null, "是否要保存文本？",
                            "提示", JOptionPane.YES_NO_OPTION);
                    String text_ = text.getText();
                    if (value == JOptionPane.YES_OPTION)
                        try {
                            lsave(text_);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    else if (value == JOptionPane.NO_OPTION)
                        text.setText("");
                    else
                        return;
                    nw(text);
                }

            }

        });

        // ===============实现保存功能=====================
        saveitem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String text_ = text.getText();
                try {
                    save(text_);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // ===============实现另存为功能=====================
        lsaveitem.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                String text_ = text.getText();
                try {
                    lsave(text_);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // ===============实现退出功能=====================
        exititem.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (text.getText().equals(""))
                    System.exit(0);
                else {
                    int value = JOptionPane.showConfirmDialog(null, "保存文本？",
                            "提示", JOptionPane.YES_NO_OPTION);
                    String text_ = text.getText();
                    if (value == JOptionPane.YES_OPTION)
                        try {
                            lsave(text_);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    System.exit(0);
                }

            }

        });

        // ===============实现剪切功能=====================
        cutitem.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                text.cut();
            }
        });

        // ===============实现复制功能=====================
        copyitem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                text.copy();
            }
        });
        // ===============实现粘贴功能=====================
        pasteitem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                text.paste();
            }
        });
        // ===============实现替换功能=====================
        replaceitem.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                frame.setVisible(true);

            }

        });
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                String reptext = text.getText().replaceAll(tf1.getText(),
                        tf2.getText());
                text.setText("");
                text.append(reptext);

            }

        });

        // ===============实现lil pad说明功能================
        helpitem.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                JOptionPane.showOptionDialog(null, "程序名称:\n lil pad \n"
                                + "程序设计:\n Java课程设计   \n" + "简介:\n 一个简单的文字编辑器\n"
                                + " 制作:  翠翠抄老师的\n" ,
                        "关于MyPad", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        });

    }// Main

    private static void openfile(File file, JTextArea text) throws IOException {

        BufferedReader bufr = new BufferedReader(new FileReader(file));
        String line;
        while ((line = bufr.readLine()) != null) {
            text.append(line);
            text.append("\r\n");
        }
        bufr.close();

    }

    private static void nw(JTextArea text) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            File file = new File(path);
            try {
                openfile(file, text);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static void lsave(String text) throws IOException {
        JFileChooser chooser = new JFileChooser();
        File file;
        if (chooser.showSaveDialog(chooser) == JFileChooser.APPROVE_OPTION) {
            {
                path = chooser.getSelectedFile().getAbsolutePath();
                file = new File(path);
                savefile(file, text);
            }
        }
    }

    private static void save(String text) throws IOException {

        JFileChooser chooser = new JFileChooser();
        File file;
        if (path.equals("")) {
            if (chooser.showSaveDialog(chooser) == JFileChooser.APPROVE_OPTION) {
                path = chooser.getSelectedFile().getAbsolutePath();
            } else
                return;
        }
        file = new File(path);
        savefile(file, text);

    }

    private static void savefile(File file, String text) throws IOException {

        byte[] b = text.getBytes();
        if (file != null) {
            BufferedOutputStream bufw = new BufferedOutputStream(
                    new FileOutputStream(file));
            bufw.write(b, 0, b.length);
            bufw.close();
        }
    }
}
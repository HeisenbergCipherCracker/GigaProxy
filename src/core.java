import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.*;

public class core extends JFrame {
    private JTextField hostField;
    private JTextField portField;
    private JButton connectButton;
    private JTextArea logArea;



    public core() {
        super("Proxy Connector");
        ImageIcon icon = new ImageIcon("proxy.png");
        setIconImage(icon.getImage());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel proxyPanel = createProxyPanel();
        tabbedPane.addTab("Proxy Settings", proxyPanel);

        JPanel logPanel = createLogPanel();
        tabbedPane.addTab("Log", logPanel);

        JPanel infoPanel = infopanel();
        tabbedPane.addTab("Info", infoPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createProxyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        hostField = new JTextField(20);
        portField = new JTextField(5);
        connectButton = new JButton("Connect");

        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isInternetAvailable()) {
                    logMessage("No internet connection available.");
                    return;
                }
                else{
                    logMessage("Internet connection available.");
                }

                String host = hostField.getText();
                int port = Integer.parseInt(portField.getText());

                System.setProperty("http.proxyHost", host);
                System.setProperty("http.proxyPort", Integer.toString(port));

                if (testProxyConnection(host, port)) {
                    logMessage("Proxy connection successful.");
                } else {
                    logMessage("Failed to connect through proxy.");
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Proxy Host:"));
        inputPanel.add(hostField);
        inputPanel.add(new JLabel("Proxy Port:"));
        inputPanel.add(portField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(connectButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLogPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    private JPanel infopanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        logArea = new JTextArea(20,40);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(5000); // Timeout in milliseconds
        } catch (IOException e) {
            return false;
        }
    }

    private boolean testProxyConnection(String host, int port) {
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            URL url = new URL("https://www.google.com");
            URLConnection connection = url.openConnection(proxy);
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void logMessage(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        logArea.append("[" + timestamp + "] " + message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new core().setVisible(true);
            }
        });
    }
}

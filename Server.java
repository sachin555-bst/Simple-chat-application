import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

 class Server extends  JFrame {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //DECLARE COMPONENTS
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    //constructor
    public Server() {

        try {
             server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("waiting......");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

 startReading();
//            startWriting();

            createGUI();
            handleEvent();
//

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("this is server.  going to start server");
        new Server();
    }

    private void handleEvent() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("key release" + e.getKeyCode());
                if (e.getKeyCode() == 10) {
//                    System.out.println("you have typed enter");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Server:" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });

    }

    private void createGUI() {
        //gui code
        this.setTitle("Server Messenger[END]");
        this.setSize(600, 600);  //DIMENSION OF THE GUI
        this.setLocationRelativeTo(null);  //TO CENTER THE GUI
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //TO CREATE A CROSS EXIT BUTTON

        //coding for components

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

//        heading.setIcon(new ImageIcon("clogo.png"));

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageArea.setEditable(false); // we can't edit the messages in the message input

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //setting the frame layout
        this.setLayout(new BorderLayout());

//adding the components to the frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);  //to add scroll bar
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);
        this.setVisible(true);

        //
    }

    public void startReading() {
        //thread --> read the data

        Runnable r1 = () -> {
            System.out.println("reader started.......");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Client terminated the chat");
                        socket.close();
                        break;
                    }
//                    System.out.println("Client:" + msg);
                    messageArea.append("Client:" + msg + "\n");

                }

            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("connection closed");

            }
        };
        new Thread(r1).start();

    }

    public void startWriting() {
//thread--> user get the data and send it to client or writing part
        System.out.println("writer started....");
        Runnable r2 = () -> {
            try {
                while (!socket.isClosed()) {


                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }

            } catch (Exception e) {
//        e.printStackTrace();

                System.out.println("connection closed");

            }
        };
        new Thread(r2).start();

    }


}


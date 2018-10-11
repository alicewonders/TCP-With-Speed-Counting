import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private static Integer BUF_SIZE = 64 * 1024;
    private static Socket socket = null;
    private static String filePath;
    private static String serverAddress;
    private int port;

    public Client(String filePath, String address, int port) {
        this.filePath = filePath;
        serverAddress = address;
        this.port = port;
        try {
            socket = new Socket(InetAddress.getByName(serverAddress), this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataOutputStream os;
        DataInputStream is;
        FileInputStream fis;
        try {
            Client client = new Client(args[0], args[1], Integer.valueOf(args[2]));
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
            File file = new File(filePath);
            if (!file.exists()) {
                os.writeUTF("Error");
            }
            else {
                os.writeUTF("OK");
            }

            os.writeUTF(file.getName());
            os.writeLong(file.length());

            fis = new FileInputStream(file);
            byte[] buf = new byte[BUF_SIZE];
            Integer bytesRead = 0;

            while ((bytesRead = fis.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }

            String message = is.readUTF();
            if (message.equals("Success")) {
                System.out.println("File " + file.getName() + " was sent");
            }
            else {
                System.out.println("Error!!!!!!!!!");
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

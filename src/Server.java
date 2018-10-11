import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private static Integer PORT = 52525;
    private static ServerSocket socket = null;

    private DataInputStream is = null;
    private DataOutputStream os = null;

    public Server(Integer port) {
        PORT = port;
        try  {
            socket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server is running ...");
    }

    @Override
    public void run() {
        Socket clientSocket;
        while (true) {
            try {
                clientSocket = socket.accept();
                System.out.println("New client is connected");
                Connection connection = new Connection(clientSocket);
                connection.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (PORT < 1 || PORT > 65535) {
            System.err.println("Invalid number of the port");
            System.exit(2);
        }
        Server server = new Server(PORT);
        server.start();
    }
}

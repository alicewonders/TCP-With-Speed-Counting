import javax.imageio.metadata.IIOMetadataNode;
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

public class Connection extends Thread {
    private static Integer BUF_SIZE = 64 * 1024;

    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;

    private static Integer currBytes = 0;
    private static boolean receivedStatus;
    private static Integer total = 0;
    private static Integer temp = 0;

    public Connection (Socket clientSocket) {
        socket = clientSocket;
        try {
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream((socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] buf = new byte[BUF_SIZE];
        FileOutputStream fos;
        Integer bytesReceived;
        Integer prevCurrBytes = 0;
        receivedStatus = true;

        try {
            String clientMessage = is.readUTF();
            if (clientMessage.equals("Error")) {
                System.err.println("Server error: Error while receiving the file");
                return;
            }
            else {
                clientMessage = is.readUTF();
                fos = new FileOutputStream("D:\\Documents\\IdeaProjects\\nets-lab2\\src\\main\\java\\upload\\" + clientMessage);
            }
            long fileSize = is.readLong();
            total = 0;

            new Timer(total, fileSize).start();
            while ((bytesReceived = is.read(buf)) > 0) {
                total += bytesReceived;
                currBytes = total - prevCurrBytes;
                fos.write(buf, 0, bytesReceived);
//                prevCurrBytes = total;
                if (total == fileSize) {
                    System.out.println("File " + clientMessage + " was received");
                    os.writeUTF("Success");
                    receivedStatus = false;
                    break;
                }
            }
            if (total != fileSize) {
                os.writeUTF("Bad");
            }

            if (!receivedStatus) {
                fos.flush();
                fos.close();
            }
            else {
                fos.close();
                File newFile = new File("D:\\Documents\\IdeaProjects\\nets-lab2\\src\\main\\java\\upload\\" + clientMessage);
                newFile.delete();
                receivedStatus = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getReceiveStatus() {
        return receivedStatus;
    }

    public static Integer getCurrBytes() {
        return currBytes;
    }

    public static Integer getTotal() {
        return total;
    }

    public static Integer getTemp() {
        return temp;
    }

    public static void setTemp(Integer temp) {
        Connection.temp = temp;
    }
}

import java.time.LocalTime;

public class Timer extends Thread {
    private static int INTERVAL = 1;
    private Integer receivedBytes;
    private long fileSize;
    private static long difference;

    public Timer(Integer currBytes, long fileSize) {
        receivedBytes = currBytes;
        this.fileSize = fileSize;
    }

    @Override
    public void run() {
        int i = 0;
        double instant = 0.0, session, temp = 0.0;
        Connection.setTemp(0);
        System.out.println("Timer is running");
//        long startPoint = System.nanoTime();
            while (Connection.getReceiveStatus()) {
                long startPoint = System.nanoTime();
                try {
                    Thread.sleep(1000);
                    i++;
                    if ((difference = (System.nanoTime() - startPoint)) % INTERVAL == 0)
                        instant = 1.0 * (Connection.getTotal() - Connection.getTemp()) / INTERVAL / 1024 / 1024;
                        temp += instant;
                        session = temp / i;
                        Connection.setTemp(Connection.getTotal());
                        System.out.format("Instant speed %.2f Mb/s\n", instant);
                        System.out.format("Session speed %.2f Mb/s\n\n", session);
                        difference = 0;
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
    }
}

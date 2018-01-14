package con.github.btnguyen2k.gearmanworker.qnd;

import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanJobReturn;

public class QndGearmanClient {

    static void qndSubmitJobSync(GearmanClient gearmanClient, String function)
            throws InterruptedException {
        System.out.println("Sending job [" + function + "] to " + gearmanClient.getServers());
        for (int i = 0; i < 5; i++) {
            String data = "SubmitJobSync " + i + ": " + System.currentTimeMillis();
            GearmanJobReturn jobReturn = gearmanClient.submitJob(function, data.getBytes());
            System.out.println("\t" + jobReturn);
            while (!jobReturn.isEOF()) {
                System.out.println("\t" + jobReturn.poll());
            }
        }
        System.out
                .println("Stopped sending job [" + function + "] to " + gearmanClient.getServers());
    }

    static void qndSubmitJobAsync(GearmanClient gearmanClient, String function)
            throws InterruptedException {
        System.out.println("Sending job [" + function + "] to " + gearmanClient.getServers());
        for (int i = 0; i < 5; i++) {
            String data = "SubmitJobAsync " + i + ": " + System.currentTimeMillis();
            GearmanJobReturn jobReturn = gearmanClient.submitBackgroundJob(function,
                    data.getBytes());
            System.out.println("\t" + jobReturn);
            while (!jobReturn.isEOF()) {
                System.out.println("\t" + jobReturn.poll());
            }
        }
        System.out
                .println("Stopped sending job [" + function + "] to " + gearmanClient.getServers());
    }

    public static void main(String[] args) throws Exception {
        Gearman gearman = Gearman.createGearman();
        {
            GearmanClient client = gearman.createGearmanClient();
            client.addServer(gearman.createGearmanServer("localhost", 4730));

            Thread.sleep(2000);
            qndSubmitJobSync(client, "demo");
            Thread.sleep(2000);

            client.shutdown();
        }

        {
            GearmanClient client = gearman.createGearmanClient();
            client.addServer(gearman.createGearmanServer("localhost", 4731));

            Thread.sleep(2000);
            qndSubmitJobAsync(client, "topic1");
            Thread.sleep(2000);
            client.shutdown();
        }

        gearman.shutdown();
    }
}

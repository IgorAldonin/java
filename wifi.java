import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class wifi  {
    public static void main (String[] args) throws IOException, InterruptedException {
        String line;
        ProcessBuilder  iwconf = new ProcessBuilder("iwconfig");
        Process pr1 = iwconf.start();
        BufferedReader in1 = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
        while ((line = in1.readLine()) != null) {
            if (line.trim().contains("wlan")) {
                System.out.println("Подключено устройство: "+line.trim().substring(0,7));
            };
        }
        while (true) {
            ProcessBuilder iwlist = new ProcessBuilder("iwlist", "wlan0", "scanning");
            Process pr2 = iwlist.start();
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
            System.out.println("Найденые сети Wi-Fi:");
            while ((line = in2.readLine()) != null) {
                if (line.trim().contains("ESSID:")) {
                    System.out.println(line.trim().substring(7, line.trim().length() - 1));
                }
                ;
            }
            Thread.sleep(60000);

        }


    }


}

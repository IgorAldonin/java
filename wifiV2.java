import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class wifiV2  {
    public static void main (String[] args) throws IOException, InterruptedException {
        String name_iw = check_iw();
        System.out.println("Подключено устройство: " + name_iw);
        String[][] name = check_wifi(name_iw);
        System.out.println("Найденные сети:");
        for (int i = 0; i < name.length; i++) {
            System.out.println(name[1][i]);
        }
        connect(name[][]);





    }
    public static String check_iw () throws IOException {
        String line;
        ProcessBuilder  iwconf = new ProcessBuilder("iwconfig");
        Process pr = iwconf.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = in.readLine()) != null) {
            line = line.trim();
            int x = line.indexOf("IEEE");
            if (x != -1) {
                line = line.substring(0,x ).trim();
                break;
            }
        }
        return (line);
    }
    public static String[][] check_wifi (String name) throws IOException {
        String line;
        String [][] wifi = new String[10][30];
        int i = 0;
        ProcessBuilder iwlist = new ProcessBuilder("iwlist",name,"scanning");
        Process pr = iwlist.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = in.readLine()) != null) {
            if (line.contains("Encryption key:")) {
                line = line.trim();
                wifi[0][i] = line.substring(15,line.length());
                line = in.readLine();
                line =line.trim();
                wifi[1][i] = line.substring(7,line.length()-1);
                i++;
            }

        }
        return (wifi);
    }
    public static void connect (String wifi[][]) throws IOException {
        for (int i = 0; i < wifi.length; i++) {
            if (wifi[1][i] == "off") {
                System.out.println("Подключение к сети: "+wifi[2][i]);
                ProcessBuilder  open = new ProcessBuilder("nmcli","device","wifi","connect",wifi[2][i]);
                System.out.println("Подключено к сети: "+wifi[2][i]);
            }
        }
    }
    public static void ping () throws IOException {
        String line;
        ProcessBuilder  ping = new ProcessBuilder("ping","8.8.8.8","-c","4");
        Process pr = ping.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        for (int i = 0; i<5; )line = in.readLine();
    }
}
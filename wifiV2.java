import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class wifiV2  {
    public static void main (String[] args) throws IOException{
        String name_iw = check_iw();
        System.out.println("Подключено устройство: " + name_iw);
        String[][] name = check_wifi(name_iw);
        System.out.println("Найденные сети:");
        int i = 0;
       while (name[0][i] != null){
            System.out.println(name[0][i] + name[1][i]);
            i++;
        }
        if (!(connect_open(name))) connect_private(name);
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
    public static boolean connect_open (String[][] wifi) throws IOException {
        Boolean a = false;
        int i = 0;
        while (wifi[0][i] != null){
            if (wifi[0][i].contains("off")) {
                System.out.println("Подключение к сети: " + wifi[1][i]);
                try {
                    ProcessBuilder open = new ProcessBuilder("nmcli", "device", "wifi", "connect", wifi[1][i]);
                    Process pr = open.start();
                    try {
                        pr.waitFor();
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Подключено к сети: " + wifi[1][i]);
                } catch (Exception e) {
                    System.out.println("Не удалось подключится к сети: " + wifi[1][i]);
                    continue;
                }
                System.out.println("Проверка наличия сети");
                if (ping()) {
                    System.out.println("Соединение установлено");
                     a = true;
                    break;}
                }
            i++;
            }
        return a;
        }
    public static void connect_private (String[][] wifi) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String password;
        for (int i = 0; i < wifi.length; i++) {
            if (wifi[0][i].contains("on")) {
                System.out.println("Подключение к сети: " + wifi[1][i]);
                System.out.println("Введите пароль:");
                password = scanner.nextLine();
                try {
                    ProcessBuilder  close = new ProcessBuilder("nmcli","device","wifi","connect",wifi[1][i],"password",password);
                    Process pr = close.start();
                    try {
                        pr.waitFor();
                    } catch (InterruptedException e) {
                    }
                    System.out.println("Подключено к сети: " + wifi[1][i]);
                } catch (Exception e) {
                    System.out.println("Не удалось подключится к сети: " + wifi[1][i]);
                    continue;
                }
                System.out.println("Проверка наличия сети");
                if (ping()) {
                    System.out.println("Соединение установлено");
                    break;}
            }
        }



    }
    public static boolean ping () throws IOException {
        String line = new String();
        ProcessBuilder ping = new ProcessBuilder("ping", "8.8.8.8", "-c", "4", "-q");
        Process pr = ping.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        for (int i = 0; i < 4; i++) {
            line = in.readLine();

        }
        try {
            if (line.contains("100%")) {

                System.out.println("Нет доступа в интернет");
                return false;
            } else {
                System.out.println("Есть доступ в интернет");
                return true;
            }
        } catch (Exception e) {return false;}
    }
}
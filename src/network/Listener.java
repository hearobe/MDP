package network;

import java.util.ArrayList;

public class Listener extends Thread {
    private NetworkMain rpi;

    Listener(NetworkMain rpi) {
        this.rpi = rpi;
    }

    public void run() {
        ArrayList<String> buffer = new ArrayList<>();

        while (true) {
            String s = rpi.receiveMessage();
            buffer.add(s);
            System.out.println(s);
        }
    }
}

package network;

import algorithm.Arena;
import algorithm.Direction;
import algorithm.PathSequencer;
import car.Coordinate;

import java.net.Socket;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class NetworkMain {
    private String ip = "0.0.0.0";//"192.168.4.4";
    private int port = 51043;
    private String name;

    private static final Logger LOGGER = Logger.getLogger(NetworkMain.class.getName());


    public static Socket socket = null;


    private static BufferedWriter out;
    private static BufferedReader in;
    private InputStream in1;

    public NetworkMain(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
    }

    public void connect() throws Exception {
        if (socket == null) {
            LOGGER.info("Initiating Connection with "+name+"...");
            socket = new Socket(ip, port);
            if(socket == null) {
                LOGGER.info("Failed to create socket.\n");
            }
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            in1 = socket.getInputStream();

            LOGGER.info("Connection with "+name+" established! " + socket);
            return;
        } else {
            LOGGER.info("Already connected with "+name+". " + socket);
            return;
        }
    }

    public void sendMessage(String message) {
        if(socket == null) {
            LOGGER.warning("Not connected to "+name);
            return;
        } else {
            try {
                LOGGER.info("Sending message to "+name+"...");
                out.write(message);
                out.newLine();
                out.flush();
                LOGGER.info("Message sent: " + message);
                return;
            } catch (Exception e) {
                LOGGER.info("Sending Message Failed.\n" + e.toString());
                e.printStackTrace();
                return;
            }
        }
    }

    public String receiveImage() {
        while(true){
            try{
                byte[] sizeAr = new byte[4];
                in1.read(sizeAr);
                int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                byte[] imageAr = new byte[size];
                in1.read(imageAr);

                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
                ImageIO.write(image, "jpg", new File("takenImage.jpg"));
                Thread.sleep(3000);
                String ID = readFile();
                return ID;
            }catch(Exception ex){
                System.out.println("Not connected.\n");
            }
        }
    }

    public String readFile() {
        try {
            File myObj = new File("takenID.txt");
            Scanner myReader = new Scanner(myObj);
            String data = myReader.nextLine();
            myReader.close();
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    // TODO: create thread to listen for messages
    public String receiveMessage() {
        if(socket == null) {
            LOGGER.warning("Not connected to "+name);
            return null;
        } else {
            try {
                LOGGER.info("Receiving message from "+name+"...");
                String message = null;
                while(message == null || message.isEmpty()) {
                    message = in.readLine();
                    if (message != null && !message.isEmpty()) {
                        LOGGER.info("Message received: " + message);
                        return message;
                    }
                }
            } catch (Exception e) {
                LOGGER.info("Receiving Message Failed.\n" + e.toString());
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static void disconnect() {
        LOGGER.info("Disconnecting...");
        if(socket == null) {
            LOGGER.warning("Not connected");
            return;
        }
        else {
            try {
                socket.close();
                out.close();
                in.close();
                socket = null;
                return;
            } catch (Exception e) {
                LOGGER.warning("Disconnecting failed");
                e.printStackTrace();
                return;
            }
        }
    }

    public static void main(String[] args) {
        NetworkMain test2 = new NetworkMain("192.168.4.4", 4334, "RPI");
        try {
            test2.connect();
        } catch (UnknownHostException e) {
            LOGGER.warning("Connection Failed: UnknownHostException\n" + e.toString());
            return;
        } catch (IOException e) {
            LOGGER.warning("Connection Failed: IOException\n" + e.toString());
            return;
        } catch (Exception e) {
            LOGGER.warning("Connection Failed!\n" + e.toString());
            e.printStackTrace();
            return;
        }

//        test2.sendMessage("Hello");
        String androidInput = test2.receiveMessage();

        // should refactor this part, but I'm lazy
        String[] locations = androidInput.split(",!");
        String[] curLocation;
        int[] x = new int[locations.length-1], y = new int[locations.length-1];
        Direction[] dir = new Direction[locations.length-1];

        curLocation = locations[0].split(",");
        Coordinate start = new Coordinate(Integer.parseInt(curLocation[0]), Integer.parseInt(curLocation[1]), Direction.parseDir(curLocation[2]));
        for (int i = 1; i < locations.length; i++) {
//            System.out.println(locations[i]);
            curLocation = locations[i].split(",");
//            System.out.println(Arrays.toString(curLocation));
            x[i-1] = Integer.parseInt(curLocation[0]);
            y[i-1] = Integer.parseInt(curLocation[1]);
            dir[i-1] = Direction.parseDir(curLocation[2]);
        }
//        System.out.println(Arrays.toString(dir));

//        int[] x = new int[] {3, 10, 17, 7, 15};
//        int[] y = new int[] {14, 9, 7, 1, 15};
//        Direction[] dir = new Direction[] {DOWN, DOWN, LEFT, UP,LEFT};
//        CarCoordinate start = new CarCoordinate(1,1,UP);

//        int[] x = new int[] {6,11, 18};
//        int[] y = new int[] {9,5, 18};
//        Direction[] dir = new Direction[] {DOWN, LEFT, LEFT};
//        CarCoordinate start = new CarCoordinate(1,1,UP);

        Arena arena = new Arena();
        arena.setObstacles(x, y, dir);
        PathSequencer pathSequencer = new PathSequencer(arena, start);
        System.out.println(pathSequencer.getSTMPath());

        try {
            test2.sendMessage(pathSequencer.getAndroidOrder());

            test2.sendMessage(pathSequencer.getSTMPath());
        } catch (NullPointerException e) {
            LOGGER.warning("No path found");
        }

        test2.disconnect();
    }
}

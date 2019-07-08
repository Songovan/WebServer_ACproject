package WebServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    //properties


    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private Thread thread;
    private int threadCounter;
    private ExecutorService cachedPool = Executors.newCachedThreadPool();

    //constructor
    public WebServer(int portNumber) {

        try {
            start(portNumber);
        } catch (UnknownHostException unEx) {
            System.out.println("Unknown Host Exception");
        } catch (IOException ioEx) {
            System.out.println("IO Exception");
        }
    }


    //start method
    public void start(int portNumber) throws IOException {

        serverSocket = new ServerSocket(portNumber);

        while (true) {

            System.out.println("Listening");
            socket = serverSocket.accept();
            System.out.println("Connected");
            out = new PrintWriter(socket.getOutputStream(), true);
            cachedPool.submit(new ClientDispatcher(socket,out));

        }
    }
}

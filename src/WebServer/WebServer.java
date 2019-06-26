package WebServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebServer {

    //properties
    private String fileName = "./www/index.html";
    private String errorMSG = "./www/errorMSG.html";
    private String imgOne = "./resources/336fri.jpg";
    private File file;
    private String line;
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader bReader;
    private BufferedReader exitBuffer;
    private String htmlVerifier = "GET / HTTP/1.1";
    private String imageVerifier = "GET /imagem HTTP/1.1";
    private String requestHeaderLine;

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
            getRequest();
            exitBuffer = new BufferedReader(new InputStreamReader(System.in));

            if (exitBuffer.readLine().equals("quit")) {
                System.exit(0);
            }
        }
    }

    //getRequest method
    public void getRequest() throws IOException {

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(requestHeaderLine = in.readLine());

        if (requestHeaderLine.equals(htmlVerifier)) {
            sendHTML();

        } else if (requestHeaderLine.equals(imageVerifier)) {
            sendIMG();
        } else {
            sendErr();
        }
    }

    public void sendHTML() throws IOException {

        bReader = new BufferedReader(new FileReader(fileName));
        out.println("HTTP/1.0 200 Document Follows\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: <file_byte_size> \r\n" +
                "\r\n");

        while ((line = bReader.readLine()) != null) {
            out.println(line);
        }
    }

    public void sendErr() throws IOException {

        bReader = new BufferedReader(new FileReader(errorMSG));
        out.println("HTTP/1.0 404 Not Found\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: <file_byte_size> \r\n" +
                "\r\n");

        while ((line = bReader.readLine()) != null) {
            out.println(line);
        }
    }

    public void sendIMG() throws IOException {
        file = new File(imgOne);
        out.println("HTTP/1.0 200 Document Follows\r\n" +
                "Content-Type: image/.jpg \r\n" +
                "Content-Length: <file_byte_size> \r\n" +
                "\r\n");

            out.println(file);
    }
}

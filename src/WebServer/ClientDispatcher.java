package WebServer;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

public class ClientDispatcher implements  Runnable {

    private BufferedReader bReader;
    private PrintWriter out;
    private Socket socket;
    private String fileName = "./www/file.html";
    private String errorMSG = "./www/errorMSG.html";
    private String imgOne = "./resources/336fri.jpg";
    private String htmlVerifier = "GET / HTTP/1.1";
    private String imageVerifier = "GET /imagem HTTP/1.1";
    private BufferedReader in;
    private String requestHeaderLine;
    private String line = "";
    private File htmlFile = new File(fileName);
    private File errorMessageFile = new File(errorMSG);

    public ClientDispatcher (Socket socket, PrintWriter out) {
        this.out = out;
        this.socket = socket;
    }

    @Override
    public void run () {
         try {
            getRequest();
        } catch (UnknownHostException unEx) {
            System.out.println("Unknown Host Exception");
        } catch (IOException ioEx) {
            System.out.println("IO Exception");
        }

    }

    //getRequest method
    public void getRequest() throws IOException {

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(requestHeaderLine = in.readLine());

        if (requestHeaderLine.equals(htmlVerifier) && requestHeaderLine!= null) {
            sendHTML();
        } else {
            sendErr();
        }
    }

    public void sendHTML() throws IOException {

        bReader = new BufferedReader(new FileReader(fileName));
        out.println("HTTP/1.0 200 Document Follows\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + htmlFile.length() + " \r\n" +
                "\r\n");

        while ((line = bReader.readLine()) != null) {
            out.println(line);
        }
    }

    public void sendErr() throws IOException {

        bReader = new BufferedReader(new FileReader(errorMSG));
        out.println("HTTP/1.0 404 Not Found\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + errorMessageFile.length() + " \r\n" +
                "\r\n");

        while ((line = bReader.readLine()) != null) {
            out.println(line);
        }
    }
}

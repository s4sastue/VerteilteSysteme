package ueb1.common.comunication;

import java.io.*;
import java.net.Socket;
import ueb1.common.message.Message;

public class Connection{
    ObjectOutputStream out;
    ObjectInputStream in;

    public Connection(String server, int port) {
        try {
            Socket sock = new Socket(server, port);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
        } catch (Exception e) {
            System.err.println("connect: "+e.getMessage());
        }
    }

    public Connection(Socket sock) {
        try {
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
        } catch (Exception e) {
            System.err.println("connect: "+e.getMessage());
        }
    }


    public Message receive() {
        Message message = null;

        try {
            message = (Message) in.readObject();
        } catch (Exception e) {
            System.err.println("receive: "+e.getMessage());
        }
        return message;
    }


    public void send(Message message) {
        try {
            out.writeObject(message);

        } catch (Exception e) {
            System.err.println("send: "+e.getMessage());
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println("close: "+e.getMessage());
        }
    }
}
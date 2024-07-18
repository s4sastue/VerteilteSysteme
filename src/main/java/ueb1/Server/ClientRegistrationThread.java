package ueb1.Server;

import ueb1.common.message.InitializationMessage;

import java.net.ServerSocket;
import java.net.Socket;

public class ClientRegistrationThread extends Thread{
    Server parent;
    ClientRegistrationThread(Server parent){
        this.parent = parent;
    }

    public void run() {
        try {
            ServerSocket ss = new ServerSocket(parent.getServerPort());
            while (!isInterrupted()) {
                Socket client = ss.accept();
                ClientConnectionThread ct = new ClientConnectionThread(parent, client);
                ct.start();
                ct.con.send(new InitializationMessage(parent.getClockType(), parent.incrementClock()));

                try {
                    parent.getClientListLock().acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                parent.addClient(ct);
                parent.getClientListLock().release();

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

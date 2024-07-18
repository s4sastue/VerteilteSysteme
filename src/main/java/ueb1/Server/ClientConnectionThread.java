package ueb1.Server;

import ueb1.common.comunication.Connection;

import java.net.Socket;
import ueb1.common.message.Message;
import ueb1.common.message.ResultMessage;

public class ClientConnectionThread extends Thread {
    Server parent;
    Socket clientSocket;
    Connection con;

    ClientConnectionThread(Server parent, Socket clientSocket){
        this.parent = parent;
        this.clientSocket = clientSocket;

        con = new Connection(clientSocket);
    }

    public void run() {
        Message message;
        while((message = con.receive()) != null) {
            if(message instanceof ResultMessage resultMessage){
                parent.incrementClock(resultMessage.getTimestamp());
                Result result = new Result(resultMessage, System.currentTimeMillis());

                try {
                    parent.getGameHostThread().getIsRoundRunningLock().acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (parent.getGameHostThread().isRoundRunning()){
                    parent.getGameHostThread().addResultToList(result);
                }
                parent.getGameHostThread().getIsRoundRunningLock().release();


            }
        }
        con.close();
    }
}
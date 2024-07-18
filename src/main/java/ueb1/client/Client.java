package ueb1.client;

import ueb1.common.clock.Clock;
import ueb1.common.clock.LamportClock;
import ueb1.common.clock.Timestamp;
import ueb1.common.comunication.Connection;
import ueb1.common.message.*;

import java.util.Random;

public class Client extends Thread{

    static int minNumber = 0;
    static int maxNubmer = 100;

    Clock clock;
    String name;
    Connection connection;

    int latency;

    public Client(String name, String serverAdress, int serverPort, int latency){
        this.name = name;
        this.connection = new Connection(serverAdress, serverPort);
        this.latency = latency;
    }

    public void run() {
        Message message;

        while ((message = connection.receive()) != null) {

            if(message instanceof StartMessage startMessage){
                if(clock != null){
                    clock.increment(startMessage.getTimestamp());
                }
                int result = new Random().nextInt(Client.maxNubmer-Client.minNumber) + Client.minNumber;

                latency();
                sendResult(result);

            }

            if(message instanceof StopMessage stopMessage){
                if(clock != null){
                    clock.increment(stopMessage.getTimestamp());
                }
            }

            if(message instanceof InitializationMessage initializationMessage){
                if(initializationMessage.getClockType() == null){
                    clock = null;
                } else if (initializationMessage.getClockType() == LamportClock.class) {
                    clock = new LamportClock();
                    clock.increment(initializationMessage.getTimestamp());
                    System.out.println(clock.getTimestamp());
                }
            }
        }
    }

    public void latency(){
        try {
            Thread.sleep(new Random().nextInt(latency));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendResult(int result){
        Timestamp timestamp = null;
        if(clock != null){
            timestamp = clock.increment();
        }
        Message message = new ResultMessage(timestamp, name, result);
        connection.send(message);

    }

    public static void main(String[] args) {
        Client c = new Client("test", "127.0.0.1", 44444, 10000);
        c.start();
    }
}

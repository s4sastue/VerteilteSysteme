package ueb1.Server;

import ueb1.common.clock.Clock;
import ueb1.common.clock.LamportClock;

import ueb1.common.clock.Timestamp;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Server{
    private final String filenameResultTable;
    private final GameHostThread gameHostThread;
    private final ClientRegistrationThread clientRegistrationThread;
    private Clock clock;
    private final int serverPort;
    private final int durationRound;
    LinkedList<ClientConnectionThread> clients = new LinkedList<>();

    private final Semaphore clientListLock = new Semaphore(1);

    public Server(String filenameResultTable, int serverPort, int durationRound){
        this.filenameResultTable = filenameResultTable;
        this.serverPort = serverPort;
        this.durationRound = durationRound;

        clientRegistrationThread = new ClientRegistrationThread(this);
        clientRegistrationThread.start();

        gameHostThread = new GameHostThread(this);
    }

    public String getFilenameResultTable() {
        return filenameResultTable;
    }

    public void startGame(){
        gameHostThread.start();
    }
    public GameHostThread getGameHostThread() {
        return gameHostThread;
    }

    public ClientRegistrationThread getClientRegistrationThread() {
        return clientRegistrationThread;
    }

    public LinkedList<ClientConnectionThread> getClients() {
        return clients;
    }
    public int getServerPort() {
        return serverPort;
    }
    public void addClient(ClientConnectionThread client){
        clients.add(client);
    }
    public void setClockToLamport(){
        clock = new LamportClock();
    }
    public Class getClockType(){
        return (clock == null)
                ? null
                : clock.getClass();
    }
    public Timestamp incrementClock(){
        return (clock == null)
                ? null
                : clock.increment();
    }
    public Timestamp incrementClock(Timestamp otherTimestamp){
        return (clock == null)
                ? null
                : clock.increment(otherTimestamp);
    }
    public Timestamp getCurrentTimestamp(){
        return (clock == null)
                ? null
                : clock.getTimestamp();
    }
    public int getDurationRound() {
        return durationRound;
    }

    public Semaphore getClientListLock() {
        return clientListLock;
    }
}
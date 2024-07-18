package ueb1.Server;

import ueb1.common.clock.Timestamp;
import ueb1.common.message.Message;
import ueb1.common.message.StartMessage;
import ueb1.common.message.StopMessage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.Semaphore;

public class GameHostThread extends Thread{
    Server parent;
    private Timestamp startRoundTimestamp;

    private boolean isRoundRunning;
    private final Semaphore isRoundRunningLock = new Semaphore(1);

    private List<Result> resultList;
    GameHostThread(Server parent){
        this.parent = parent;
    }

    public void run() {
        try(FileWriter fw = new FileWriter(parent.getFilenameResultTable(), false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println("roundNumber;roundTimestamp;timestampString;reciveTime;latency;clientName;result;isWinningRoll;isVaild;comment");
        } catch (IOException ignore) {}

        int roundNumber = 1;
        while (!isInterrupted()) {
            resetResultList();

            if(parent.getClockType() != null){
                setStartRoundTimestamp();
                System.out.println("new Round Timestamp: " + getStartRoundTimestamp().getValue());
            }else{
                System.out.println("new Round");
            }

            long startTime = System.currentTimeMillis();


            try {
                isRoundRunningLock.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isRoundRunning = true;
            isRoundRunningLock.release();


            try {
                parent.getClientListLock().acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for(ClientConnectionThread ct: parent.getClients()) {
                Message message = new StartMessage(parent.incrementClock());
                ct.con.send(message);
            }
            parent.getClientListLock().release();

            latency();

            try {
                isRoundRunningLock.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isRoundRunning = false;
            isRoundRunningLock.release();

            try {
                parent.getClientListLock().acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for(ClientConnectionThread ct: parent.getClients()) {
                Message message = new StopMessage(parent.incrementClock());
                ct.con.send(message);
            }
            parent.getClientListLock().release();

            Result winningResult = null;
            Set<String> namesList = new HashSet<>();
            Collections.reverse(resultList);
            for(Result res: resultList){
                res.roundTimestamp = getStartRoundTimestamp();
                if (namesList.contains(res.clientName)){
                    res.isVaild = false;
                    res.comment = "Roll does not belong to this round, same client has registered at least one later roll for this round";
                }else{
                    namesList.add(res.clientName);
                }

                res.latency = res.reciveTime - startTime;
                if(parent.getClockType() != null && !(res.timestamp.isCausallyDependentOn(getStartRoundTimestamp()))){
                    res.isVaild = false;
                    res.comment = "Roll definitely does not belong to this round, exclusion due to causal connection";
                }

                if(winningResult == null){
                    winningResult = res;
                }

                if(winningResult.result <= res.result){
                    winningResult = res;
                }
            }

            if(winningResult != null){
                winningResult.isWinningRoll = true;
            }

            Collections.reverse(resultList);
            try(FileWriter fw = new FileWriter(parent.getFilenameResultTable(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
                for(Result res: resultList){
                    out.println(roundNumber + ";" + res);
                }

            } catch (IOException ignore) {}

            roundNumber ++;
        }
    }


    public void latency(){
        try {
            Thread.sleep(parent.getDurationRound());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStartRoundTimestamp() {
        this.startRoundTimestamp = parent.getCurrentTimestamp();
    }
    public Timestamp getStartRoundTimestamp(){
        return startRoundTimestamp;
    }

    public void resetResultList(){
        resultList = new ArrayList<>();
    }

    public void addResultToList(Result r){
        resultList.add(r);
    }

    public Semaphore getIsRoundRunningLock() {
        return isRoundRunningLock;
    }

    public boolean isRoundRunning() {
        return isRoundRunning;
    }
}
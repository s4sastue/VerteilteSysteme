package ueb1.Server;

import ueb1.common.clock.Timestamp;
import ueb1.common.message.ResultMessage;

public class Result{
    Timestamp timestamp;
    Timestamp roundTimestamp;
    String clientName;
    int result;
    String comment;
    boolean isWinningRoll = false;
    boolean isVaild = true;
    long reciveTime;
    long latency;

    Result(ResultMessage resultMessage, long reciveTime){
        this.timestamp = resultMessage.getTimestamp();
        this.clientName = resultMessage.getClientName();
        this.result = resultMessage.getResult();
        this.reciveTime = reciveTime;
    }

    @Override
    public String toString() {

        String timestampString = (timestamp == null)
                ? null
                : timestamp.getValue().toString();

        String roundTimestampString = (roundTimestamp == null)
                ? null
                : roundTimestamp.getValue().toString();

        return roundTimestampString + ";" + timestampString + ";" + reciveTime + ";" + latency + ";" + clientName + ";" + result + ";" + isWinningRoll + ";" + isVaild + ";" + comment;
    }
}

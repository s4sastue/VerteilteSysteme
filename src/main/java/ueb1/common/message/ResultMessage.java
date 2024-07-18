package ueb1.common.message;

import ueb1.common.clock.Timestamp;

public class ResultMessage implements Message{
    private Timestamp timestamp;

    private String clientName;

    private Integer result;

    public ResultMessage(Timestamp timestamp, String clientName, Integer result){
        this.timestamp = timestamp;
        this.clientName = clientName;
        this.result = result;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getClientName() {
        return clientName;
    }

    public Integer getResult() {
        return result;
    }
}

package ueb1.common.message;

import ueb1.common.clock.Timestamp;

public class StopMessage implements Message{
    private Timestamp timestamp;

    public StopMessage(Timestamp timestamp){
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}

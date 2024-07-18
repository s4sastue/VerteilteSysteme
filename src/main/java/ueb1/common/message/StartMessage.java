package ueb1.common.message;

import ueb1.common.clock.Timestamp;

public class StartMessage implements Message{
    private Timestamp timestamp;

    public StartMessage(Timestamp timestamp){
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}

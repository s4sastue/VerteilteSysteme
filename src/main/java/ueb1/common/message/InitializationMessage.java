package ueb1.common.message;

import ueb1.common.clock.Timestamp;

public class InitializationMessage implements Message{
    private Class clockType;
    private Timestamp timestamp;

    public InitializationMessage(Class clockType, Timestamp timestamp){
        this.clockType = clockType;
        this.timestamp = timestamp;
    }

    public Class getClockType() {
        return clockType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}

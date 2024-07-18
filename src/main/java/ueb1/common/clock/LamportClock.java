package ueb1.common.clock;

public class LamportClock implements Clock{
    Timestamp currentTimestamp = new LamportTimestamp();

    public LamportClock(){}

    @Override
    public Timestamp getTimestamp() {
        return currentTimestamp.copy();
    }

    @Override
    public Timestamp increment() {
        return currentTimestamp.increment();
    }

    @Override
    public Timestamp increment(Timestamp otherTimestamp) {
        return currentTimestamp.increment(otherTimestamp);
    }
}

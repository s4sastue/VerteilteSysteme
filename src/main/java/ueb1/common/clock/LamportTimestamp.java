package ueb1.common.clock;

public class LamportTimestamp implements Timestamp {
    private long value = -1;

    public LamportTimestamp(){
        value = 0;
    }

    private LamportTimestamp(long value){
        this.value = value;
    }

    @Override
    public synchronized Long getValue() {
        return value;
    }

    @Override
    public synchronized Timestamp increment() {
        value += 1;
        return new LamportTimestamp(value);
    }

    @Override
    public synchronized Timestamp increment(Timestamp otherTimestamp) {
        if(otherTimestamp instanceof LamportTimestamp otherLamportTimestamp){
            value = Long.max(value, otherLamportTimestamp.value) + 1;
        }

        return new LamportTimestamp(value);
    }

    @Override
    public boolean isCausallyDependentOn(Timestamp otherTimestamp) {
        assert otherTimestamp instanceof LamportTimestamp;

        var otherLamportTimestamp = (LamportTimestamp) otherTimestamp;

        return otherLamportTimestamp.value < this.value;
    }

    @Override
    public synchronized Timestamp copy() {
        return new LamportTimestamp(value);
    }
}

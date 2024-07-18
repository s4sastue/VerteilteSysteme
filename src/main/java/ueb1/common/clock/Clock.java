package ueb1.common.clock;

public interface Clock {
    Timestamp getTimestamp();

    Timestamp increment();

    Timestamp increment(Timestamp otherTimestamp);
}

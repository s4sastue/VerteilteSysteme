package ueb1.common.clock;

import java.io.Serializable;

public interface Timestamp extends Serializable {

    Object getValue();

    Timestamp increment();

    Timestamp increment(Timestamp otherTimestamp);

    boolean isCausallyDependentOn(Timestamp otherTimestamp);

    Timestamp copy();
}

package tokenRing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Package {
    private final String message;
    private final Integer from;
    private final Integer where;
    private final Long timeSent; //in order to measure latency, I decide to track sending time of the package

}

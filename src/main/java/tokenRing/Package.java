package tokenRing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Package {
    private final String message;
    private final Integer from;
    private final Integer where;
    private Long timeSent; //in order to measure latency, I decide to track sending time of the package

}

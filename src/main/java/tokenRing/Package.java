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

}

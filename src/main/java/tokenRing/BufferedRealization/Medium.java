package tokenRing.BufferedRealization;

import tokenRing.Package;

public interface Medium {
    public void accept(Package data);
    public Package get() throws InterruptedException;
}

package tokenRing.BufferedRealization;

import tokenRing.Package;

public interface Node extends Runnable {
    public void push(Package pack);
    public void pull();
}

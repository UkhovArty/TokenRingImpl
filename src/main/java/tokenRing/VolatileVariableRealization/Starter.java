package tokenRing.VolatileVariableRealization;

import tokenRing.Package;

public class Starter {
    public static void main(String[] args) {
        Ring someRing  = new Ring(9, "initial");
        someRing.sendMessage(new Package("from 0 to 1",0,9, System.currentTimeMillis()));
    }
}

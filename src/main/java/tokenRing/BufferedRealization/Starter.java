package tokenRing.BufferedRealization;

import tokenRing.Package;

public class Starter {
    public static void main(String[] args) {
        Medium medium1 = new BlockingQueueMedium();
        Medium medium2 = new BlockingQueueMedium();
        NodeImpl node1 = new NodeImpl(0, medium2, medium1);
        NodeImpl node2 = new NodeImpl(1, medium1, medium2);
        new Thread(node1).start();
        new Thread(node2).start();
        System.out.println("here");
        medium1.accept(new Package("msg", 0, 1, System.nanoTime()));
    }
}

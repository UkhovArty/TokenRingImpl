package tokenRing;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

class LatencyTest {
    private Ring someRing = new Ring(5, "initial");

    // I will start my tests with Loading TokenRing with InitialNodes (this nodes are made to be able to "consume" packages,
    // that were sent for them. They are also configured to measure latency.
    @Test
    void FullyLoadTestForInitialNodes() throws InterruptedException {
        someRing.sendMessage(new Package("from 0 to 1", 0, 1));
        someRing.sendMessage(new Package("from 1 to 2", 1, 2));
        someRing.sendMessage(new Package("from 2 to 3", 2, 3));
        someRing.sendMessage(new Package("from 3 to 4", 3, 4));
        someRing.sendMessage(new Package("from 4 to 5", 4, 0));
        Thread.sleep(10000);
        //With messages sent from every node system struggles to deliver them to the destination points. the process will never end
    }

    // 5 nodes system latency were measured for different amount of messages sent from different nodes almost at one time:
    @Test
    void LoadTestForDifferentAmountOfMessages() throws InterruptedException {
        HashMap<Integer, Long> latenciesForNodes = new HashMap<>();
        Long markers = 0L;
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2));
            someRing.sendMessage(new Package("from 2 to 3", 2, 3));
            someRing.sendMessage(new Package("from 3 to 4", 3, 4));
            Thread.sleep(1000);
            markers += someRing.getAverageLatencyMarker();
        }
        latenciesForNodes.put(4, markers / 20);
        markers = 0L;
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2));
            someRing.sendMessage(new Package("from 4 to 2", 2, 3));
            Thread.sleep(1000);
            markers += someRing.getAverageLatencyMarker();
        }
        latenciesForNodes.put(3, markers / 20);
        markers = 0L;
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2));
            Thread.sleep(1000);
            markers += someRing.getAverageLatencyMarker();
        }
        latenciesForNodes.put(2, markers / 20);
        markers = 0L;
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1));
            Thread.sleep(1000);
            markers += someRing.getAverageLatencyMarker();
        }
        latenciesForNodes.put(1, markers / 20);
        System.out.println(
                "4: " + latenciesForNodes.get(4)
                        + " 3: " + latenciesForNodes.get(3)
                        + " 2: " + latenciesForNodes.get(2)
                        + " 1: " + latenciesForNodes.get(1)
        );
    }

    // Next test is made to investigate dependency of latency from amount of nodes in system, throwing only one message.
    // 2 nodes: 1256 3 nodes: 859 4 nodes: 653 5 nodes: 605 6 nodes: 578 7 nodes: 500 8 nodes: 395 9 nodes: 318
    @Test
    void changeAmountOfNodesWithConstantAmountOfMessages() throws InterruptedException {
        Ring ring9 = new Ring(9, "initial");
        Ring ring8 = new Ring(8, "initial");
        Ring ring7 = new Ring(7, "initial");
        Ring ring6 = new Ring(6, "initial");
        Ring ring5 = new Ring(5, "initial");
        Ring ring4 = new Ring(4, "initial");
        Ring ring3 = new Ring(3, "initial");
        Ring ring2 = new Ring(2, "initial");
        ring2.sendMessage(new Package("from 0 to 1", 0, 1));
        ring3.sendMessage(new Package("from 0 to 1", 0, 2));
        ring4.sendMessage(new Package("from 0 to 1", 0, 3));
        ring5.sendMessage(new Package("from 0 to 1", 0, 4));
        ring6.sendMessage(new Package("from 0 to 1", 0, 5));
        ring7.sendMessage(new Package("from 0 to 1", 0, 6));
        ring8.sendMessage(new Package("from 0 to 1", 0, 7));
        ring9.sendMessage(new Package("from 0 to 1", 0, 8));

        Thread.sleep(10000);
        System.out.println(
                "2 nodes: " + ring2.getNodeLatencyMarker(1)
                        + " 3 nodes: " + ring3.getNodeLatencyMarker(2)
                        + " 4 nodes: " + ring4.getNodeLatencyMarker(3)
                        + " 5 nodes: " + ring5.getNodeLatencyMarker(4)
                        + " 6 nodes: " + ring6.getNodeLatencyMarker(5)
                        + " 7 nodes: " + ring7.getNodeLatencyMarker(6)
                        + " 8 nodes: " + ring8.getNodeLatencyMarker(7)
                        + " 9 nodes: " + ring9.getNodeLatencyMarker(8)
        );
    }
}
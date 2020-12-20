package tokenRing.BufferedRealization;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tokenRing.Package;

import java.util.ArrayList;
import java.util.List;

class BufferedRingTest {

    private static BufferedRing someRing = new BufferedRing(5);

    //This is the warm up
    @BeforeAll
    static void setUp() throws InterruptedException {
        BufferedRing someOtherRing = new BufferedRing(2);
        for (int i = 0; i < 10000; i++) {
            Thread.sleep(10);
            someOtherRing.sendMessage(new Package("from 0 to 1", 0, 1, System.currentTimeMillis()));
        }
    }

    // I will start my tests with Loading TokenRing with InitialNodes (this nodes are made to be able to "consume" packages,
    // that were sent for them. They are also configured to measure latency.
    @Test
    void FullyLoadTestForInitialNodes() throws InterruptedException {
        someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.currentTimeMillis()));
        someRing.sendMessage(new Package("from 1 to 2", 1, 2, System.currentTimeMillis()));
        someRing.sendMessage(new Package("from 2 to 3", 2, 3, System.currentTimeMillis()));
        someRing.sendMessage(new Package("from 3 to 4", 3, 4, System.currentTimeMillis()));
        someRing.sendMessage(new Package("from 4 to 5", 4, 0, System.currentTimeMillis()));
        Thread.sleep(10000);

        //With messages sent from every node system struggles to deliver them to the destination points. the process will never end
    }

    @Test
    void LoadTestForDifferentAmountOfMessages() throws InterruptedException {
        List<Long> markers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.currentTimeMillis()));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2, System.currentTimeMillis()));
            someRing.sendMessage(new Package("from 2 to 3", 2, 3, System.currentTimeMillis()));
            someRing.sendMessage(new Package("from 3 to 4", 3, 4, System.currentTimeMillis()));
            Thread.sleep(1000);
            markers.addAll(someRing.getLatencies());
        }
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.currentTimeMillis()));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2, System.currentTimeMillis()));
            someRing.sendMessage(new Package("from 4 to 2", 2, 3, System.currentTimeMillis()));
            Thread.sleep(1000);
            markers.addAll(someRing.getLatencies());
        }
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.currentTimeMillis()));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2, System.currentTimeMillis()));
            Thread.sleep(1000);
            markers.addAll(someRing.getLatencies());
        }
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.currentTimeMillis()));
            Thread.sleep(1000);
            markers.addAll(someRing.getLatencies());
        }
        for (Long marker : markers) {
            System.out.println(marker);
        }
    }

    // Next test is made to investigate dependency of latency from amount of nodes in system, throwing only one message.
    // 2 nodes: 1256 3 nodes: 859 4 nodes: 653 5 nodes: 605 6 nodes: 578 7 nodes: 500 8 nodes: 395 9 nodes: 318
    @Test
    void changeAmountOfNodesWithConstantAmountOfMessages() throws InterruptedException {
        BufferedRing ring9 = new BufferedRing(9);
        BufferedRing ring8 = new BufferedRing(8);
        BufferedRing ring7 = new BufferedRing(7);
        BufferedRing ring6 = new BufferedRing(6);
        BufferedRing ring5 = new BufferedRing(5);
        BufferedRing ring4 = new BufferedRing(4);
        BufferedRing ring3 = new BufferedRing(3);
        BufferedRing ring2 = new BufferedRing(2);
        ring2.sendMessage(new Package("from 0 to 1", 0, 1, System.currentTimeMillis()));
        ring3.sendMessage(new Package("from 0 to 2", 0, 2, System.currentTimeMillis()));
        ring4.sendMessage(new Package("from 0 to 3", 0, 3, System.currentTimeMillis()));
        ring5.sendMessage(new Package("from 0 to 4", 0, 4, System.currentTimeMillis()));
        ring6.sendMessage(new Package("from 0 to 5", 0, 5, System.currentTimeMillis()));
        ring7.sendMessage(new Package("from 0 to 6", 0, 6, System.currentTimeMillis()));
        ring8.sendMessage(new Package("from 0 to 7", 0, 7, System.currentTimeMillis()));
        ring9.sendMessage(new Package("from 0 to 8", 0, 8, System.currentTimeMillis()));

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
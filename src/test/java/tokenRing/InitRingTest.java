package tokenRing;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

class InitRingTest {
    private Ring someRing = new Ring(5, "initial");
    private Ring throughputRing = new Ring(5, "throughputTest");

    // I will start my tests with Loading TokenRing with InitialNodes (this nodes are made to be able to "consume" packages,
    // that were sent for them. They are also configured to measure latency.
    @Test
    void FullyLoadTestForInitialNodes() throws InterruptedException {
        someRing.sendMessage(new Package("from 0 to 1", 0, 1));
        someRing.sendMessage(new Package("from 1 to 2", 1, 2));
        someRing.sendMessage(new Package("from 2 to 3", 2, 3));
        someRing.sendMessage(new Package("from 3 to 4", 3, 4));
        someRing.sendMessage(new Package("from 4 to 5", 4, 0));
        Thread.sleep(1000);
        //With messages sent from every node system struggles to deliver them to the destination points. the process will never end
    }

    // 5 nodes system latency were measured for different amount of messages sent from different nodes almost at one time:
    // the results are: 4: 451 3: 267 2: 233 1: 214, thus we can see that latency is low when system is not too loaded
    @Test
    void LoadTestForDifferentAmountOfMessages() throws InterruptedException {
        HashMap<Integer, Long> latenciesForNodes = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2));
            someRing.sendMessage(new Package("from 4 to 2", 2, 3));
            someRing.sendMessage(new Package("from 4 to 2", 3, 4));
        }
        Thread.sleep(1000);
        latenciesForNodes.put(4, someRing.getAverageLatencyMarker());
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2));
            someRing.sendMessage(new Package("from 4 to 2", 2, 3));
        }
        Thread.sleep(1000);
        latenciesForNodes.put(3, someRing.getAverageLatencyMarker());
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2));
        }
        Thread.sleep(1000);
        latenciesForNodes.put(2, someRing.getAverageLatencyMarker());
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1));
        }
        Thread.sleep(1000);
        latenciesForNodes.put(1, someRing.getAverageLatencyMarker());
        System.out.println(
                "4: " + latenciesForNodes.get(4)
                + " 3: " + latenciesForNodes.get(3)
                + " 2: " + latenciesForNodes.get(2)
                + " 1: " + latenciesForNodes.get(1)
        );
    }

//    //In order to test throughput I will use special Nodes called ThroughputNodes, which will not set messages
//    //sent for them.
//    //I will throw new messages from one node every 0,5 seconds and try to catch the moment when system will stop to perform ander such pressure.
//    @Test
//    void ThroughputTest() throws InterruptedException {
//        int counter = 0;
//        while (true) {
//            Thread.sleep(1000);
//            throughputRing.sendMessage(new Package("from 4 to 2", 3, 4));
//            counter += 1;
//            System.out.println(counter);
//        }
//    }

}
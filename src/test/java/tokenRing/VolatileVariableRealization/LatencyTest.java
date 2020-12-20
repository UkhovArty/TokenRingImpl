package tokenRing.VolatileVariableRealization;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tokenRing.Package;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LatencyTest {
    private static Ring someRing = new Ring(5, "initial");

    //This is the warm up
    @BeforeAll
    static void setUp() throws InterruptedException {
        Ring someOtherRing = new Ring(2, "initial");
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

    // 5 nodes system latency were measured for different amount of messages sent from different nodes almost at one time:
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
        File csvFile = new File("InitialLatencyFile");
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile))){
            for(Long item : markers){
                csvWriter.println(item);
            }
        } catch (IOException e) {
            //Handle exception
            e.printStackTrace();
        }
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
        ring2.sendMessage(new Package("from 0 to 1", 0, 1, System.currentTimeMillis()));
        ring3.sendMessage(new Package("from 0 to 1", 0, 2, System.currentTimeMillis()));
        ring4.sendMessage(new Package("from 0 to 1", 0, 3, System.currentTimeMillis()));
        ring5.sendMessage(new Package("from 0 to 1", 0, 4, System.currentTimeMillis()));
        ring6.sendMessage(new Package("from 0 to 1", 0, 5, System.currentTimeMillis()));
        ring7.sendMessage(new Package("from 0 to 1", 0, 6, System.currentTimeMillis()));
        ring8.sendMessage(new Package("from 0 to 1", 0, 7, System.currentTimeMillis()));
        ring9.sendMessage(new Package("from 0 to 1", 0, 8, System.currentTimeMillis()));

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
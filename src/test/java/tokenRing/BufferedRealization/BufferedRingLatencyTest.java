package tokenRing.BufferedRealization;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tokenRing.Package;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

class BufferedRingLatencyTest {

    private static BufferedRing someRing = new BufferedRing(2);

    //This is the warm up
    @BeforeAll
    static void setUp() throws InterruptedException {
        BufferedRing someOtherRing = new BufferedRing(2);
        for (int i = 0; i < 1; i++) {
            Thread.sleep(10);
            someOtherRing.sendMessage(new Package("from 0 to 1", 0, 1, System.nanoTime()));
        }
        Thread.sleep(30000);
    }

    @SneakyThrows
    @Test
    void LatencyPerCapacityTest() {
        ArrayList<Long> latencies = new ArrayList<>();
        String filePath = "C:\\Users\\auhov\\IdeaProjects\\TokenRingImpl\\src\\test\\resources\\LatenciesPerNodeAmountPerCapacity.txt";
        int numOfNodes = 6;
        List<Integer> amountOfMsgs = asList(6, 21, 34);
        int capacity = 6;
        for (Integer j : amountOfMsgs) {
            BufferedRing someRing1 = new BufferedRing(numOfNodes);
            for (int i = 0; i < j; i++) {
                someRing1.sendMessage(new Package("from 0 to 3", 0, numOfNodes - 1, System.nanoTime()));
                Thread.sleep(1);
            }
            for (int i = 0; i < 100; i++) {
                Thread.sleep(10);
                latencies.add(someRing1.getNodeLatencyMarker(numOfNodes - 1));
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(numOfNodes).append("nodes capacity ").append(capacity).append(" ").append(j).append(" msgs ");
            for (Long latency : latencies) {
                stringBuilder.append(latency.toString()).append(",");
            }
            stringBuilder.append("\n");
            try {
                Files.write(Paths.get(filePath), stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println(e);
            }
            latencies = new ArrayList<>();
        }
    }

    // I will start my tests with Loading TokenRing with InitialNodes (this nodes are made to be able to "consume" packages,
    // that were sent for them. They are also configured to measure latency.
    @Test
    void FullyLoadTestForInitialNodes() throws InterruptedException {
        someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.nanoTime()));
        someRing.sendMessage(new Package("from 1 to 2", 1, 2, System.nanoTime()));
        someRing.sendMessage(new Package("from 2 to 3", 2, 3, System.nanoTime()));
        someRing.sendMessage(new Package("from 3 to 4", 3, 4, System.nanoTime()));
        someRing.sendMessage(new Package("from 4 to 5", 4, 0, System.nanoTime()));
        Thread.sleep(10000);
        //With messages sent from every node system struggles to deliver them to the destination points. the process will never end
    }

    @Test
    void LoadTestForDifferentAmountOfMessages() throws InterruptedException {
        List<Long> markers = new ArrayList<>();
        Map<String, List<Long>> latencies = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.nanoTime()));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2, System.nanoTime()));
            someRing.sendMessage(new Package("from 2 to 3", 2, 3, System.nanoTime()));
            someRing.sendMessage(new Package("from 3 to 4", 3, 4, System.nanoTime()));
            Thread.sleep(1000);
            markers.addAll(asList(someRing.getNodeLatencyMarker(1),
                    someRing.getNodeLatencyMarker(2),
                    someRing.getNodeLatencyMarker(3),
                    someRing.getNodeLatencyMarker(4)));
        }
        latencies.put("4 msgs", markers);
        markers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.nanoTime()));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2, System.nanoTime()));
            someRing.sendMessage(new Package("from 4 to 2", 2, 3, System.nanoTime()));
            Thread.sleep(1000);
            markers.addAll(asList(someRing.getNodeLatencyMarker(1),
                    someRing.getNodeLatencyMarker(2),
                    someRing.getNodeLatencyMarker(3)));
        }
        latencies.put("3 msgs", markers);
        markers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.nanoTime()));
            someRing.sendMessage(new Package("from 1 to 2", 1, 2, System.nanoTime()));
            Thread.sleep(1000);
            markers.addAll(asList(someRing.getNodeLatencyMarker(1),
                    someRing.getNodeLatencyMarker(2)));
        }
        latencies.put("2 msgs", markers);
        markers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 1, System.nanoTime()));
            Thread.sleep(1000);
            markers.addAll(asList(someRing.getNodeLatencyMarker(1)));
        }
        latencies.put("1 msgs", markers);
        File csvFile = new File("BufferedLatencyFile1");
        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFile))) {
            for (String s : latencies.keySet()) {
                csvWriter.println(s + "\n");
                for (Long item : latencies.get(s)) {
                    csvWriter.print(item + ",");
                }
                csvWriter.println();
            }
        } catch (IOException e) {
            //Handle exception
            e.printStackTrace();
        }
    }

    // Next test is made to investigate dependency of latency from amount of nodes in system, throwing only one message.
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
        ring2.sendMessage(new Package("from 0 to 1", 0, 1, System.nanoTime()));
        ring3.sendMessage(new Package("from 0 to 2", 0, 2, System.nanoTime()));
        ring4.sendMessage(new Package("from 0 to 3", 0, 3, System.nanoTime()));
        ring5.sendMessage(new Package("from 0 to 4", 0, 4, System.nanoTime()));
        ring6.sendMessage(new Package("from 0 to 5", 0, 5, System.nanoTime()));
        ring7.sendMessage(new Package("from 0 to 6", 0, 6, System.nanoTime()));
        ring8.sendMessage(new Package("from 0 to 7", 0, 7, System.nanoTime()));
        ring9.sendMessage(new Package("from 0 to 8", 0, 8, System.nanoTime()));

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
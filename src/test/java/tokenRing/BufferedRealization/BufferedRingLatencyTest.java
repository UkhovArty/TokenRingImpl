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
        int numOfNodes = 3;
        List<Integer> amountOfMsgs = asList(1, 3, 5);
        int capacity = 1;
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

    @Test
    void LoadTestForDifferentAmountOfMessages() throws InterruptedException {
        List<Long> latencies = new ArrayList<>();
        BufferedRing someRing = new BufferedRing(3);
        String filePath = "C:\\Users\\auhov\\IdeaProjects\\TokenRingImpl\\src\\test\\resources\\LatenciesPerMsgs1.txt";
        for (int numOfMsgs = 1; numOfMsgs < 13; numOfMsgs++) {
//            for (int i = 0; i < numOfMsgs; i++) {
            someRing.sendMessage(new Package("from 0 to 1", 0, 2, System.nanoTime()));
//                Thread.sleep(15);
//            }
            for (int i = 0; i < 40; i++) {
                Thread.sleep(20);
                latencies.add(someRing.getNodeLatencyMarker(2));
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(numOfMsgs).append("m,");
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
}
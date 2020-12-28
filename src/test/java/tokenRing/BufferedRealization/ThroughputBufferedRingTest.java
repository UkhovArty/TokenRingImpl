package tokenRing.BufferedRealization;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tokenRing.Package;
import tokenRing.VolatileVariableRealization.Ring;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

class ThroughputBufferedRingTest {

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
    void ThroughputPerCapacityTest() {
        ArrayList<Integer> throughputs = new ArrayList<>();
        String filePath = "C:\\Users\\auhov\\IdeaProjects\\TokenRingImpl\\src\\test\\resources\\ThroughputsPerNodeAmountPerCapacity.txt";
        int numOfNodes = 3;
        int capacity = 3;
        List<Integer> amountOfMsgs = asList(2, 6, 10);
        for (Integer j : amountOfMsgs) {
            BufferedRing someRing1 = new BufferedRing(numOfNodes);
            for (int i = 0; i < j; i++) {
                someRing1.sendMessage(new Package("from 0 to 3", 0, numOfNodes - 1, System.nanoTime()));
                Thread.sleep(1);
            }
            for (int i = 0; i < 50; i++) {
                someRing1.resetThroughputs();
                Thread.sleep(500);
                throughputs.addAll(someRing1.getNodesThroughputs());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(numOfNodes).append("n").append(capacity).append("c").append(j).append("m ");
            for (Integer thput : throughputs) {
                stringBuilder.append(thput.toString()).append(",");
            }
            stringBuilder.append("\n");
            try {
                Files.write(Paths.get(filePath), stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println(e);
            }
            throughputs = new ArrayList<>();
        }
    }

    @Test
    void LoadTestForDifferentAmountOfMessages() throws InterruptedException {
        List<Integer> throughputs = new ArrayList<>();
        BufferedRing someRing = new BufferedRing(3);
        String filePath = "C:\\Users\\auhov\\IdeaProjects\\TokenRingImpl\\src\\test\\resources\\ThroughputPerMsgs1.txt";
        for (int numOfMsgs = 1; numOfMsgs < 12; numOfMsgs++) {
//            for (int i = 0; i < numOfMsgs; i++) {
                someRing.sendMessage(new Package("from 0 to 1", 0, 2, System.nanoTime()));
                Thread.sleep(15);
//            }
            for (int i = 0; i < 30; i++) {
                    someRing.resetThroughputs();
                    Thread.sleep(1000);
                    throughputs.addAll(someRing.getNodesThroughputs());
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(numOfMsgs).append("m,");
            for (Integer thput : throughputs) {
                stringBuilder.append(thput.toString()).append(",");
            }
            stringBuilder.append("\n");
            try {
                Files.write(Paths.get(filePath), stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println(e);
            }
            throughputs = new ArrayList<>();
        }
    }
}
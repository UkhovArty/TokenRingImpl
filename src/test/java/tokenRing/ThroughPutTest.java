package tokenRing;

import org.junit.jupiter.api.Test;

class ThroughPutTest {
    private Ring throughputRing = new Ring(5, "throughputTest");

    //In order to test throughput I will use special Nodes called ThroughputNodes, which will not set messages
    //sent for them.
    //I will fill maximum amount of messages allowed to a five node system without "consuming" them,
    // and try to figure out how many messages will pass through one node in a 10000 period
    //------------------------(avg for 5 nodes with 5 nonconsumig messages in system)-------------------------------
    // the result is 44 messages per second (not really cool) (avg for 5 nodes with 5 nonconsumig messages in system)
    //avg by 10 tries is 44.33 msq per second (my laptop is a potato :) )
    @Test
    void ThroughputFullyLoadedSystemTest() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread.sleep(100);
            throughputRing.sendMessage(new Package("from 4 to 2", 3, 4));
        }
        Thread.sleep(30000);
        System.out.println(throughputRing.getAverageThroughput());
    }

    //Now I will modernize previous test in order to cover cases with under loaded system.
    @Test
    void ThroughputUnderLoadedSystemTest() throws InterruptedException {
        throughputRing.sendMessage(new Package("from 4 to 2", 3, 4));
        Thread.sleep(30000);
        System.out.println(throughputRing.getAverageThroughput());
    }

    //Normal loaded System
    @Test
    void ThroughputNormalLoadedSystemTest() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            Thread.sleep(100);
            throughputRing.sendMessage(new Package("from 4 to 2", 3, 4));
        }
        Thread.sleep(10000);
        System.out.println(throughputRing.getAverageThroughput());
    }

    //Thus fully loaded system shows slightly better throughput,
    // I will measure throughput on num of nodes dependency for the fully loaded systems:
    //I will do it in manual mode
    @Test
    void ThroughputForDifferentNodeAmountsTest() throws InterruptedException {
        Ring ring = new Ring(7, "throughputTest");
        for (int i = 0; i < 1; i++) {
            Thread.sleep(100);
            ring.sendMessage(new Package("from 4 to 2", 2, 1));
        }
        Thread.sleep(10000);
        System.out.println(ring.getAverageThroughput());
    }
}
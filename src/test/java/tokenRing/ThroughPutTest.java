package tokenRing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThroughPutTest {
    private Ring throughputRing = new Ring(5, "throughputTest");

    //In order to test throughput I will use special Nodes called ThroughputNodes, which will not set messages
    //sent for them.
    //I will fill maximum amount of messages allowed to a five node system without "consuming" them,
    // and try to figure out how many messages will pass through one node in a 10000 period
    //------------------------(avg for 5 nodes with 5 nonconsumig messages in system)-------------------------------
    // the result is 44 messages per second (not really cool) (avg for 5 nodes with 5 nonconsumig messages in system)
    // 42 second try
    // 47 third try
    //44.33 msq per second (my laptop is a potato :) )
    @Test
    void ThroughputTest() throws InterruptedException {
        int counter = 0;
        for (int i = 0; i < 5; i++) {
            Thread.sleep(100);
            throughputRing.sendMessage(new Package("from 4 to 2", 3, 4));
        }
        Thread.sleep(30000);
        System.out.println(throughputRing.getAverageThroughput());
    }

}
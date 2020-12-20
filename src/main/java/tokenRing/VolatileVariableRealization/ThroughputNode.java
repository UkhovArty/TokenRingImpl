package tokenRing.VolatileVariableRealization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tokenRing.Package;

@RequiredArgsConstructor
@Getter
@Setter
public class ThroughputNode implements Runnable, Node {
    private final Integer num;
    private volatile Package data;
    private Node nextNode;
    private Long latencyMarker;
    private Integer amountOfMsgs = 0;
    private Long firstRecievedMsg;

    @Override
    public void run() {
        while (true) {
            if (this.data != null) {
                if (amountOfMsgs.equals(0)) {
                    firstRecievedMsg = this.data.getTimeSent();
                }
                passPack(this.data);
                if (System.currentTimeMillis() - firstRecievedMsg <= 1000) {
                    this.amountOfMsgs += 1;
                }
                this.data = null;
            }
        }

    }

    @Override
    public void passPack(Package pack) {
        System.out.println("Package: " + pack.getMessage() + " sent from node: " + this.num);
        this.nextNode.obtainPack(pack);
    }

    @Override
    public void obtainPack(Package pack) {
        System.out.println("Package: " + pack.getMessage() + " received on node: " + this.num);
        while (this.data != null) {
        }
        this.data = pack;
    }
}

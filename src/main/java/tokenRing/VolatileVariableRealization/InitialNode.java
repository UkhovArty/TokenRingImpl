package tokenRing.VolatileVariableRealization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tokenRing.Package;

@RequiredArgsConstructor
@Getter
@Setter
public class InitialNode implements Runnable, Node {
    private final Integer num;
    private volatile Package data;
    private Node nextNode;
    private Long latencyMarker = 0L;
    private Integer amountOfMsgs = 0;

    public void run() {
            while (true) {
                if (this.data != null) {
                if (!this.num.equals(this.data.getWhere())) {
                    passPack(this.data);
                } else {
                    this.latencyMarker = System.currentTimeMillis() - data.getTimeSent();
                    System.out.println("latency is " + this.latencyMarker);
                }
                this.data = null;
            }
        }
    }

    @Override
    public void passPack(Package pack) {
        this.nextNode.obtainPack(pack);
    }

    @Override
    public void obtainPack(Package pack) {
        while (this.data != null) {
        }
        this.data = pack;
    }
}

package tokenRing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
                    System.out.println(this.data.getMessage() + "set on node: " + this.num);
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

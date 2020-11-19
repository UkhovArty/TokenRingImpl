package tokenRing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ThroughputNode implements Runnable, Node {
    private final Integer num;
    private volatile Package data;
    private Node nextNode;
    private Long latencyMarker;

    @Override
    public void run() {
        while (true) {
            if (this.data != null) {
                passPack(this.data);
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

package tokenRing.BufferedRealization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tokenRing.Package;

@RequiredArgsConstructor
@Getter
@Setter
public class NodeImpl implements Node, Runnable {
    private final Integer num;
    private Package data;
    private final Medium pushingMedium;
    private final Medium pullingMedium;
    private Long latencyMarker = 0L;

    @Override
    public void push(Package pack) {
        pushingMedium.accept(data);
        this.data = null;
    }

    @Override
    public void pull() {
        try {
            data = pullingMedium.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (data.getWhere().equals(num)) {
            this.latencyMarker = System.currentTimeMillis() - data.getTimeSent();
            System.out.println("Latency fot node" + this.num
                    + " for message " + this.data.getMessage() + " is " + this.latencyMarker);
            data = null;
        }
    }

    @Override
    public void run() {
        while (true) {
            if (this.data != null) {
                push(this.data);
            } else {
                try {
                    pull();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

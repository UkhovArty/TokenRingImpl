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
    private Integer amountOfMsgs = 0;
    private boolean isFirstMsgRecieved = false;
    private Long time;

    @Override
    public void push(Package pack) {
        pushingMedium.accept(data);
        this.data = null;
    }

    @Override
    public void pull() {
        try {
            this.data = pullingMedium.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!this.isFirstMsgRecieved) {
            this.time = System.currentTimeMillis();
            isFirstMsgRecieved = true;
        } else {
            if (System.currentTimeMillis() - time <= 500) {
                amountOfMsgs += 1;
            }
        }
//        if (this.data.getWhere().equals(this.num)) {
//            this.latencyMarker = System.nanoTime() - this.data.getTimeSent();
//        }
//        if (this.data.getFrom().equals(this.num)) {
//            this.data.setTimeSent(System.nanoTime());
//        }
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

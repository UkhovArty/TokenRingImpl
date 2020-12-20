package tokenRing.BufferedRealization;

import lombok.NoArgsConstructor;
import tokenRing.Package;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@NoArgsConstructor
public class BlockingQueueMedium implements Medium {
    private final BlockingQueue<Package> queue = new ArrayBlockingQueue<>(5);

    @Override
    public void accept(Package data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Package get() throws InterruptedException {
        return queue.take();
    }
}

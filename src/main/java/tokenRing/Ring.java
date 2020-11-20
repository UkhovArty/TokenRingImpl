package tokenRing;

public class Ring {
    private final Node[] nodes;
    private final String THROUGHPUT_TEST = "throughputTest";
    private final String INITIAL = "initial";
    private Long averageLatencyMarker = 0L;
    private Integer averageThroughput = 0;

    public Ring(int numOfNodes, String nodeType) {
        if (nodeType.equals(INITIAL)) {
            this.nodes = new InitialNode[numOfNodes];
        } else if (nodeType.equals(THROUGHPUT_TEST)) {
            this.nodes = new ThroughputNode[numOfNodes];
        } else {
            this.nodes = new InitialNode[numOfNodes];
        }
        createRing(numOfNodes, nodeType);
    }

    public void sendMessage(Package pack) {
        this.nodes[pack.getFrom()].setData(pack);
    }

    public Long getAverageLatencyMarker() {
        for (Node node : nodes) {
            averageLatencyMarker += node.getLatencyMarker();
        }
        averageLatencyMarker = averageLatencyMarker / nodes.length;
        return averageLatencyMarker;
    }

    public Long getNodeLatencyMarker(Integer numNode) {
        return nodes[numNode].getLatencyMarker();
    }

    public Integer getAverageThroughput() {
        for (Node node : nodes) {
            averageThroughput += node.getAmountOfMsgs();
        }
        averageThroughput = averageThroughput / nodes.length;
        return averageThroughput;
    }

    private void createRing(int numOfNodes, String nodeType) {
        for (int i = 0; i < numOfNodes; i++) {
            if (nodeType.equals(INITIAL)) {
                this.nodes[i] = new InitialNode(i);
            } else if (nodeType.equals(THROUGHPUT_TEST)) {
                this.nodes[i] = new ThroughputNode(i);
            }
        }
        for (int i = 0; i < numOfNodes - 1; i++) {
            this.nodes[i].setNextNode(nodes[i + 1]);
        }
        this.nodes[numOfNodes - 1].setNextNode(nodes[0]);
        for (Node node : nodes) {
            new Thread(node).start();
        }
    }
}

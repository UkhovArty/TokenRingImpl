package tokenRing;

public class Ring {
    private final Node[] nodes;

    public Ring(int numOfNodes) {
        this.nodes = new Node[numOfNodes];
        createRing(numOfNodes);
    }

    public void sendMessage(Package pack) {
        if (pack.getWhere() > this.nodes.length) {
            System.out.println("Invalid recieving node");
        } else {
            this.nodes[pack.getFrom()].setData(pack);
        }
    }

    private void createRing(int numOfNodes) {
        for (int i = 0; i < numOfNodes; i++) {
            this.nodes[i] = new Node(i);
        }
        for (int i = 0; i < numOfNodes - 1; i++) {
            this.nodes[i].setNextNode(nodes[i+1]);
        }
        this.nodes[numOfNodes-1].setNextNode(nodes[0]);
        for (Node node : nodes) {
            new Thread(node).start();
        }
    }
}

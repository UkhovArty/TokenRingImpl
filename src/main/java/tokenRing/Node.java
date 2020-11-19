package tokenRing;

public interface Node extends Runnable {
    void passPack(Package pack);
    void obtainPack(Package pack);
    Package getData();
    Integer getNum();
    Node getNextNode();
    Long getLatencyMarker();

    void setData(Package pack);
    void setNextNode(Node nextNode);


}

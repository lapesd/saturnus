package main;


public class AbstractSimulator {

    private double mainClock;
    protected AbstractDataNode[] dataNodes;
    protected static OrderedSet eventsQueue;

    public double getMainClock() {
        return this.mainClock;
    }

    public void initDataNodes(int number) {
        dataNodes = new AbstractDataNode[number];
        for (int i = 0; i < number; i++) {
            this.dataNodes[i] = new DataNode(i+1);
        }
    }
    
    public void insertEvent(Event newEvent) {
        eventsQueue.insert(newEvent);
        if(dataNodes[0].getQueueSize() > dataNodes[1].getQueueSize()) {
            dataNodes[1].insertEvent(newEvent);
        } else {
            dataNodes[0].insertEvent(newEvent);
        }
    }

    public void executeEvents() {
        while ((Event)eventsQueue.removeFirst() != null) {
            for (int i = 0; i < dataNodes.length; i++) {
                dataNodes[i].executeAll();
            }
        }
        this.mainClock += clockSync();
        System.out.println("Clock: " + getMainClock() + "\n");
    }

    public double clockSync() {
        double large = dataNodes[0].getClock();
        for (int i = 1; i < dataNodes.length; i++) {
            if(dataNodes[i].getClock() > large) { large = dataNodes[i].getClock(); }
        }
        return large;
    }
}

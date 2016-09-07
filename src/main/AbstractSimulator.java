package main;

/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of events, data nodes, etc.
 */
public class AbstractSimulator {

    /**
     * Next node to insert an event. For workload balance.
     */
    private int nextDataNode;
    private double mainClock;
    protected AbstractDataNode[] dataNodes;
    protected static OrderedSet eventsQueue;

    /**
     * Gets the main clock of the simulator.
     * @return The time used by the simulator.
     */
    public double getMainClock() {
        return this.mainClock;
    }

    /**
     * Initialize and add the nodes to the "dataNodes" vector.
     * @param number - Number of data nodes of the system.
     */
    public void initDataNodes(int number) {
        dataNodes = new AbstractDataNode[number];
        for (int i = 0; i < number; i++) {
            this.dataNodes[i] = new DataNode(i+1);
        }
        this.nextDataNode = Random.randomInt(0, number-1);
    }

    /**
     * Insert an event to a dataNode and to the queue of events.
     * @param newEvent - Event to be added.
     */
    public void insertEvent(Event newEvent) {
        eventsQueue.insert(newEvent);
        dataNodes[this.nextDataNode].insertEvent(newEvent);
        if(nextDataNode == dataNodes.length-1) {
            this.nextDataNode = 0;
        } else {
            this.nextDataNode++;
        }
    }

    /**
     * Remove all events from the queue and execute them on the data nodes.
     * Besides that, synchronize the main clock with the events execution time.
     */
    public void executeEvents() {
        while (eventsQueue.removeFirst() != null) {
            for (int i = 0; i < dataNodes.length; i++) {
                dataNodes[i].executeAll();
            }
        }
        this.mainClock += clockSync();
        System.out.println("Clock: " + getMainClock() + "\n");
    }

    /**
     * Gets the clock of all data nodes to synchronize with the main clock
     * @return The largest clock, among the data nodes.
     */
    public double clockSync() {
        double large = dataNodes[0].getClock();
        dataNodes[0].clearClock();
        for (int i = 1; i < dataNodes.length; i++) {
            if(dataNodes[i].getClock() > large) { large = dataNodes[i].getClock(); }
            dataNodes[i].clearClock();
        }
        return large;
    }
}

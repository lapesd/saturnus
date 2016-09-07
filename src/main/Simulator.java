package main;

public class Simulator extends AbstractSimulator {

    public static void main(String[] args) {

        /* Executa uma simulacao inicial.*/
        new Simulator().init();
    }

    /**
     * Initialize the simulator itself. Initialize the data nodes, insert events and
     * execute them.
     */
    public void init() {
        eventsQueue = new EventQueue();
        initDataNodes(2);
        insertEvent(new WriteEvent(Random.exponential(6.0), 12345));
        insertEvent(new WriteEvent(Random.exponential(6.0), 10001));
        insertEvent(new WriteEvent(Random.exponential(6.0), 25555));
        insertEvent(new ReadEvent(Random.exponential(1.0), 999));
        executeEvents();
    }
}
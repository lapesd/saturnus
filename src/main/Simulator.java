package main;

import java.util.Scanner;

public class Simulator extends AbstractSimulator {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int nodesAmount;

        System.out.println("Insert the number of data nodes: ");
        nodesAmount = Integer.parseInt(input.next());


        /* Executa uma simulacao inicial.*/
        new Simulator().init(nodesAmount);
    }

    /**
     * Initialize the simulator itself. Initialize the data nodes, insert events and
     * execute them.
     * @param nodesAmount - Amount of data nodes of the system.
     */
    public void init(int nodesAmount) {
        eventsQueue = new EventQueue();
        initDataNodes(nodesAmount);
        insertEvent(new WriteEvent(Random.exponential(6.0), 12345));
        insertEvent(new WriteEvent(Random.exponential(6.0), 10001));
        insertEvent(new WriteEvent(Random.exponential(6.0), 25555));
        insertEvent(new ReadEvent(Random.exponential(1.0), 999));
        executeEvents();
    }
}
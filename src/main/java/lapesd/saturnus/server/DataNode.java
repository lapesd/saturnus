package lapesd.saturnus.server;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.dataStructures.SubReqComparator;
import lapesd.saturnus.event.SubRequest;
import lapesd.saturnus.simulator.AbstractSimulator;

import java.util.PriorityQueue;

public class DataNode extends Entity {

    private double nodeClock;
    private int ID;
    private AbstractSimulator model;
    private PriorityQueue<SubRequest> subRequestsQueue;

    public DataNode(Model model, int dataNodeID) {
        super(model, "DataNode", true);
        this.model = (AbstractSimulator)model;
        this.nodeClock = 0.0;
        this.ID = dataNodeID;
        this.subRequestsQueue = new PriorityQueue<>(new SubReqComparator());
    }

    public int getID() {
        return this.ID;
    }


    /**
     * Add a given sub request into the queue. Beyond that, check if
     * the sub-request needs to be scheduled at some specific time span
     * (usually because of a sync between requests).
     * @param subRequest Sub-request to insert
     */
    public void insertSubRequest(SubRequest subRequest) {
        if (subRequest.getSendingTime() == null) {
            subRequest.setSendingTime(new TimeSpan(nodeClock));
        }
        this.subRequestsQueue.add(subRequest);
    }

    /**
     * Remove the first element of the sub-request queue.
     * @return The removed element
     */
    private SubRequest removeFirstSubRequest() {
        return this.subRequestsQueue.poll();
    }


    /**
     * Get the given sub-request and schedule it. That means, execute the event.
     * Just to simplify, the 'execution time' of each event is considered as 1
     * time unit(incremented into the sub request).
     */
    public void executeOneSubRequest() {
        // TODO: To generate synchrony among nodes. Node 'wait' until the sending time arrives.
        SubRequest toBeExecuted = removeFirstSubRequest();
        double execTime = toBeExecuted.getExecutionTime();
        TimeSpan attendedTime = new TimeSpan(nodeClock);
        TimeSpan outputTime = new TimeSpan(nodeClock + execTime);
        toBeExecuted.setAttendedTime(attendedTime);
        toBeExecuted.setOutputTime(outputTime);

        skipTraceNote();    // Avoiding useless messages on trace
        toBeExecuted.schedule(toBeExecuted.getRequest(), this, toBeExecuted.getClient(),
                attendedTime);
        this.nodeClock += execTime;
        this.model.saveSubRequest(toBeExecuted);

        // Send request ID and the sub-request output time.
        toBeExecuted.getClient().sendFinishedSignal(toBeExecuted.getRequest(), this.nodeClock);
    }
}

/*
    Ideia:
    Cada cliente tem um vetor com todas as suas requisições. Apenas a primeira é enviada.
    Assim, quando os nodos de dados terminam de executar as sub-requisições dessa requisição,
     eles enviam seus tempos para o cliente que as enviou.
    Após o cliente notar que todas as sub-requisições da requisição 0 foram executadas, ele envia
     a próxima, já sincronizada, e manda os nodo executarem novamente. NOTA: Pode ser que a criação
     de uma função para executar apenas uma requisição ou sub-requisição nos nodos seja útil.
    Quando não existirem mais requisições em nenhuma máquina cliente, o simulador deve encerrar a sua
     execução.

     'Protótipo': execOneSub() ->
        Agenda a execução da sub-request (Talvez manter a fila e pegar a primeira dela)
        Pega o relógio
        Envia para o cliente, 'dono' da sub-req um sinal que ela foi executada e o output time dela.
        Cliente verifica se precisa enviar uma nova pros nodos ou ainda não.

     Nota: gerar todos os blocos de cada cliente, enviar apenas a primeira requisição. Deixar eles
     armazenados (talvez)
     Nota2: requisições podem ter um atributo sub-reqs exacutadas, para manter apenas um vetor nos
     clientes
     Nota3: abstractSimulator pode ter um método que fica mandando os nodos executarem uma sub-request
     enquanto ainda tiver alguma na fila ou alguma para ser enviada pelos clientes (vectorOfRequests.size)
 */

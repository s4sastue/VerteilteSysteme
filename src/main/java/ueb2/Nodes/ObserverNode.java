package ueb2.Nodes;

import org.oxoo2a.sim4da.Message;
import org.oxoo2a.sim4da.Node;
import ueb2.MessageCounter.MessageCounter;


import java.util.ArrayList;
import java.util.List;

public class ObserverNode extends Node {
    private static ObserverNode observerNode = null;
    MessageCounter messageCounter;
    List<String> pyrotechnicianNodes;
    private ObserverNode(MessageCounter messageCounterType, List<String> pyrotechnicianNodes) {
        super("Observer");
        assert messageCounterType != null;
        this.messageCounter = messageCounterType.getNewInstance(NodeName());
        this.pyrotechnicianNodes = new ArrayList<>(pyrotechnicianNodes);
        System.out.println("STARTING OBSERVER");
    }

    public static ObserverNode getObserverNode(MessageCounter messageCounter, List<String> pyrotechnicianNodes){
        if (observerNode == null){
            assert messageCounter != null;
            observerNode = new ObserverNode(messageCounter, pyrotechnicianNodes);
        }

        return observerNode;
    }

    @Override
    protected void engage() {
        // each iteration checks whether the simulation is terminated
        while(true){
            boolean allActorsPassive = true;
            messageCounter.reset();

            int messagesReceivedIteration = 0;
            Message message;
            // Sending a control message to each actor
            for(String nodeName: pyrotechnicianNodes){
                message = new Message()
                        .addHeader("type", "CONTROL_MESSAGE");

                System.out.println(NodeName() + ": Request to " + nodeName + " to send status information");

                messagesReceivedIteration++;
                sendBlindly(message, nodeName);
            }

            // wait until each actor has replied and process each message
            while(messagesReceivedIteration>0){
                messagesReceivedIteration--;
                message = receive();

                if(message.getHeader().get("state").equals("ACTIVE")){
                    allActorsPassive = false;
                }

                // updating the statistics
                messageCounter.aggregate(message.getPayload());
            }

            // if termination detected, send exit message to all actors
            if (allActorsPassive && messageCounter.isTerminated()){
                for(String nodeName: pyrotechnicianNodes){
                    message = new Message()
                            .addHeader("type", "EXIT_MESSAGE");

                    sendBlindly(message, nodeName);
                }
                break;
            }
        }
    }
}

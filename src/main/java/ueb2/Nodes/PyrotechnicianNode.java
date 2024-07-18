package ueb2.Nodes;

import org.oxoo2a.sim4da.Message;
import org.oxoo2a.sim4da.Node;
import ueb2.DataStructure.STATE;
import ueb2.MessageCounter.MessageCounter;
import java.util.*;

public class PyrotechnicianNode extends Node {
    MessageCounter messageCounter;
    List<String> pyrotechnicianNodes;
    double probability = 0.9;
    STATE state = STATE.PASSIVE;

    public PyrotechnicianNode(String name, MessageCounter messageCounterType, List<String> pyrotechnicianNodes) {
        super(name);

        if(messageCounterType != null){
            this.messageCounter = messageCounterType.getNewInstance(name);
        }

        this.pyrotechnicianNodes = new ArrayList<>(pyrotechnicianNodes);
        this.pyrotechnicianNodes.remove(NodeName());
    }


    /**
     * Method that sends fireworks to random nodes
     */
    private void startFirework(){
        Collections.shuffle(pyrotechnicianNodes);
        int number = new Random().nextInt(pyrotechnicianNodes.size());
        System.out.println(NodeName() + ": Fireworks send to " + pyrotechnicianNodes.subList(0, number));

        for (int i=0; i<=number; i++){
            String receiver = pyrotechnicianNodes.get(i);
            Message message = new Message()
                    .addHeader("type", "FIREWORK");

            if(messageCounter != null){
                messageCounter.sendMessageTo(receiver);
            }

            sendBlindly(message, receiver);
        }
    }


    /**
     * Method to send the internal message statistics to the observer
     */
    private void sendControlMessage(){
        String state = (this.state == STATE.ACTIVE)
                ? "ACTIVE"
                : "PASSIVE";

        Message message = new Message()
                .addHeader("type", "CONTROL_MESSAGE_ANSWER")
                .addHeader("state", state);

        // every value from internal statistics is written to message
        if(messageCounter != null){
            Map<String, Integer> result = messageCounter.getMessageCounterValues();
            for(Map.Entry<String, Integer> entry: result.entrySet()){
                message.add(entry.getKey(), entry.getValue());
            }
        }

        sendBlindly(message, "Observer");
    }

    @Override
    protected void engage() {
        state = STATE.ACTIVE;
        startFirework();
        state = STATE.PASSIVE;

        Message message;
        while ((message = receive()) != null) {
            String sender = message.queryHeader("sender");

            // Update statistics (only messages from actuators are considered)
            if(messageCounter != null && !sender.equals("Observer")){
                messageCounter.receivedMessageFrom(sender);
            }

            // behavior is adapted to the message type
            String messageType = message.getHeader().get("type");
            switch(messageType){
                case "FIREWORK":
                    state = STATE.ACTIVE;

                    if (new Random().nextDouble() <= probability) {
                        startFirework();
                        probability /= 2;
                    }else{
                        System.out.println(NodeName() + ": Firework from " + sender + " ignored (Probability for send Fireworks: " + probability + ")");
                    }

                    state = STATE.PASSIVE;
                    continue;

                case "CONTROL_MESSAGE":
                    sendControlMessage();
                    continue;
                case "EXIT_MESSAGE":
                    break;
            }
            break;
        }
    }
}

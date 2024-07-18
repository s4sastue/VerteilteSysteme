package ueb2.MessageCounter;

import java.util.HashMap;
import java.util.Map;

public abstract class MessageCounter{
    String nodeName;
    Map<String, Integer> messagesSend;
    Map<String, Integer> messagesReceived;

    MessageCounter(String nodeName){
        this.nodeName = nodeName;
        messagesSend = new HashMap<>();
        messagesReceived = new HashMap<>();
    }

    /**
     * Method that saves the statistics on send messages
     *
     * @param receiverName name of the message receiver
     */
    public void sendMessageTo(String receiverName){
        int value = 1;
        if(messagesSend.containsKey(receiverName)){
            value += messagesSend.get(receiverName);
        }
        messagesSend.put(receiverName, value);
    }

    /**
     * Method that saves the statistics on received messages
     *
     * @param senderName name of the message sender
     */
    public void receivedMessageFrom(String senderName){
        int value = 1;
        if(messagesReceived.containsKey(senderName)){
            value += messagesReceived.get(senderName);
        }
        messagesReceived.put(senderName, value);
    }

    /**
     * Method that calculates the required indicators of a termination method
     */
    public abstract Map<String, Integer> getMessageCounterValues();

    /**
     * Method that creates a new instance
     */
    public abstract MessageCounter getNewInstance(String nodeName);

    /**
     * Method that calculates the required indicators of a method
     *
     * @param messageCountData Data to be integrated into existing data structure
     */
    public abstract void aggregate(Map<String, String> messageCountData);


    /**
     * Method that switches the internal data state to the next time step
     */
    public abstract void reset();

    /**
     * Method with which it can be checked whether the termination has occurred
     */
    public abstract boolean isTerminated();
}
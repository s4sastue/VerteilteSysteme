package ueb2.MessageCounter;


import java.util.HashMap;
import java.util.Map;

public class DoubleCountMethodMessageCounter extends MessageCounter {
    int sumMessagesReceived = 0,
            sumMessagesSend = 0,
            lastSumMessagesReceived = -1,
            lastSumMessagesSend = -1;

    public DoubleCountMethodMessageCounter(String nodeName) {
        super(nodeName);
    }

    @Override
    public Map<String, Integer> getMessageCounterValues() {

        int messageSendCounter = 0;
        for(Map.Entry<String, Integer> entry: messagesSend.entrySet()){
            messageSendCounter += entry.getValue();
        }

        int messageReceivedCounter = 0;
        for(Map.Entry<String, Integer> entry: messagesReceived.entrySet()){
            messageReceivedCounter += entry.getValue();
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("messagesSend", messageSendCounter);
        map.put("messagesReceived", messageReceivedCounter);

        return map;
    }

    @Override
    public DoubleCountMethodMessageCounter getNewInstance(String nodeName) {
        return new DoubleCountMethodMessageCounter(nodeName);
    }

    @Override
    public void aggregate(Map<String, String> messageCountData) {
        sumMessagesReceived += Integer.parseInt(messageCountData.get("messagesReceived"));
        sumMessagesSend += Integer.parseInt(messageCountData.get("messagesSend"));
    }

    @Override
    public void reset() {
        lastSumMessagesReceived = sumMessagesReceived;
        lastSumMessagesSend = sumMessagesSend;
        sumMessagesReceived = 0;
        sumMessagesSend = 0;
    }

    @Override
    public boolean isTerminated() {
        return lastSumMessagesReceived != -1
                && lastSumMessagesReceived == lastSumMessagesSend
                && sumMessagesReceived == sumMessagesSend
                && lastSumMessagesReceived == sumMessagesReceived;
    }
}

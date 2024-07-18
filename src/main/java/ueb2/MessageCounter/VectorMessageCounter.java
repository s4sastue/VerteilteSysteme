package ueb2.MessageCounter;



import java.util.HashMap;
import java.util.Map;

public class VectorMessageCounter extends MessageCounter {
    private Map<String, Integer> vector = new HashMap<>();
    public VectorMessageCounter(String nodeName){
        super(nodeName);
    }

    @Override
    public Map<String, Integer> getMessageCounterValues(){

        int count = 0;
        for(Map.Entry<String, Integer> entry: messagesReceived.entrySet()){
            count -= entry.getValue();
        }

        Map<String, Integer> map = new HashMap<>(messagesSend);
        map.put(nodeName, count);

        return map;
    }

    @Override
    public VectorMessageCounter getNewInstance(String nodeName) {
        return new VectorMessageCounter(nodeName);
    }

    /**
     * Method with which the message vector is calculated
     *
     * @param messageCountData List representing the message vector
     */
    @Override
    public void aggregate(Map<String, String> messageCountData){
        for(Map.Entry<String, String> entry: messageCountData.entrySet()){
            String key = entry.getKey();
            int value = Integer.parseInt(entry.getValue());
            if(vector.containsKey(key)){
                value += vector.get(key);
            }
            vector.put(key, value);
        }
    }

    @Override
    public void reset(){
        vector = new HashMap<>();
    }

    /**
     * Method that can determine whether termination has occurred on the basis of the message vector
     *
     * @return true, if simulation is terminated, false else
     */
    @Override
    public boolean isTerminated(){
        for (Map.Entry<String, Integer> entry: vector.entrySet()){
            // all entries in the vector must be 0 if the simulation is terminated
            // if at least one value is different from 0 termination cannot have occurred
            if(entry.getValue() != 0){
                return false;
            }
        }

        return true;
    }
}

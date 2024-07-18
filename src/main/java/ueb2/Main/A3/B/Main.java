package ueb2.Main.A3.B;

import org.oxoo2a.sim4da.Simulator;
import ueb2.MessageCounter.MessageCounter;
import ueb2.MessageCounter.DoubleCountMethodMessageCounter;
import ueb2.Nodes.PyrotechnicianNode;
import ueb2.Nodes.ObserverNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Main executable for experiment of task 3 (use of the double count method)
 */
public class Main {
    public static void main(String[] args) {
        int simulationSize = 100;
        Simulator simulator = Simulator.getInstance();

        List<String> pyrotechnicianNodes = new ArrayList<>();
        for (int i = 0; i < simulationSize; i++) {
            String nodeName = "PyrotechnicianNode_" + i;
            pyrotechnicianNodes.add(nodeName);
        }

        // Observer is required to dedicate termination
        MessageCounter messageCounterType = new DoubleCountMethodMessageCounter(null);
        ObserverNode.getObserverNode(messageCounterType, pyrotechnicianNodes);

        for(String nodeName: pyrotechnicianNodes){
            new PyrotechnicianNode(nodeName, messageCounterType, pyrotechnicianNodes);
        }

        simulator.simulate();
        simulator.shutdown();
    }
}

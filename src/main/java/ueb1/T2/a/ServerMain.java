package ueb1.T2.a;

import ueb1.Server.Server;

public class ServerMain {
    public static void main(String[] args){
        Server s = new Server("ResultTable_T2a.csv", 44444, 7500);
        s.startGame();
    }
}

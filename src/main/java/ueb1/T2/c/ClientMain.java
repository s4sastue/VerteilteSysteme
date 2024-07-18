package ueb1.T2.c;

import ueb1.client.Client;

public class ClientMain {
    public static void main(String[] args) {
        Client c;

        for(int i=0; i<20000; i++){
            c = new Client("Client"+i, "127.0.0.1", 44444, 10000);
            c.start();
        }
    }
}

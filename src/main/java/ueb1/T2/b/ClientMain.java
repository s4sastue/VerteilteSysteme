package ueb1.T2.b;

import ueb1.client.Client;

public class ClientMain {
    public static void main(String[] args) {
        int i = 1;

        String[] names = {"Bohr", "Curie", "Einstein", "Heisenberg", "von Helmholtz", "Tesla", "Volta", "Maxwell", "Coulomb", "Fourier", "Ampère", "Kirchhoff", "Schröder"};
        Client c = new Client(names[i], "127.0.0.1", 44444, 10000);
        c.start();
    }
}

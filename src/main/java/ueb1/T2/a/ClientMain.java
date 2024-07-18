package ueb1.T2.a;

import ueb1.client.Client;

public class ClientMain {
    public static void main(String[] args) {
        int i = 6;

        String[] names = {"Bohr", "Curie", "Einstein", "Heisenberg", "von Helmholtz", "Tesla", "Volta", "Maxwell", "Coulomb", "Fourier", "Ampère", "Kirchhoff", "Schröder"};
        Client c = new Client(names[i], "192.168.178.65", 44444, 10000);
        c.start();
    }
}

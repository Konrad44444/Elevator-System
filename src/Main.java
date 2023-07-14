package src;

import src.simulation.Simulator;

public class Main {
    public static void main(String[] args) {
        try {

            Simulator simulator = new Simulator();

            simulator.runElevatorSystemSymulation();

        } catch (IllegalArgumentException e) {
            System.err.println("Błąd! --- " + e.getMessage());
        }
    }

    

}

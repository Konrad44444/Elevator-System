package src.elevator_system;

import java.util.List;

import src.elevator.Elevator;

public class ElevatorSystemDataPrinter {
    private ElevatorSystemDataPrinter() {}
    // funkcja wyświetla statusy wszystkich wind
    public static void printAllElevatorsStatus(ElevatorSystem es) {
        String line1Head = "Numer windy     \t";
        String line2Head = "Obecne piętro   \t";
        String line3Head = "Docelowe piętro \t";
        String line4Head = "Ilość pasażerów \t";

        StringBuilder line1 = new StringBuilder(line1Head);
        StringBuilder line2 = new StringBuilder(line2Head);
        StringBuilder line3 = new StringBuilder(line3Head);
        StringBuilder line4 = new StringBuilder(line4Head);

        List<List<Integer>> elevatorsStatus = es.getAllElevatorsStatus();

        elevatorsStatus.forEach( status -> {
            line1.append(status.get(0));
            line1.append("\t");
            line2.append(status.get(1));
            line2.append("\t");
            line3.append(status.get(2));
            line3.append("\t");
            line4.append(status.get(3));
            line4.append("\t");
        });

        System.out.println("\n"+ line1.toString());
        System.out.println(line2.toString());
        System.out.println(line3.toString());
        System.out.println(line4.toString() + "\n");
    }

    // funkcja wyswietla informacje o przydzielonej windzie
    public static void printCalledElevatorInfo(Elevator elevator) {
        System.out.println(ElevatorSystem.CALLED_ELEVATOR_STRING + elevator);
    }

    public static void printElevatorStatus(ElevatorSystem es, Integer elevatorID) {
        System.out.println(es.getElevatorStatusStringByID(elevatorID));
    }
}

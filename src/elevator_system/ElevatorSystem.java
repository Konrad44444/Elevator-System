package src.elevator_system;

import java.util.List;

import src.direction.Direction;
import src.elevator.Elevator;
import src.person.Person;

public interface ElevatorSystem {
    public static final String INVALID_NUMBER_ELEVATORS = "Niepoprawna ilość wind! Podaj liczbę z przedziału [2, 16].";
    public static final String INVALID_NUMBER_FLOORS = "Niepoprawna ilość pięter! Podaj liczbę większą od 4.";
    public static final String NO_ELEVATOR_FOUND = "Brak windy o podanym ID!";
    public static final String CALLED_ELEVATOR_STRING = "Przywołano windę! Twoja winda to ";

    List<List<Integer>> getAllElevatorsStatus();
    Elevator callAnElevator(Direction direction, Integer userFloor);
    String getElevatorStatusStringByID(Integer elevatorID);
    void makeSimulationStep();
    Elevator getElevatorById(Integer elevatorID);
    void addPersonToQueue(Elevator elevator, Person person);
    void checkWaitingPeople();

    // funkcja odnajduje windę (z listy wind), która znajduje się najbliżej podanego piętra
    public static int findNearestElevatorID(List<Elevator> elevators, Integer floor) {
        int elevatorID = 0;
        int min = Integer.MAX_VALUE;

        for(int i = 0; i < elevators.size(); i++) {
            int newMin = Math.abs(floor - elevators.get(i).getCurrentFloor());
            if( newMin < min) {
                min = newMin;
                elevatorID = i;
            }
        }

        return elevatorID;
    }

    // funkcja odnajduje windę (z listy wind), która kończy swój obieg najbliżej podanego piętra
    public static int findNearestEndingElevatorID(List<Elevator> elevators, Integer floor) {
        int elevatorID = 0;
        int min = Integer.MAX_VALUE;

        for(int i = 0; i < elevators.size(); i++) {
            int newMin = Math.abs(floor - elevators.get(i).getDestinationFloor());
            if( newMin < min) {
                min = newMin;
                elevatorID = i;
            }
        }

        return elevatorID;
    }
}

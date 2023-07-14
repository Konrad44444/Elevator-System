package src.simulation;

import src.direction.Direction;
import src.elevator.Elevator;
import src.elevator_system.ElevatorSystem;
import src.elevator_system.ElevatorSystemDataPrinter;
import src.elevator_system.ElevatorSystemImpl;
import src.person.Person;

public class Simulator {
    private static final String INSERT_ELEVATORS_AMOUNT = "Podaj ilość wind (od 2 do 16): ";
    private static final String INSERT_FLOORS_AMOUNT = "Podaj ilość pięter w budynku (min. 4): ";
    private static final String MENU_STRING = "Wybierz akcję:\n1. Wsiądź do windy\n2. Wykonaj krok symulacji\n3. Informacje o wybranej windzie\n4. Wygeneruj osobę losowo\n5. Zakończ\nWybór: ";
    private static final String INVALID_OPTION = "Błędny numer akcji.";
    private static final String CHOOSE_DIRECTION = "Podaj kierunek:\n1. Góra\n2. Dół\nWybór: ";
    private static final String YOUR_FLOOR = "Podaj piętro, na którym się znajdujesz: ";
    private static final String CHOOSE_FLOOR = "Podaj piętro, na które chcesz dojechać: ";
    private static final String GENERATED_PERSON_INFO = "Piętro początkowe i końcowe wygenerowanej osoby: ";
    private static final String ELEVATOR_CALLED_INFO = "Numer przywołanej windy: ";
    private static final String GET_ELEVATOR_ID = "Podaj numer windy: ";
    private static final String EMPTY_STRING = "";

    private static Integer elevatorsAmount;
    private static Integer floorsAmount;
    private static ElevatorSystem elevatorSystem;

    public Simulator() {
        // --- pobranie danych
        elevatorsAmount = DataGetter.getDataInteger(INSERT_ELEVATORS_AMOUNT);

        floorsAmount = DataGetter.getDataInteger(INSERT_FLOORS_AMOUNT);
        // jeśli podana ilość pięter 5 to piętra są od 0 do 5

        // --- działanie programu
        elevatorSystem = new ElevatorSystemImpl(floorsAmount, elevatorsAmount);
        ElevatorSystemDataPrinter.printAllElevatorsStatus(elevatorSystem);   

    }

    public void runElevatorSystemSymulation() {
        boolean loop = true;

        Elevator elevatorCalled;
        Direction direction;

        while(loop) {
            
            System.out.print(MENU_STRING);

            switch(DataGetter.getDataInteger(EMPTY_STRING)) {
                case 1:
                    // pobranie danych odnośnie kierunku jazdy
                    Integer chosenDirection = DataGetter.getDataInteger(CHOOSE_DIRECTION);
                
                    if(chosenDirection == 1) direction = Direction.UP;
                    else if(chosenDirection == 2) direction = Direction.DOWN;
                    else {
                        System.out.println(INVALID_OPTION);
                        break;
                    }

                    // pobranie danych odnośnie aktualnego piętra
                    Integer yourFloor = DataGetter.getDataInteger(YOUR_FLOOR);
                    if(yourFloor < 0 && yourFloor > floorsAmount) {
                        System.out.println(INVALID_OPTION);
                        break;
                    }

                    // wezwanie windy
                    elevatorCalled = elevatorSystem.callAnElevator(direction, yourFloor);

                    // winda jedzie po osobę
                    elevatorCalled.addStop(yourFloor);
                    
                    // pobranie danych odnośnie docelowego piętra
                    Integer chosenFloor = DataGetter.getDataInteger(CHOOSE_FLOOR);
                    if(chosenFloor < 0 && chosenFloor > floorsAmount) {
                        System.out.println(INVALID_OPTION);
                        break;
                    }

                    // utworzenie osoby, dodanie jej do kolejki i zaktualizowanie statusu windy
                    Person person = new Person(yourFloor, chosenFloor);
                    elevatorSystem.addPersonToQueue(elevatorCalled, person);

                    ElevatorSystemDataPrinter.printCalledElevatorInfo(elevatorCalled);

                    break;

                case 2: 
                    elevatorSystem.checkWaitingPeople();
                    elevatorSystem.makeSimulationStep();
                    ElevatorSystemDataPrinter.printAllElevatorsStatus(elevatorSystem); 
                    break;

                case 3:
                    System.out.print(GET_ELEVATOR_ID);
                    ElevatorSystemDataPrinter.printElevatorStatus(elevatorSystem, DataGetter.getDataInteger(EMPTY_STRING));

                    break;
                
                case 4:
                    Person generatedPerson = Helper.generatePerson(floorsAmount);

                    // określenie kierunku
                    if(generatedPerson.startingFloor() < generatedPerson.destinationFloor()) {
                        direction = Direction.UP;
                    } else {
                        direction = Direction.DOWN;
                    } 

                    // wezwanie windy i dodanie windy do kolejki
                    elevatorCalled = elevatorSystem.callAnElevator(direction, generatedPerson.destinationFloor());
                    elevatorCalled.addStop(generatedPerson.startingFloor());
                    elevatorSystem.addPersonToQueue(elevatorCalled, generatedPerson);

                    System.out.println(GENERATED_PERSON_INFO + generatedPerson.startingFloor() + " - " + generatedPerson.destinationFloor() + " " + ELEVATOR_CALLED_INFO + elevatorCalled.getId());

                    break;

                case 5:
                    loop = false;
                    break;
                
                default: 
                    System.out.println(INVALID_OPTION);
                    break;
            }

        }
    }
    
}

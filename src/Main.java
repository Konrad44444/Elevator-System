package src;
import java.util.Random;
import java.util.Scanner;

import src.direction.Direction;
import src.elevator.Elevator;
import src.elevator_system.ElevatorSystem;
import src.elevator_system.ElevatorSystemDataPrinter;
import src.elevator_system.ElevatorSystemImpl;
import src.person.Person;

public class Main {
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
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {

        try {

            runElevatorSystemSymulation();

            scanner.close();

        } catch (IllegalArgumentException e) {
            scanner.close();
            System.err.println("Błąd! --- " + e.getMessage());
        }
    }

    private static void runElevatorSystemSymulation() {
        // --- pobranie danych
        System.out.print(INSERT_ELEVATORS_AMOUNT);
        Integer elevatorsAmount = Integer.valueOf(scanner.nextInt()); 

        System.out.print(INSERT_FLOORS_AMOUNT);
        Integer floorsAmount = Integer.valueOf(scanner.nextInt());
        // jeśli podana ilość pięter 5 to piętra są od 0 do 5


        // --- działanie programu
        ElevatorSystem elevatorSystem = new ElevatorSystemImpl(floorsAmount, elevatorsAmount);
        ElevatorSystemDataPrinter.printAllElevatorsStatus(elevatorSystem);   

        boolean loop = true;

        Elevator elevatorCalled;
        Direction direction;

        while(loop) {
            
            System.out.print(MENU_STRING);

            switch(scanner.nextInt()) {
                case 1:
                    // pobranie danych odnośnie kierunku jazdy
                    System.out.print(CHOOSE_DIRECTION);
                    Integer chosenDirection = Integer.valueOf(scanner.nextInt());
                
                    if(chosenDirection == 1) direction = Direction.UP;
                    else if(chosenDirection == 2) direction = Direction.DOWN;
                    else {
                        System.out.println(INVALID_OPTION);
                        break;
                    }

                    // pobranie danych odnośnie aktualnego piętra
                    System.out.print(YOUR_FLOOR);
                    Integer yourFloor = Integer.valueOf(scanner.nextInt());
                    if(yourFloor < 0 && yourFloor > floorsAmount) {
                        System.out.println(INVALID_OPTION);
                        break;
                    }

                    // wezwanie windy
                    elevatorCalled = elevatorSystem.callAnElevator(direction, yourFloor);

                    // winda jedzie po osobę
                    elevatorCalled.addStop(yourFloor);
                    
                    // pobranie danych odnośnie docelowego piętra
                    System.out.print(CHOOSE_FLOOR);
                    Integer chosenFloor = Integer.valueOf(scanner.nextInt());
                    if(yourFloor < 0 && yourFloor > floorsAmount) {
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
                    ElevatorSystemDataPrinter.printElevatorStatus(elevatorSystem, scanner.nextInt());

                    break;
                
                case 4:
                    Person generatedPerson = generatePerson(floorsAmount);

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

    private static Person generatePerson(Integer floors) {
        Integer startFloor = Integer.valueOf(random.nextInt(floors));
        Integer endFloor = getRandomWithExclusion(random, 0, floors, startFloor);

        return new Person(startFloor, endFloor);
    }

    public static int getRandomWithExclusion(Random random, int start, int end, int... exclude) {
        int result = start + random.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (result < ex) {
                break;
            }
            result++;
        }
        return result;
    }
}

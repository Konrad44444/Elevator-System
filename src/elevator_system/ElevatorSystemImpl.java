package src.elevator_system;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.direction.Direction;
import src.elevator.Elevator;
import src.elevator.ElevatorImpl;
import src.person.Person;

public class ElevatorSystemImpl implements ElevatorSystem{
    private static final Integer ELEVATOR_SIZE = 6;

    // informacje o wszystkich windach
    private final List<Elevator> elevators;

    // zapisanie informacji o tym, która winda została przydzielona użytkownikowi
    private Map<Elevator, List<Person>> elevatorPersonMap;

    public ElevatorSystemImpl(Integer floors, Integer amountOfElevators) {
        // sprawdzenie poprawności danych
        if(amountOfElevators < 2 || amountOfElevators > 16) {
            throw new IllegalArgumentException(ElevatorSystem.INVALID_NUMBER_ELEVATORS + " ElevatorSystem()"); 
        }
        if(floors < 4) {
            throw new IllegalArgumentException(ElevatorSystem.INVALID_NUMBER_FLOORS + " ElevatorSystem()");
        }


        this.elevators = new ArrayList<>(amountOfElevators);
        this.elevatorPersonMap = new HashMap<>(amountOfElevators);

        // numery wind zaczynają się od 1, piętra od 0 do floors - 1
        for(int i = 1; i <= amountOfElevators; i++) {
            elevators.add(new ElevatorImpl(i, ELEVATOR_SIZE, floors));
            elevatorPersonMap.put(elevators.get(i-1), new ArrayList<>());
        }
    }

    // funkcja zwraca stan wszystkich wind zwrócony jako 
    public List<List<Integer>> getAllElevatorsStatus() {
        List<List<Integer>> statusList = new ArrayList<>(elevators.size());

        for(Elevator elevator : elevators) {
            List<Integer> integersList = new ArrayList<>(4);
            
            integersList.add(0, elevator.getId());
            integersList.add(1, elevator.getCurrentFloor());
            integersList.add(2, elevator.getDestinationFloor());
            integersList.add(3, elevator.getPeopleInElevator().size());

            statusList.add(integersList);
        }

        return statusList;
    }

    // algorytm wybrania odpowiedniej widny do przywołania i zwrócenie jej obiektu
    public Elevator callAnElevator(Direction direction, Integer userFloor) {
        // przypadek 1
        // - na piętrze z którego została przywołana winda znajduje się "winda bez celu"
        List<Elevator> elevatorsOnFLoor = elevators.stream()
            .filter(elevator -> elevator.getCurrentFloor().equals(elevator.getDestinationFloor()) && 
                                elevator.getCurrentFloor().equals(userFloor))
            .toList();
        
        if(!elevatorsOnFLoor.isEmpty()) {
            // zwrócenie pierwszej windy z listy
            return elevatorsOnFLoor.get(0);
        }

        // przypadek 2: nie ma nieaktywnej windy na piętrze 
        // - znalezienie windy która jest najbliżej piętra osoby i jedzie w tym samym kierunku co wybrany przez osobę 
        else {
            // wszystkie windy z poprawnym kierunkiem
            List<Elevator> elevatorsWithGoodDirection = elevators.stream()
                .filter(elevator -> elevator.getDirection().equals(direction))
                .toList();

            if(!elevatorsWithGoodDirection.isEmpty()) {

                try {
                    
                    // jeśli osoba chce jechać w górę - winda musi znajdować się na piętrze niższym lub tym samym co osoba
                    if(direction.equals(Direction.UP)) {
                        
                        List<Elevator> goodElevators  = elevatorsWithGoodDirection.stream()
                            .filter(elevator -> elevator.getCurrentFloor() <= userFloor)
                            .toList();

                        return goodElevators.get(ElevatorSystem.findNearestElevatorID(goodElevators, userFloor));
                    }
                    // osoba chce jechać w dół - odwrócenie warunku 
                    else {
                        
                        List<Elevator> goodElevators  = elevatorsWithGoodDirection.stream()
                            .filter(elevator -> elevator.getCurrentFloor() >= userFloor)
                            .toList();

                        return goodElevators.get(ElevatorSystem.findNearestElevatorID(goodElevators, userFloor));
                    }
                
                } catch(ArrayIndexOutOfBoundsException e) {
                    // przypadek 3: windy jadą w dobrych kierunkach ale minęły już piętro osoby
                    // - przywołanie windy która znajduje się najbliżej piętra osoby

                    return elevators.get(ElevatorSystem.findNearestEndingElevatorID(elevators, userFloor));
                } 
            }
            // przypadek 4: żadna winda nie jedzie w podanym kierunku
            // rozwiązanie jak w przypadku 3
            else {
                return elevators.get(ElevatorSystem.findNearestEndingElevatorID(elevators, userFloor));
            }
        }
    }

    public String getElevatorStatusStringByID(Integer elevatorID) {
        return getElevatorById(elevatorID).toString();
    }

    // funkcja aktualizuje stan wszystkich wind -> zmiana piętra, wypuszczenie/wpuszeczenie ludzi
    public void makeSimulationStep() {
        for(Elevator elevator : elevators)
            elevator.updateStatus();
    }

    // funkcja dająca dostęp do wybranej windy
    public Elevator getElevatorById(Integer elevatorID) {
        if(elevatorID > 0 && elevatorID <= elevators.size())
            // ID windy zaczyna się od 1
            return elevators.get(elevatorID - 1);
        else
            throw new IllegalArgumentException(NO_ELEVATOR_FOUND + " getElevatorById()");
    }

    // funkcja dodająca osobę do mapy która przechowuje windę oraz osoby które ją wezwały 
    public void addPersonToQueue(Elevator elevator, Person person) {
        elevatorPersonMap.get(elevator).add(person);
    }

    // funkcja sprawdza czy piętro windy pokrywa się z piętrem osoby na nią oczekującej i jeśli tak to dodaje osobę do winy oraz dodaje przystanek
    public void checkWaitingPeople() {
        for(Elevator elevator : elevators) {
            List<Person> listPerson = elevatorPersonMap.get(elevator);

            for(int i = 0; i < listPerson.size(); i++) {
                Person person = listPerson.get(i);
                if(person.startingFloor().equals(elevator.getCurrentFloor())) {
                    // "osoba wsiada i klika numer piętra na które chce dojechać"
                    elevator.getPeopleInElevator().add(person);
                    elevator.addStop(person.destinationFloor());

                    // usunięcie osoby z kolejki - zmiejszy się rozmiar kolejki więc i--
                    elevatorPersonMap.get(elevator).remove(person);
                    i--;
                }
            }
        }

    }
}

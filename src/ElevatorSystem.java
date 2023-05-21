package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElevatorSystem {
    public static final String INVALID_NUMBER_ELEVATORS = "Niepoprawna ilość wind! Podaj liczbę z przedziału [2, 16].";
    public static final String INVALID_NUMBER_FLOORS = "Niepoprawna ilość pięter! Podaj liczbę większą od 4.";
    public static final String NO_ELEVATOR_FOUND = "Brak windy o podanym ID!";
    public static final String CALLED_ELEVATOR_STRING = "Przywołano windę! Twoja winda to ";


    private static final Integer ELEVATOR_SIZE = 6;

    private final List<Elevator> elevators;

    private Map<Elevator, List<Person>> elevatorPersonMap;

    public ElevatorSystem(Integer floors, Integer amountOfElevators) {
        // sprawdzenie poprawności danych
        if(amountOfElevators < 2 || amountOfElevators > 16) {
            throw new IllegalArgumentException(INVALID_NUMBER_ELEVATORS + " ElevatorSystem()"); 
        }
        if(floors < 4) {
            throw new IllegalArgumentException(INVALID_NUMBER_FLOORS + " ElevatorSystem()");
        }


        this.elevators = new ArrayList<>(amountOfElevators);
        this.elevatorPersonMap = new HashMap<>(amountOfElevators);

        // numery wind zaczynają się od 1, piętra od 0 do floors - 1
        for(int i = 1; i <= amountOfElevators; i++) {
            elevators.add(new Elevator(i, ELEVATOR_SIZE, floors));
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

    // funkcja wyświetla statusy wszystkich wind
    public void printAllElevatorsStatus() {
        String line1Head = "Numer windy     \t";
        String line2Head = "Obecne piętro   \t";
        String line3Head = "Docelowe piętro \t";
        String line4Head = "Ilość pasażerów \t";

        StringBuilder line1 = new StringBuilder(line1Head);
        StringBuilder line2 = new StringBuilder(line2Head);
        StringBuilder line3 = new StringBuilder(line3Head);
        StringBuilder line4 = new StringBuilder(line4Head);

        List<List<Integer>> elevatorsStatus = getAllElevatorsStatus();

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

                        return goodElevators.get(findNearestElevatorID(goodElevators, userFloor));
                    }
                    // osoba chce jechać w dół - odwrócenie warunku 
                    else {
                        
                        List<Elevator> goodElevators  = elevatorsWithGoodDirection.stream()
                            .filter(elevator -> elevator.getCurrentFloor() >= userFloor)
                            .toList();

                        return goodElevators.get(findNearestElevatorID(goodElevators, userFloor));
                    }
                
                } catch(ArrayIndexOutOfBoundsException e) {
                    // przypadek 3: windy jadą w dobrych kierunkach ale minęły już piętro osoby
                    // - przywołanie windy która znajduje się najbliżej piętra osoby

                    return elevators.get(findNearestEndingElevatorID(elevators, userFloor));
                } 
            }
            // przypadek 4: żadna winda nie jedzie w podanym kierunku
            // rozwiązanie jak w przypadku 3
            else {
                return elevators.get(findNearestEndingElevatorID(elevators, userFloor));
            }
        }
    }

    // funkcja wyswietla informacje o przydzielonej windzie
    public void printCalledElevatorInfo(Elevator elevator) {
        System.out.println(CALLED_ELEVATOR_STRING + elevator);
    }

    public String getElevatorStatusStringByID(Integer elevatorID) {
        return getElevatorById(elevatorID).toString();
    }

    public void printElevatorStatus(Integer elevatorID) {
        System.out.println(getElevatorStatusStringByID(elevatorID));
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
                if(person.getStartFloor().equals(elevator.getCurrentFloor())) {
                    // "osoba wsiada i klika numer piętra na które chce dojechać"
                    elevator.getPeopleInElevator().add(person);
                    elevator.addStop(person.getDestinationFloor());

                    // usunięcie osoby z kolejki - zmiejszy się rozmiar kolejki więc i--
                    elevatorPersonMap.get(elevator).remove(person);
                    i--;
                }
            }
        }

    }

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

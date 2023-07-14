package src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Elevator {
    private final Integer id;
    private Integer currentFloor;
    private Integer destinationFloor;
    private Direction direction = null;
    private final Integer limitOfPeople;
    private List<Person> peopleInElevator = new ArrayList<>();
    private final Integer maxFloor;
    private List<Integer> route = new ArrayList<>();


    public Elevator(Integer id, Integer limitOfPeople, Integer maxFloor) {
        this.id = id;

        // początkowe wartości pięter dla windy są losowane
        Random random = new Random();
        this.currentFloor = random.nextInt(maxFloor);
        route.add(random.nextInt(maxFloor));
        this.destinationFloor = route.get(0);

        // nadanie odpowiedniego kierunku windzie
        if(currentFloor > route.get(0))  this.direction = Direction.DOWN;
        else this.direction = Direction.UP;

        this.limitOfPeople = limitOfPeople;
        this.maxFloor = maxFloor;
    }


    public Integer getId() {
        return this.id;
    }

    public Integer getCurrentFloor() {
        return this.currentFloor;
    }

    public void setCurrentFloor(Integer currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Integer getDestinationFloor() {
        return this.destinationFloor;
    }

    public void setDestinationFloor(Integer destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Integer getLimitOfPeople() {
        return this.limitOfPeople;
    }

    public List<Person> getPeopleInElevator() {
        return this.peopleInElevator;
    }

    public void setPeopleInElevator(List<Person> peopleInElevator) {
        this.peopleInElevator = peopleInElevator;
    }

    public Integer getMaxFloor() {
        return this.maxFloor;
    }

    public List<Integer> getRoute() {
        return this.route;
    }

    public void setRoute(List<Integer> route) {
        this.route = route;
    }


    @Override
    public String toString() {
        return "Winda numer " + this.getId() +
        ", aktualne piętro: " + this.getCurrentFloor() +
        ", piętro docelowe: " + this.getDestinationFloor() +
        ", ilość osób w windzie: " + this.getPeopleInElevator().size();
    }

    // funkcja aktualizująca status windy -> ruch windy, wysiadanie ludzi
    public void updateStatus() {
        if(!route.isEmpty()) {
            
            // określenie kierunku jazdy i ruch windy
            if(currentFloor < route.get(0)) {
                // ruch w górę
                direction = Direction.UP;
                currentFloor++;
            } else if (currentFloor > route.get(0)) {
                // ruch w dół
                direction = Direction.DOWN;
                currentFloor--;
            } else {
                // usunięcie przystanku z trasy, ponieważ winda dojechała na piętro
                // winda zatrzyma się na jedną turę
                route.remove(0);
                
            }
            
            // znalezienie wszystkich ludzi z piętrem docelowym równym aktualnemu piętru windy
            List<Person> peopleToLeave = peopleInElevator.stream()
                .filter(person -> person.destinationFloor().equals(currentFloor))
                .collect(Collectors.toList());

            //  "wyjście" ludzi z windy
            peopleInElevator.removeAll(peopleToLeave);

        }
    }

    public void addStop(Integer floor) {
        if(floor < 0 || floor > maxFloor) {
            throw new IllegalArgumentException(ElevatorSystem.INVALID_NUMBER_FLOORS + " addStop(), podane piętro: " + floor);
        }

        // dodanie przystanku do trasy jeśli nie istnieje
        if(!route.contains(floor)) route.add(floor);

        // nadanie kierunku windzie w zależności od wezwania
        if(currentFloor < floor) {
            direction = Direction.UP;
        } else {
            direction = Direction.DOWN;
        }

        // posortowanie kolejności przystanków
        if(direction.equals(Direction.UP)) {
            // jeśli jazda w górę to posortowanie rosnąco
            Collections.sort(route, (i1, i2) -> i1- i2);
        } else {
            // jazda w dół - posortowanie malejąco
            Collections.sort(route, (i1, i2) -> i2- i1);
        }


        // ostatni element listy - nowe piętro docelowe
        destinationFloor = route.get(route.size() - 1);

    }
}

package src.elevator;

import java.util.List;

import src.direction.Direction;
import src.person.Person;

public interface Elevator {
    void updateStatus();
    void addStop(Integer floor);
    
    Integer getId();
    Integer getCurrentFloor();
    void setCurrentFloor(Integer currentFloor);
    Integer getDestinationFloor();
    void setDestinationFloor(Integer destinationFloor);
    Direction getDirection();
    void setDirection(Direction direction);
    Integer getLimitOfPeople();
    List<Person> getPeopleInElevator();
    void setPeopleInElevator(List<Person> peopleInElevator);
    Integer getMaxFloor();
    List<Integer> getRoute();
    void setRoute(List<Integer> route);
}

package src.person;

import java.util.Objects;

public record Person(Integer startingFloor, Integer destinationFloor) {
    public Person {
        Objects.requireNonNull(startingFloor);
        Objects.requireNonNull(destinationFloor);
    }
}

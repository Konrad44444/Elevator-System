package src;
public class Person {
    private Integer startFloor;
    private Integer destinationFloor;

    public Person() {
    }

    public Person(Integer startFloor, Integer endFloor) {
        this.startFloor = startFloor;
        this.destinationFloor = endFloor;
    }

    public Integer getStartFloor() {
        return this.startFloor;
    }

    public void setStartFloor(Integer startFloor) {
        this.startFloor = startFloor;
    }

    public Integer getDestinationFloor() {
        return this.destinationFloor;
    }

    public void setDestinationFloor(Integer endFloor) {
        this.destinationFloor = endFloor;
    }
}

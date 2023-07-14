package test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;

import src.direction.Direction;
import src.elevator.Elevator;    
import src.elevator.ElevatorImpl;    
    
public class ElevatorTest {
    private Elevator elevator = new ElevatorImpl(1, 4, 4);
        
    @Test
    public void testAddStop() {
        assertEquals(1, elevator.getRoute().size());

        elevator.addStop(2);
        assertEquals(2, elevator.getRoute().size());
    }

    @Test 
    public void testInvalidFloorPassedToAddStopMethod() throws Exception {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> elevator.addStop(10)),
            () -> assertThrows(IllegalArgumentException.class, () -> elevator.addStop(-2))
        );
    }

    @Test
    public void testUpdateStatus() throws Exception {
        Integer oldFloor = elevator.getCurrentFloor();

        // jeśli piętro docelowe i obecne są takie same brak ruchu windy
        if(elevator.getCurrentFloor().equals(elevator.getDestinationFloor())) {
            elevator.updateStatus();
            assertEquals(oldFloor, elevator.getCurrentFloor());
        } else {    
            elevator.updateStatus();
            
            if(elevator.getDirection().equals(Direction.UP)) {
                assertEquals(oldFloor + 1, elevator.getCurrentFloor());
            } else {
                assertEquals(oldFloor - 1, elevator.getCurrentFloor());
            }
        }
    }
}
    
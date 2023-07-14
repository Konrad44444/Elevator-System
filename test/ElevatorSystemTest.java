package test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Test;

import src.elevator.Elevator;
import src.elevator.ElevatorImpl;
import src.elevator_system.ElevatorSystem;
import src.elevator_system.ElevatorSystemImpl;   
    
public class ElevatorSystemTest {
        
    @Test
    public void passValidDataToContructorThenOK() throws Exception {
        assertDoesNotThrow(() -> new ElevatorSystemImpl(10, 10));
    }

    @Test
    public void passInvalidElevatorAmountThenException() throws Exception {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ElevatorSystemImpl(10, -1)
        );

        assertEquals(exception.getMessage(), ElevatorSystem.INVALID_NUMBER_ELEVATORS);
    }

    @Test
    public void passInvalidFloorAmountThenException() throws Exception {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ElevatorSystemImpl(-1, 10)
        );

        assertEquals(exception.getMessage(), ElevatorSystem.INVALID_NUMBER_FLOORS);
    }

    @Test
    public void passBothInvalidDataThenException() throws Exception {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ElevatorSystemImpl(-1, 100)
        );
    }

    @Test
    public void findNearestElevatorIDTest() throws Exception {
        // given
        Elevator e1 = new ElevatorImpl(1, 4, 4);
        e1.setCurrentFloor(1);

        Elevator e2 = new ElevatorImpl(2, 4, 4);
        e1.setCurrentFloor(4);

        List<Elevator> elevatorList = List.of(e1, e2);

        // when
        Elevator elevatorFound = elevatorList.get(ElevatorSystem.findNearestElevatorID(elevatorList, 2));

        // then
        assertEquals(elevatorFound.getId(), 1);
    }

    @Test
    public void findNearestEndingElevatorIDTest() throws Exception {
        // given
        Elevator e1 = new ElevatorImpl(1, 4, 4);
        e1.setDestinationFloor(1);

        Elevator e2 = new ElevatorImpl(2, 4, 4);
        e1.setDestinationFloor(4);

        List<Elevator> elevatorList = List.of(e1, e2);

        // when
        Elevator elevatorFound = elevatorList.get(ElevatorSystem.findNearestEndingElevatorID(elevatorList, 2));

        // then
        assertEquals(elevatorFound.getId(), 1);
    }

    @Test
    public void testGetElevatorByIdWhenInvalidIdPassed() throws Exception {
        ElevatorSystem elevatorSystem = new ElevatorSystemImpl(10, 10);

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> elevatorSystem.getElevatorById(-1)),
            () -> assertThrows(IllegalArgumentException.class, () -> elevatorSystem.getElevatorById(-11))
            );
    }

    

}
    
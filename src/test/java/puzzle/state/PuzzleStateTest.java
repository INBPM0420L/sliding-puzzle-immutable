package puzzle.state;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleStateTest {
    private static final PuzzleState STATE_1;
    private static final PuzzleState STATE_2;
    private static final PuzzleState STATE_3;
    private static final PuzzleState STATE_4;

    static {
        STATE_1 = PuzzleState.DEFAULT_STATE; // the original initial state
        STATE_2 = new PuzzleState(List.of(
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 2))); // a goal state
        STATE_3 = new PuzzleState(List.of(
                new Position(1, 1),
                new Position(2, 0),
                new Position(1, 1),
                new Position(0, 2))); // a non-goal state
        STATE_4 = new PuzzleState(List.of(
                new Position(0, 0),
                new Position(1, 0),
                new Position(0, 1),
                new Position(0, 0))); // a dead-end state with no legal moves
    }

    @Test
    void testConstructor_invalid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PuzzleState(List.of(new Position(0, 0))));
        assertThrows(
                IllegalArgumentException.class,
                () -> new PuzzleState(List.of(
                        new Position(0, 0),
                        new Position(1, 1),
                        new Position(2, 2),
                        new Position(3, 3)))
        );
        assertThrows(IllegalArgumentException.class, () -> new PuzzleState(List.of(new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 1))));
    }

    @Test
    void isGoal() {
        assertFalse(STATE_1.isGoal());
        assertTrue(STATE_2.isGoal());
        assertFalse(STATE_3.isGoal());
        assertFalse(STATE_4.isGoal());
    }

    @Test
    void canMove_state1() {
        assertFalse(STATE_1.canMove(Position.Direction.UP));
        assertTrue(STATE_1.canMove(Position.Direction.RIGHT));
        assertTrue(STATE_1.canMove(Position.Direction.DOWN));
        assertFalse(STATE_1.canMove(Position.Direction.LEFT));
    }

    @Test
    void canMove_state2() {
        assertTrue(STATE_2.canMove(Position.Direction.UP));
        assertFalse(STATE_2.canMove(Position.Direction.RIGHT));
        assertTrue(STATE_2.canMove(Position.Direction.DOWN));
        assertTrue(STATE_2.canMove(Position.Direction.LEFT));
    }

    @Test
    void canMove_state3() {
        assertTrue(STATE_3.canMove(Position.Direction.UP));
        assertTrue(STATE_3.canMove(Position.Direction.RIGHT));
        assertTrue(STATE_3.canMove(Position.Direction.DOWN));
        assertTrue(STATE_3.canMove(Position.Direction.LEFT));
    }

    @Test
    void canMove_state4() {
        assertFalse(STATE_4.canMove(Position.Direction.UP));
        assertFalse(STATE_4.canMove(Position.Direction.RIGHT));
        assertFalse(STATE_4.canMove(Position.Direction.DOWN));
        assertFalse(STATE_4.canMove(Position.Direction.LEFT));
    }
}

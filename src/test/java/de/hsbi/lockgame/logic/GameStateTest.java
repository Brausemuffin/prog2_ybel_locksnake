package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    // =====================================================
    // TEST FIXTURES
    // =====================================================

    private Level emptyLevel() {

        CellType[][] cells = {
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY}
        };

        return new Level(
            3,
            3,
            cells,
            List.of(),
            new Position(1, 1)
        );
    }
    private Level wallLevel() {

        CellType[][] cells = {
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.WALL, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY}
        };

        return new Level(
            3,
            3,
            cells,
            List.of(),
            new Position(0, 0)
        );
    }
    // =====================================================
    // TESTS
    // =====================================================

    @Test
    void givenNewGameState_whenCreated_thenStatusIsRunning() {

        GameState state = new GameState(emptyLevel());

        assertEquals(
            GameState.Status.RUNNING,
            state.status()
        );
    }

    @Test
    void givenDirectionRight_whenTick_thenSnakeMovesRight() {

        GameState state = new GameState(emptyLevel());

        state.setPendingDirection(Direction.RIGHT);

        state.tick();

        assertEquals(
            new Position(2, 1),
            state.snake().head()
        );
    }

    @Test
    void givenNoDirection_whenTick_thenSnakeDoesNotMove() {

        GameState state = new GameState(emptyLevel());

        state.tick();

        assertEquals(
            new Position(1, 1),
            state.snake().head()
        );
    }

    @Test
    void givenMoveOutOfBounds_whenTick_thenGameLost() {

        GameState state = new GameState(emptyLevel());

        state.setPendingDirection(Direction.UP);

        state.tick();
        state.tick();

        assertEquals(
            GameState.Status.LOST_OUT_OF_BOUNDS,
            state.status()
        );
    }
    @Test
    void givenWallAhead_whenTick_thenSnakeDoesNotMove() {

        GameState state = new GameState(wallLevel());

        state.setPendingDirection(Direction.RIGHT);

        state.tick();

        assertEquals(
            new Position(0, 0),
            state.snake().head()
        );
    }
    @Test
    void givenSuccessfulMove_whenTick_thenSnakeGrows() {

        GameState state = new GameState(emptyLevel());

        state.setPendingDirection(Direction.RIGHT);

        state.tick();

        assertEquals(
            2,
            state.snake().body().size()
        );
    }
    @Test
    void givenDirectionUpdate_whenSetPendingDirection_thenDirectionStored() {

        GameState state = new GameState(emptyLevel());

        state.setPendingDirection(Direction.LEFT);

        assertEquals(
            Direction.LEFT,
            state.pendingDirection()
        );
    }
    @Test
    void givenGameState_whenStatusChecked_thenGameIsRunning() {

        GameState state = new GameState(emptyLevel());

        assertTrue(
            state.status().isRunning()
        );
    }
    @Test
    void givenSelfCollision_whenTick_thenGameLost() {

        Snake snake = new Snake(
            List.of(
                new Position(1,1),
                new Position(2,1),
                new Position(2,2),
                new Position(1,2)
            )
        );

        CellType[][] cells = {
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY}
        };

        Level level = new Level(
            3,
            3,
            cells,
            List.of(),
            new Position(1,1)
        );

        GameState state = new GameState(level);

        state.setSnake(snake);

        state.setPendingDirection(Direction.RIGHT);

        state.tick();

        assertEquals(
            GameState.Status.LOST_SELF_COLLISION,
            state.status()
        );
    }
    @Test
    void givenCorrectPinDirection_whenTick_thenPinActivated() {

        Pin pin = new Pin(
            new Position(2,1),
            Pin.State.LOW,
            Direction.RIGHT
        );

        CellType[][] cells = {
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY}
        };

        Level level = new Level(
            3,
            3,
            cells,
            new java.util.ArrayList<>(List.of(pin)),
            new Position(1,1)
        );

        GameState state = new GameState(level);

        state.setPendingDirection(Direction.RIGHT);

        state.tick();

        assertTrue(
            state.pins().get(0).state().isSet()
        );
    }
    @Test
    void givenWrongPinDirection_whenTick_thenSnakeBlocked() {

        Pin pin = new Pin(
            new Position(2,1),
            Pin.State.LOW,
            Direction.LEFT
        );

        CellType[][] cells = {
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY}
        };

        Level level = new Level(
            3,
            3,
            cells,
            new java.util.ArrayList<>(List.of(pin)),
            new Position(1,1)
        );

        GameState state = new GameState(level);

        state.setPendingDirection(Direction.RIGHT);

        state.tick();

        assertEquals(
            new Position(1,1),
            state.snake().head()
        );
    }
    @Test
    void givenAllPinsActivated_whenTick_thenGameWon() {

        Pin pin = new Pin(
            new Position(2,1),
            Pin.State.LOW,
            Direction.RIGHT
        );

        CellType[][] cells = {
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY, CellType.EMPTY}
        };

        Level level = new Level(
            3,
            3,
            cells,
            new java.util.ArrayList<>(List.of(pin)),
            new Position(1,1)
        );

        GameState state = new GameState(level);

        state.setPendingDirection(Direction.RIGHT);

        state.tick();

        assertEquals(
            GameState.Status.WON,
            state.status()
        );
    }
}



package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.model.*;

import java.util.List;


public final class GameState {
    private Snake snake;
    private Level level;
    private int score;
    private boolean running;
    private Status status;
    private Direction pendingDirection;


    public GameState(Level level) {
        this.level = level;

        this.snake = new Snake(
            List.of(level.snakeStart())
        );

        this.running = true;

        this.score = 0;
        this.status = Status.RUNNING;

        this.pendingDirection = Direction.NONE;
    }

    public Level level() {
        return level;
    }


    public Snake snake() {
        return snake;
    }


    public List<Pin> pins() {
        return level.pins();
    }

    public Status status() {
        return status;
    }

    public Direction pendingDirection() {
        return pendingDirection;
    }


    public GameState tick() {

        // early exit
        if (!running || pendingDirection == Direction.NONE) {
            return this;
        }

        Position next = snake.nextHead(pendingDirection);

        // out of bounds
        if (!level.inBounds(next)) {

            running = false;

            status = Status.LOST_OUT_OF_BOUNDS;

            return this;
        }

        // self collision
        boolean selfCollision = snake.body()
            .stream()
            .anyMatch(p -> p.equals(next));

        if (selfCollision) {

            running = false;

            status = Status.LOST_SELF_COLLISION;

            return this;
        }

        // wall collision
        if (level.isWall(next)) {

            pendingDirection = Direction.NONE;

            return this;
        }

        // actual movement
        for (int i = 0; i < level.pins().size(); i++) {

            Pin pin = level.pins().get(i);

            if (pin.position().equals(next)) {

                // richtiger LOW-Pin
                if (!pin.state().isSet()
                    && pin.activationDirection() == pendingDirection) {

                    level.pins().set(
                        i,
                        pin.withState(Pin.State.HIGH)
                    );

                    // gewinnen?
                    boolean allPinsSet = level.pins()
                        .stream()
                        .allMatch(p -> p.state().isSet());

                    if (allPinsSet) {
                        status = Status.WON;
                        running = false;
                    }

                    return this;
                }

                // sonst blockiert
                pendingDirection = Direction.NONE;

                return this;
            }
        }

        snake = snake.move(pendingDirection);

        return this;
    }

    public enum Status {
        RUNNING,
        WON,
        LOST_SELF_COLLISION,
        LOST_OUT_OF_BOUNDS;

        public boolean isRunning() {
            return this == RUNNING;
        }
    }

    public void setPendingDirection(Direction direction) {
        this.pendingDirection = direction;
    }
    public void setSnake(Snake snake) {
        this.snake = snake;
    }
}

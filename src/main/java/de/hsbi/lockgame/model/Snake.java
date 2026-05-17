package de.hsbi.lockgame.model;

import java.util.ArrayList;
import java.util.List;

public final class Snake {
  private final List<Position> body;

  public Snake(List<Position> body) {
    if (body == null || body.isEmpty())
      throw new IllegalArgumentException("Snake: body must not be null/empty");
    body = List.copyOf(body);
    this.body = body;
  }
    private Direction direction;

    public Position head() {
        return body.get(0);
    }
    public Direction direction() {
        return direction;
    }
  public List<Position> body() {
    return body;
  }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

  public Position nextHead(Direction d) {
    return d.applyTo(head());
  }

  public boolean occupies(Position position) {
    return body.contains(position);
  }

  public Snake grow(Direction d) {
    var newHead = nextHead(d);
    var newBody = new ArrayList<Position>(body.size() + 1);
    newBody.add(newHead);
    newBody.addAll(body);
    return new Snake(newBody);
  }
    public Snake move(Direction d) {

        var newHead = nextHead(d);

        var newBody = new ArrayList<Position>();

        newBody.add(newHead);

        newBody.addAll(body);

        return new Snake(newBody);
    }
}

package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.model.Direction;
import de.hsbi.lockgame.model.Level;
import de.hsbi.lockgame.ui.GamePanel;

// TODO: Die GameEngine verwaltet den GameState.

// TODO: Die GameEngine wird durch den Timer im main() getriggert ("tick") und lässt den GameState
// daraufhin einen Schritt ausführen. Dann müssen alle für den GameState registrierten Observer
// benachrichtigt werden, damit das Spielfeld neu gezeichnet werden kann o.ä.

// TODO: Die GameEngine beobachtet die Tastatureingaben (gesetzt in GamePanel.setupKeyBindings()),
// die in Direction übersetzt und an GameEngine.update() übergeben werden. Wenn es eine neue Eingabe
// gibt, wird die "update"-Methode von GameEngine aufgerufen, und die GameEngine muss die
// Blickrichtung der Schlange aktualisieren und diese GameState-Änderung den für den GameState
// registrierten Observer mitteilen.

// TODO: Die GameEngine ist ein Observer für Direction: GameEngine.update(Direction)
// TODO: Die GameEngine ist ein Observable für GameState: GamePanel.update(GameState)
public final class GameEngine {
    private final GameState state;
    private GamePanel panel;

    public GameEngine(Level level) {
        this.state = new GameState(level);
    }

    public GameState state() {
        return state;
    }

    public void setGamePanel(GamePanel panel) {
        this.panel = panel;
  }

    public void update(Direction d) {

        state.setPendingDirection(d);

        if (panel != null) {
            panel.update(state);
        }
    }

    public void tick() {

        state.tick();

        if (panel != null) {
            panel.update(state);
        }
    }
}

import java.util.ArrayList;
import java.util.List;

interface GameListener {
    void onEvent(String text);
}

class GameEventBus {
    List<GameListener> listeners = new ArrayList<GameListener>();

    void add(GameListener listener) {
        listeners.add(listener);
    }

    void send(String text) {
        for (GameListener listener : listeners) {
            listener.onEvent(text);
        }
    }
}

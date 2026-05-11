package game;

import java.util.ArrayList;
import java.util.List;

public class Observer {
    public static class Event {
        public String type;
        public String text;
        public int value;

        public Event(String type, String text, int value) {
            this.type = type;
            this.text = text;
            this.value = value;
        }
    }

    public interface Listener {
        void onEvent(Event event);
    }

    public static class Bus {
        List<Listener> listeners = new ArrayList<Listener>();

        public void add(Listener listener) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        public void send(Event event) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onEvent(event);
            }
        }
    }
}


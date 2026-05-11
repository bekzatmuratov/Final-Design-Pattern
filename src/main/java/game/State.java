package game;

public class State {
    public interface RunState {
        String id();

        boolean updateWorld();

        String title();

        String hint();
    }

    public static class Ready implements RunState {
        public String id() { return "ready"; }
        public boolean updateWorld() { return false; }
        public String title() { return "ГОТОВ"; }
        public String hint() { return "SPACE старт, G гараж"; }
    }

    public static class Playing implements RunState {
        public String id() { return "playing"; }
        public boolean updateWorld() { return true; }
        public String title() { return ""; }
        public String hint() { return ""; }
    }

    public static class Paused implements RunState {
        public String id() { return "paused"; }
        public boolean updateWorld() { return false; }
        public String title() { return "ПАУЗА"; }
        public String hint() { return "SPACE продолжить, ESC меню"; }
    }

    public static class Dead implements RunState {
        public String id() { return "dead"; }
        public boolean updateWorld() { return false; }
        public String title() { return "АВАРИЯ"; }
        public String hint() { return "SPACE заново, G гараж"; }
    }
}

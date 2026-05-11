package game;

public class Command {
    public interface Keys {
        void left();

        void right();

        void jump();

        void slide();

        void action();

        void garage();

        void tab();

        void menu();
    }

    public interface KeyCommand {
        void run(Keys keys);
    }

    public static class Left implements KeyCommand {
        public void run(Keys keys) { keys.left(); }
    }

    public static class Right implements KeyCommand {
        public void run(Keys keys) { keys.right(); }
    }

    public static class Jump implements KeyCommand {
        public void run(Keys keys) { keys.jump(); }
    }

    public static class Slide implements KeyCommand {
        public void run(Keys keys) { keys.slide(); }
    }

    public static class Action implements KeyCommand {
        public void run(Keys keys) { keys.action(); }
    }

    public static class Garage implements KeyCommand {
        public void run(Keys keys) { keys.garage(); }
    }

    public static class Tab implements KeyCommand {
        public void run(Keys keys) { keys.tab(); }
    }

    public static class Menu implements KeyCommand {
        public void run(Keys keys) { keys.menu(); }
    }
}


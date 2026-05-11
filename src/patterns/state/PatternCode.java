interface GameState {
    String title();

    boolean canMove();
}

class ReadyState implements GameState {
    public String title() {
        return "ГОТОВ";
    }

    public boolean canMove() {
        return false;
    }
}

class PlayingState implements GameState {
    public String title() {
        return "ИГРА";
    }

    public boolean canMove() {
        return true;
    }
}

class GameContext {
    GameState state = new ReadyState();

    void setState(GameState newState) {
        state = newState;
    }
}

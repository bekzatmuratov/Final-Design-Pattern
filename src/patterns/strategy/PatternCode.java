interface ScoreStrategy {
    int score(int distance);
}

class NormalScore implements ScoreStrategy {
    public int score(int distance) {
        return distance;
    }
}

class FastScore implements ScoreStrategy {
    public int score(int distance) {
        return distance * 2;
    }
}

class ScoreCounter {
    ScoreStrategy strategy;

    ScoreCounter(ScoreStrategy strategy) {
        this.strategy = strategy;
    }

    int count(int distance) {
        return strategy.score(distance);
    }
}

package game;


public class Strategy {
    public interface ScoreRule {
        int distance(float meters, CarSkin car, Location location, Decorator.Ride ride, boolean boost);

        int coin(CarSkin car, Location location, Decorator.Ride ride, boolean boost);
    }

    public static class RunnerScore implements ScoreRule {
        public int distance(float meters, CarSkin car, Location location, Decorator.Ride ride, boolean boost) {
            float boostValue = boost ? 1.75f : 1f;
            return (int) (meters * 0.32f * car.bonus * location.bonus * ride.score() * boostValue);
        }

        public int coin(CarSkin car, Location location, Decorator.Ride ride, boolean boost) {
            float boostValue = boost ? 1.35f : 1f;
            return Math.max(1, (int) (14 * car.bonus * location.bonus * ride.score() * boostValue));
        }
    }
}


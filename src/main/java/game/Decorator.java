package game;


public class Decorator {
    public interface Ride {
        String name();

        int shield();

        float score();
    }

    public static class BaseRide implements Ride {
        CarSkin car;

        public BaseRide(CarSkin car) {
            this.car = car;
        }

        public String name() { return car.name; }
        public int shield() { return 0; }
        public float score() { return 1f; }
    }

    public static class ShieldRide implements Ride {
        Ride ride;

        public ShieldRide(Ride ride) {
            this.ride = ride;
        }

        public String name() { return ride.name() + " shield"; }
        public int shield() { return ride.shield() + 1; }
        public float score() { return ride.score(); }
    }

    public static class ScoreRide implements Ride {
        Ride ride;

        public ScoreRide(Ride ride) {
            this.ride = ride;
        }

        public String name() { return ride.name() + " score"; }
        public int shield() { return ride.shield(); }
        public float score() { return ride.score() + 0.18f; }
    }

    public static Ride make(CarSkin car) {
        Ride ride = new BaseRide(car);
        if (car.id.equals("truck")) {
            ride = new ShieldRide(ride);
        }
        if (car.id.equals("cyber")) {
            ride = new ScoreRide(new ScoreRide(ride));
        }
        if (car.id.equals("fire")) {
            ride = new ScoreRide(new ScoreRide(ride));
        }
        if (car.id.equals("gold")) {
            ride = new ShieldRide(new ScoreRide(new ScoreRide(ride)));
        }
        return ride;
    }
}

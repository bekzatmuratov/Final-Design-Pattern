class PlayerCar {
    int lane = 1;
}

interface CarCommand {
    void execute(PlayerCar car);
}

class MoveLeftCommand implements CarCommand {
    public void execute(PlayerCar car) {
        car.lane--;
    }
}

class MoveRightCommand implements CarCommand {
    public void execute(PlayerCar car) {
        car.lane++;
    }
}

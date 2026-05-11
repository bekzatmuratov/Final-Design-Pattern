public class RunExample {
    public static void main(String[] args) {
        PlayerCar car = new PlayerCar();
        CarCommand right = new MoveRightCommand();
        CarCommand left = new MoveLeftCommand();

        right.execute(car);
        left.execute(car);

        System.out.println("Текущая полоса: " + car.lane);
    }
}

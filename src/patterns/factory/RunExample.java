public class RunExample {
    public static void main(String[] args) {
        FactoryCar player = CarFactory.create("player");
        FactoryCar rival = CarFactory.create("rival");

        System.out.println(player.name + " скорость: " + player.speed);
        System.out.println(rival.name + " скорость: " + rival.speed);
    }
}

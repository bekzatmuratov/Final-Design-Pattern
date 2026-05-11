public class RunExample {
    public static void main(String[] args) {
        CarAbility car = new ScoreDecorator(new ShieldDecorator(new BaseCarAbility()));

        System.out.println("Щит: " + car.shield());
        System.out.println("Бонус очков: x" + car.scoreBonus());
    }
}

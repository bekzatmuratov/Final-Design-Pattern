public class RunExample {
    public static void main(String[] args) {
        ScoreCounter normal = new ScoreCounter(new NormalScore());
        ScoreCounter fast = new ScoreCounter(new FastScore());

        System.out.println("Обычные очки: " + normal.count(100));
        System.out.println("Быстрая езда: " + fast.count(100));
    }
}

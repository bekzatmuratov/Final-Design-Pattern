public class RunExample {
    public static void main(String[] args) {
        GameEventBus bus = new GameEventBus();
        bus.add(text -> System.out.println("UI получил событие: " + text));

        bus.send("Игрок набрал 100 очков");
    }
}

public class RunExample {
    public static void main(String[] args) {
        RoadLocation forest = new RoadBuilder()
                .name("Лесная дорога")
                .traffic(3)
                .speed(130)
                .build();

        System.out.println(forest.name + ": трафик " + forest.traffic + ", скорость " + forest.speed);
    }
}

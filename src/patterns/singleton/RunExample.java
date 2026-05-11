public class RunExample {
    public static void main(String[] args) {
        SaveStorage first = SaveStorage.get();
        SaveStorage second = SaveStorage.get();

        first.bestScore = 777;

        System.out.println("Рекорд через второй объект: " + second.bestScore);
        System.out.println("Это один объект: " + (first == second));
    }
}

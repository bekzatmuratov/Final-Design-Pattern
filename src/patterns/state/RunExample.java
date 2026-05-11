public class RunExample {
    public static void main(String[] args) {
        GameContext game = new GameContext();
        System.out.println(game.state.title() + ", движение: " + game.state.canMove());

        game.setState(new PlayingState());
        System.out.println(game.state.title() + ", движение: " + game.state.canMove());
    }
}

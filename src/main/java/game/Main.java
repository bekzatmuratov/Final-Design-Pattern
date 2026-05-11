package game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Алматы Движ");
        config.setWindowedMode(960, 640);
        config.setResizable(true);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new TrafficGame2D(), config);
    }
}

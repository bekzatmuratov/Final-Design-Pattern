package game;

import java.util.LinkedHashSet;
import java.util.Set;

public class SaveData {
    public int coins = 0;
    public int best = 0;
    public int level = 1;
    public int runs = 0;
    public boolean storyWon = false;
    public String car = "starter";
    public String location = "metro";
    public Set<String> cars = new LinkedHashSet<String>();
    public Set<String> locations = new LinkedHashSet<String>();

    public SaveData() {
        cars.add("starter");
        locations.add("metro");
    }

    public boolean hasCar(String id) {
        return cars.contains(id);
    }

    public boolean hasLocation(String id) {
        return locations.contains(id);
    }
}

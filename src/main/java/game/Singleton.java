package game;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Singleton {
    private static SaveStore store = new SaveStore();

    public static SaveStore get() {
        return store;
    }

    public static class SaveStore {
        Path path = Paths.get("traffic-car-2d-save.properties");

        public SaveData load() {
            SaveData save = new SaveData();
            if (!Files.exists(path)) {
                return save;
            }
            try {
                Properties props = new Properties();
                InputStream in = Files.newInputStream(path);
                props.load(in);
                in.close();
                save.coins = Integer.parseInt(props.getProperty("coins", "0"));
                save.best = Integer.parseInt(props.getProperty("best", "0"));
                save.level = Integer.parseInt(props.getProperty("level", "1"));
                save.runs = Integer.parseInt(props.getProperty("runs", "0"));
                save.storyWon = Boolean.parseBoolean(props.getProperty("storyWon", "false"));
                save.car = props.getProperty("car", "starter");
                save.location = props.getProperty("location", "metro");
                fill(save.cars, props.getProperty("cars", "starter"));
                fill(save.locations, props.getProperty("locations", "metro"));
            } catch (Exception e) {
                save = new SaveData();
            }
            save.cars.add("starter");
            save.locations.add("metro");
            return save;
        }

        public void save(SaveData save) {
            try {
                Properties props = new Properties();
                props.setProperty("coins", "" + save.coins);
                props.setProperty("best", "" + save.best);
                props.setProperty("level", "" + save.level);
                props.setProperty("runs", "" + save.runs);
                props.setProperty("storyWon", "" + save.storyWon);
                props.setProperty("car", save.car);
                props.setProperty("location", save.location);
                props.setProperty("cars", join(save.cars));
                props.setProperty("locations", join(save.locations));
                OutputStream out = Files.newOutputStream(path);
                props.store(out, "Traffic Car 2D save");
                out.close();
            } catch (Exception e) {
                System.out.println("save failed " + e.getMessage());
            }
        }

        void fill(java.util.Set<String> set, String raw) {
            set.clear();
            String[] items = raw.split(",");
            for (int i = 0; i < items.length; i++) {
                if (items[i].trim().length() > 0) {
                    set.add(items[i].trim());
                }
            }
        }

        String join(java.util.Set<String> set) {
            String result = "";
            for (String item : set) {
                if (result.length() > 0) {
                    result += ",";
                }
                result += item;
            }
            return result;
        }
    }
}

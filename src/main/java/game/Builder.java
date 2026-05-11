package game;

import com.badlogic.gdx.graphics.Color;

public class Builder {
    public static class LocationBuilder {
        String id = "metro";
        String name = "Metro City";
        int price = 0;
        Color sky = Color.valueOf("70C7E8");
        Color fog = Color.valueOf("DDF4FF");
        Color road = Color.valueOf("303948");
        Color stripe = Color.WHITE;
        Color block = Color.valueOf("E55353");
        Color coin = Color.valueOf("FFC43D");
        Color side = Color.valueOf("86A3B8");
        float speed = 1f;
        float traffic = 1f;
        float bonus = 1f;
        String info = "Starter location";

        public LocationBuilder id(String value) {
            id = value;
            return this;
        }

        public LocationBuilder name(String value) {
            name = value;
            return this;
        }

        public LocationBuilder price(int value) {
            price = value;
            return this;
        }

        public LocationBuilder colors(String skyColor, String fogColor, String roadColor, String stripeColor) {
            sky = Color.valueOf(skyColor);
            fog = Color.valueOf(fogColor);
            road = Color.valueOf(roadColor);
            stripe = Color.valueOf(stripeColor);
            return this;
        }

        public LocationBuilder objects(String blockColor, String coinColor, String sideColor) {
            block = Color.valueOf(blockColor);
            coin = Color.valueOf(coinColor);
            side = Color.valueOf(sideColor);
            return this;
        }

        public LocationBuilder stats(float newSpeed, float newTraffic, float newBonus) {
            speed = newSpeed;
            traffic = newTraffic;
            bonus = newBonus;
            return this;
        }

        public LocationBuilder info(String value) {
            info = value;
            return this;
        }

        public Location build() {
            return new Location(id, name, price, sky, fog, road, stripe, block, coin, side, speed, traffic, bonus, info);
        }
    }
}


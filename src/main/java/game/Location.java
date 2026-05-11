package game;

import com.badlogic.gdx.graphics.Color;

public class Location {
    public String id;
    public String name;
    public int price;
    public Color sky;
    public Color fog;
    public Color road;
    public Color stripe;
    public Color block;
    public Color coin;
    public Color side;
    public float speed;
    public float traffic;
    public float bonus;
    public String info;

    public Location(String id, String name, int price, Color sky, Color fog, Color road, Color stripe,
            Color block, Color coin, Color side, float speed, float traffic, float bonus, String info) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sky = sky;
        this.fog = fog;
        this.road = road;
        this.stripe = stripe;
        this.block = block;
        this.coin = coin;
        this.side = side;
        this.speed = speed;
        this.traffic = traffic;
        this.bonus = bonus;
        this.info = info;
    }
}


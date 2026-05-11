package game;

import com.badlogic.gdx.graphics.Color;

public class CarSkin {
    public String id;
    public String name;
    public int price;
    public Color body;
    public Color glass;
    public int handling;
    public double bonus;
    public String info;

    public CarSkin(String id, String name, int price, Color body, Color glass, int handling, double bonus, String info) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.body = body;
        this.glass = glass;
        this.handling = handling;
        this.bonus = bonus;
        this.info = info;
    }
}


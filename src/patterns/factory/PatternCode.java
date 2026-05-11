class FactoryCar {
    String name;
    int speed;

    FactoryCar(String name, int speed) {
        this.name = name;
        this.speed = speed;
    }
}

class CarFactory {
    static FactoryCar create(String type) {
        if (type.equals("player")) {
            return new FactoryCar("Бекзат", 120);
        }
        if (type.equals("rival")) {
            return new FactoryCar("Ерасыл", 140);
        }
        return new FactoryCar("Обычная машина", 90);
    }
}

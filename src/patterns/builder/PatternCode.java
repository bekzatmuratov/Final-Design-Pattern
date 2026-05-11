class RoadLocation {
    String name;
    int traffic;
    int speed;

    RoadLocation(String name, int traffic, int speed) {
        this.name = name;
        this.traffic = traffic;
        this.speed = speed;
    }
}

class RoadBuilder {
    String name = "Город";
    int traffic = 1;
    int speed = 100;

    RoadBuilder name(String value) {
        name = value;
        return this;
    }

    RoadBuilder traffic(int value) {
        traffic = value;
        return this;
    }

    RoadBuilder speed(int value) {
        speed = value;
        return this;
    }

    RoadLocation build() {
        return new RoadLocation(name, traffic, speed);
    }
}

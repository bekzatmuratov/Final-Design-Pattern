interface CarAbility {
    int shield();

    double scoreBonus();
}

class BaseCarAbility implements CarAbility {
    public int shield() {
        return 0;
    }

    public double scoreBonus() {
        return 1.0;
    }
}

class ShieldDecorator implements CarAbility {
    CarAbility base;

    ShieldDecorator(CarAbility base) {
        this.base = base;
    }

    public int shield() {
        return base.shield() + 1;
    }

    public double scoreBonus() {
        return base.scoreBonus();
    }
}

class ScoreDecorator implements CarAbility {
    CarAbility base;

    ScoreDecorator(CarAbility base) {
        this.base = base;
    }

    public int shield() {
        return base.shield();
    }

    public double scoreBonus() {
        return base.scoreBonus() + 0.5;
    }
}

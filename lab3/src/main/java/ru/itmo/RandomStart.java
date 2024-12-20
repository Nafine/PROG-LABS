package ru.itmo;

import ru.itmo.enums.Gender;
import ru.itmo.enums.LifeQuality;

public class RandomStart {
    public static Gender randGender() {
        double r = Math.random() * 3;
        if (r <= 1.0) return Gender.MALE;
        if (r <= 2.0) return Gender.FEMALE;
        return Gender.ATTACK_HELICOPTER;
    }

    public static LifeQuality randLifeQuality() {
        double r = Math.random() * 4;
        if (r <= 1.0) return LifeQuality.EXTRA_GOOD;
        if (r <= 2.0) return LifeQuality.GOOD;
        if (r <= 3.0) return LifeQuality.BAD;
        return LifeQuality.EXTRA_BAD;
    }
}

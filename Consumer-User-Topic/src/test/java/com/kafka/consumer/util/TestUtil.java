package com.kafka.consumer.util;

import lombok.experimental.UtilityClass;
import org.jeasy.random.EasyRandom;

@UtilityClass
public class TestUtil {
    private final EasyRandom generator = new EasyRandom();

    public <T> T nextObject(Class<T> classToRandomize) {
        return generator.nextObject(classToRandomize);
    }
}

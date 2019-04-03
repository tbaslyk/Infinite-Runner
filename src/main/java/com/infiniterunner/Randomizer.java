package com.infiniterunner;

import java.util.Random;

/**
 * @author Tomasz Baslyk
 */
public class Randomizer {

    private static final Random randomizer = new Random();

    public static int randomizeNumber(int range) {

        // Generates a number from 0-(range-1), then adds +1 onto it
        int generatedNumber = randomizer.nextInt(range) + 1;

        // Returns the value
        return generatedNumber;
    }
}

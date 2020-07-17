package de.istkorrekt.microstream;

import lombok.Value;

import java.util.Random;

@Value
public class BigFatData {

    byte[] data;

    public static BigFatData random() {
        Random random = new Random();
        byte[] data = new byte[10_000_000];
        random.nextBytes(data);
        return new BigFatData(data);
    }
}

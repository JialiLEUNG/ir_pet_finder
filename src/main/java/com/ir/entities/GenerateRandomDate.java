package com.ir.entities;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

/**
 * Generate a random date for ES to do the ranking based on dates
 */
public class GenerateRandomDate {

    public GenerateRandomDate() {
    }

    public Date generateDate(){
        Random  rnd = new Random();
        Date dt;
        long ms;

        // Get an Epoch value roughly between 1940 and 2010
        // -946771200000L = January 1, 1940
        long currentTimestamp = Instant.now().toEpochMilli();
        // minus up to 1 years to it (using modulus on the next long)
        ms = currentTimestamp - (Math.abs(rnd.nextLong()) % (1L * 365 * 24 * 60 * 60 * 1000));

        dt = new Date(ms);
        // Construct a date
        return dt;
    }
}

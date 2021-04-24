package com.ir.entities;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

public class GenerateRandomDate {

    public GenerateRandomDate() {
    }

    public Date generateDate(){
//        // Declare DateTimeFormatter with desired format
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//
//        // Save current LocalDateTime into a variable
//        LocalDateTime localDateTime = LocalDateTime.now();
//
//        // Format LocalDateTime into a String variable and print
//        String formattedLocalDateTime = localDateTime.format(dateTimeFormatter);
//        System.out.println("Current Date: " + formattedLocalDateTime);
//
//        //Get random amount of days between 0 and 365
//        Random random = new Random();
//        int randomAmountOfDays = random.nextInt(365 - 0 + 1) + 0;
//        System.out.println("Random amount of days: " + randomAmountOfDays);
//
//        // Add randomAmountOfDays to LocalDateTime variable we defined earlier and store it into a new variable
//        LocalDateTime futureLocalDateTime = localDateTime.minusDays(randomAmountOfDays);
//
//        // Format new LocalDateTime variable into a String variable and print
//        String formattedFutureLocalDateTime = futureLocalDateTime.format(dateTimeFormatter);
//        System.out.println("Date " + randomAmountOfDays + " days in future: " + formattedFutureLocalDateTime);

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

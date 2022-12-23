package com.example.messanger.security;

import java.util.Random;

public class NumericStringUtils {
    private final static String NUMBERS = "1234567890";

    public static String generateNumbericString(int length){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i =0; i<length; i++){
            builder.append(NUMBERS.charAt(random.nextInt(NUMBERS.length() -1)));
        }
        return builder.toString();
    }

}

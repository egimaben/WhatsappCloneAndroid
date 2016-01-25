package io.cloudboost.whatsapp;

import java.util.Random;

public class StaticData {

    private static final Random RANDOM = new Random();

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.dp_1;
            case 1:
                return R.drawable.dp_2;
            case 2:
                return R.drawable.dp_3;
                
            case 3:
                return R.drawable.dp_4;
            case 4:
                return R.drawable.dp_5;
        }
    }

    public static final String[] names = {
            "John Doe", "Nawaz Dhandala", "Bengi Egima", "Ranjeet Kumar", "Buhiire Ken",
            "Jane Daisy", "Bill Gates", "John Wick", "Barrack Obama"
    };

}

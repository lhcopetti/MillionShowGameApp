package com.example.luiscopetti.millionshowgameapp;

/**
 * Created by Luis Copetti on 24/05/2015.
 */
final public class Util
{
    private Util()
    {

    }

    public static String timeToString(int timeS)
    {
        int seconds = timeS % 60;
        int minutes = (timeS - seconds) / 60;

        return minutes + "min " + seconds + "seg";
    }

    public static String prizeToString(int prize)
    {
        int[] units =
                { 1000000, 1000 };
        int[] values = new int[2];
        String[] words =
                { "M", "K" };

        if ( prize < units[units.length-1])
            return String.valueOf(prize);

        int currentValue = prize;

        for( int i = 0; i < units.length; i++ )
        {
            int div;
            if ((div = currentValue / units[i]) > 0)
            {
                values[i] = div;
                currentValue -= div * units[i];
            }

        }

        StringBuilder sb = new StringBuilder();

        for( int i = 0; i < values.length; i++ )
            if (values[i] != 0) sb.append(values[i] + words[i] + " ");

        return sb.toString().trim();
    }
}

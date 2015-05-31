package com.example.luiscopetti.millionshowgameapp;

/**
 * Created by Luis Copetti on 23/05/2015.
 */
public class Score implements Comparable<Score> {

    private String name;
    private int prize;
    private int timeS;
    private int numberOfQuestions;

    public Score(String name, int prize, int timeS, int numberOfQuestions)
    {
        this.name = name;
        this.prize = prize;
        this.timeS = timeS;
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getPrize()
    {
        return this.prize;
    }

    public String getName()
    {
        return this.name;
    }

    public int getTimeS() { return this.timeS; }

    public int getNumberOfQuestions() { return this.numberOfQuestions; }

    @Override
    public int compareTo(Score another) {

        if ( this.getPrize() == another.getPrize())
            return this.getTimeS() - another.getTimeS();

        return another.getPrize() - this.getPrize();

    }

    @Override
    public boolean equals(Object o) {
        if ( ! (o instanceof Score ))
            return false;
        Score s = (Score) o;

        return s.getName().equals(this.getName());
    }
}

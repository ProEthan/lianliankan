package com.mh.view;

public class Score {
    public String name;
    public int time;
    public int sc;

    public Score(){
    }

    public Score(String name,int time,int sc){
        this.name=name;
        this.time=time;
        this.sc=sc;
    }
    public String getName(){
        return name;
    }
    public int getTime(){
        return time;
    }
    public int getSc(){
        return sc;
    }
}


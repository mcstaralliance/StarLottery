package com.mcstaralliance.starlottery.manager;

public class Delay {
    private boolean delayed;
    private int time;

    public Delay(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }
}
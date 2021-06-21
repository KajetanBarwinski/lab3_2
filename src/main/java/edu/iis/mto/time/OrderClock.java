package edu.iis.mto.time;

import java.time.LocalDateTime;

public class OrderClock implements ClockInterface{

    @Override
    public LocalDateTime getTime() {
        return LocalDateTime.now();
    }
}

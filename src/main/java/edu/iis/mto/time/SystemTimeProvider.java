package edu.iis.mto.time;

import java.sql.Time;
import java.time.LocalDateTime;

public class SystemTimeProvider implements TimeProvider {

    @Override
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}

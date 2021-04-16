package edu.iis.mto.time;

import java.time.LocalDateTime;

public interface TimeProvider {
    public LocalDateTime getCurrentTime();
}

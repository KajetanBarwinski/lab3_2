package edu.iis.mto.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class FakeClock extends Clock { //klasa w znaczącym stopniu wzorowana na tej z linku

    private final Instant WHEN_STARTED = Instant.now();
    private final ZoneId DEFAULT_TIMEZONE = ZoneId.systemDefault();

    private int daysOffset = 0;
    private int hoursOffset = 0;
    private int minutesOffset = 0;
    private int secondsOffset = 0;

    public FakeClock withDaysOffset(int daysOffset) {
        this.daysOffset = daysOffset;
        return this;
    }

    public FakeClock withHoursOffset(int hoursOffset) {
        this.hoursOffset = hoursOffset;
        return this;
    }

    public FakeClock withMinutesOffset(int minutesOffset) {
        this.minutesOffset = minutesOffset;
        return this;
    }

    public FakeClock withSecondsOffset(int secondsOffset) {
        this.secondsOffset = secondsOffset;
        return this;
    }

    @Override
    public ZoneId getZone() {
        return DEFAULT_TIMEZONE;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return Clock.fixed(WHEN_STARTED, zone);
    }

    @Override
    public Instant instant() {
        return nextInstant();
    }

    private Instant nextInstant() {
        return WHEN_STARTED.plus(daysOffset, ChronoUnit.DAYS).plus(hoursOffset, ChronoUnit.HOURS).plus(minutesOffset, ChronoUnit.MINUTES).plus(secondsOffset, ChronoUnit.SECONDS);
    }

}

package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


class OrderTest {

    private Order order;
    private Clock clock;


    @BeforeEach
    void setUp() throws Exception {
        clock = Mockito.mock(Clock.class);
        order = new Order(clock);
    }

    private void getClockMockWithConfirmTimeOffsetBy(int offsetDuration) {
        when(clock.instant()).thenReturn(Instant.now(), Instant.now().plus(offsetDuration, ChronoUnit.HOURS));
    }

    @Test
    void OrderNotExpiredInstantly() {
        getClockMockWithConfirmTimeOffsetBy(0);
        order.submit();
        assertDoesNotThrow(order::confirm);
    }

    @Test
    public void orderNotExpiredAfterHoursLessThanValidPeriod() {
        getClockMockWithConfirmTimeOffsetBy(8);
        order.submit();
        assertDoesNotThrow(order::confirm);
    }

    @Test
    public void orderNotExpiredOnValidPeriod() {
        getClockMockWithConfirmTimeOffsetBy(24);
        order.submit();
        assertDoesNotThrow(order::confirm);
    }

    @Test
    public void orderExpiredOnHoursMoreThanValidPeriod() {
        getClockMockWithConfirmTimeOffsetBy(25);
        order.submit();

        assertThrows(OrderExpiredException.class, order::confirm);
    }
}

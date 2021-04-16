package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Or;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @Mock
    Clock clock;
    Order order;
    Instant startTime;

    @BeforeEach
    void setUp() {
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clock);
        startTime = Instant.now();
    }

    @Test
    void confirmShouldThrowExceptionIfConfirmationTimeIsExceeded() {
        Instant timeExceeded = startTime.plus(25, ChronoUnit.HOURS);

        when(clock.instant()).thenReturn(startTime).thenReturn(timeExceeded);

        order.submit();
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(Order.State.CANCELLED, order.getOrderState());
    }

}

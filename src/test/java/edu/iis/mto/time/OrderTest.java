package edu.iis.mto.time;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    Clock clock;
    private Instant t1;
    private Order order;
    private final long VALUE_GREATER_THAN_VALID_PERIOD_HOUR=25;
    private final long VALUE_LESS_THAN_VALID_PERIOD_HOUR=24;
    @BeforeEach
    void setUp()  {
        t1=Instant.now();
        order=new Order(clock);
    }

    @Test
    void testShouldThrowOrderExpiredException() {
        when(clock.instant()).thenReturn(t1).thenReturn(t1.plus(VALUE_GREATER_THAN_VALID_PERIOD_HOUR, ChronoUnit.HOURS));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order.submit();
        assertThrows(OrderExpiredException.class, () -> order.confirm());
        assertThat(order.getOrderState(), Is.is(Order.State.CANCELLED));
    }

    @Test
    void testShouldSetConfirmedStatusWhenOrderIsNotExpired()
    {
        when(clock.instant()).thenReturn(t1).thenReturn(t1.plus(VALUE_LESS_THAN_VALID_PERIOD_HOUR, ChronoUnit.HOURS));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order.submit();
        order.confirm();
        assertThat(order.getOrderState(), Is.is(Order.State.CONFIRMED));
    }


}

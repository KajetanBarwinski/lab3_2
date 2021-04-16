package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

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
    void setUp(){
        clock = Mockito.mock(Clock.class);
        order = new Order(clock);
    }

    @Test
    public void orderNotExpiredImmediately(){
        Mockito.when(clock.instant()).thenReturn(Instant.now());
        order.submit();
        order.confirm();
        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    public void orderNotExpiredWithValidTime(){
        Mockito.when(clock.instant()).thenReturn(Instant.now(), Instant.now().plus(24, ChronoUnit.HOURS));
        order.submit();
        order.confirm();
        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    public void OrderExpiredWithValidDate(){
        Mockito.when(clock.instant()).thenReturn(Instant.now(), Instant.now().plus(25, ChronoUnit.HOURS));
        order.submit();
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }
}

package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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


    @BeforeEach
    void setUp() throws Exception {}

    @Test
    void getOrderExpiredException() {

        Instant startTime = Instant.parse("2015-12-08T10:00:00Z");

        Order order = new Order(clock);
        Instant endTime = startTime.plus(Order.VALID_PERIOD_HOURS + 1, ChronoUnit.HOURS);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(startTime).thenReturn(endTime);

        OrderItem item = new OrderItem();

        order.addItem(item);
        order.submit();


        assertThrows(OrderExpiredException.class, order::confirm);
    }

    @Test
    void expiredOrderStatusTest(){
        Instant startTime = Instant.parse("2015-12-08T10:00:00Z");

        Order order = new Order(clock);
        Instant endTime = startTime.plus(Order.VALID_PERIOD_HOURS + 1, ChronoUnit.HOURS);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(startTime).thenReturn(endTime);

        order.submit();

        try {
            order.confirm();
        }catch (OrderExpiredException e)
        {
            assertEquals(order.getOrderState(), Order.State.CANCELLED);
        }
    }

    @Test
    void expiredOrderStatusConfirmed(){
        Instant startTime = Instant.parse("2015-12-08T10:00:00Z");

        Order order = new Order(clock);
        Instant endTime = startTime.plus(Order.VALID_PERIOD_HOURS , ChronoUnit.HOURS);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(startTime).thenReturn(endTime);

        order.submit();
        order.confirm();

        assertEquals(order.getOrderState(), Order.State.CONFIRMED);

    }

}

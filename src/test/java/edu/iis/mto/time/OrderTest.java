package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @Mock
    private ClockInterface fakeClock;

    private LocalDateTime defaultTime=LocalDateTime.of(2021,6,21,
            0,0,0);
    private Order order;

    @BeforeEach
    void setUp(){
        order= new Order(fakeClock);
    }

    @Test
    void OrderShouldBeInTime() {
        Mockito.when(fakeClock.getTime()).thenReturn(defaultTime);
        order.submit();

        Mockito.when(fakeClock.getTime()).thenReturn(defaultTime.plusHours(23));
        order.confirm();

        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    void OrderShouldNotBeInTime(){
        Mockito.when(fakeClock.getTime()).thenReturn(defaultTime);
        order.submit();

        Mockito.when(fakeClock.getTime()).thenReturn(defaultTime.plusDays(1).plusHours(1));

        assertThrows(OrderExpiredException.class,()-> order.confirm());
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }

}

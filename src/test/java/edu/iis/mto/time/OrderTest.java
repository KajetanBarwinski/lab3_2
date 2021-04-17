package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private CustomDateTime mock;

    Order order;
    FakeClock fakeClock;

    @BeforeEach
    void setUp() throws Exception {
        order = new Order(mock);
        fakeClock = new FakeClock();
    }

    @Test
    void timeExpiredTest1() {
        Mockito.when(mock.getTime()).thenReturn(LocalDateTime.now(fakeClock.withDaysOffset(1).withHoursOffset(1).withMinutesOffset(1).withSecondsOffset(1)));
        order.submit();
        assertThrows(OrderExpiredException.class, () -> {
            order.confirm();
        });
    }

    @Test
    void timeExpiredTest2() {
        Mockito.when(mock.getTime()).thenReturn(LocalDateTime.now(fakeClock.withDaysOffset(1).withHoursOffset(1).withMinutesOffset(1)));
        order.submit();
        assertThrows(OrderExpiredException.class, () -> {
            order.confirm();
        });
    }

    @Test
    void timeNotExpiredTest() {
        Mockito.when(mock.getTime()).thenReturn(LocalDateTime.now(fakeClock.withDaysOffset(1).withHoursOffset(1)));
        order.submit();
        order.confirm();
        assertSame(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    void timeNotExpiredTest2() {
        Mockito.when(mock.getTime()).thenReturn(LocalDateTime.now(fakeClock.withDaysOffset(1)));
        order.submit();
        order.confirm();
        assertSame(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    void regularTimeTest() {
        Mockito.when(mock.getTime()).thenReturn(LocalDateTime.now(fakeClock));
        order.submit();
        order.confirm();
        assertSame(order.getOrderState(), Order.State.CONFIRMED);
    }



}

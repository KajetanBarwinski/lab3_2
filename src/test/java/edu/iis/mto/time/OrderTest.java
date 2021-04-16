package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;


class OrderTest {

    @Mock
    private LocalDateTime fakeDateTime;

    Order order;
    FakeClock fakeClock;

    @BeforeEach
    void setUp() throws Exception {
        order = new Order();
        fakeClock = new FakeClock();
    }

    @Test
    void test() {
        Mockito.when(LocalDateTime.now(fakeClock)).thenReturn(LocalDateTime.now(fakeClock.withDaysOffset(1).withHoursOffset(1)));
        order.setFakeClock(fakeClock);
        order.submit();
        assertThrows(OrderExpiredException.class, () -> {
            order.confirm();
        });
    }

}

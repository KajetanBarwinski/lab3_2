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
    private Instant now;

    @BeforeEach
    void setUp() {
       this.now = Instant.now();
    }

    @Test
    void OrderExpirationTest() {
        Order order = new Order(clock);
        Instant endTime = now.plus( 25, ChronoUnit.HOURS);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(now).thenReturn(endTime);

        order.submit();

        assertThrows(OrderExpiredException.class, () -> order.confirm());
    }

}

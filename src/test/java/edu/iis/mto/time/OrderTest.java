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
    private Clock clock;
    private Order order;

    @BeforeEach
    void setUp() throws Exception {}

    @Test
    void expiredOrderTest() {
        order = new Order(clock);

        Instant submission = Instant.parse("2010-10-10T10:10:10.00Z");
        Instant confirmation = submission.plus(Order.VALID_PERIOD_HOURS + 1, ChronoUnit.HOURS);

        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(submission).thenReturn(confirmation);

        order.submit();
        assertThrows(OrderExpiredException.class, order::confirm);
    }


}



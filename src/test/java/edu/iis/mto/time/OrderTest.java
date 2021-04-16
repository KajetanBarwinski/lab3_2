package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
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
    void setUp() throws Exception {
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clock);
    }

    @Test
    void checkIfOrderConfirmationAfterTwentyFourHourThrowsOrderExpiredExceptions() {
        Instant submission = Instant.now();
        Instant confirmation = submission.plus(26, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(submission).thenReturn(confirmation);
        order.submit();
        assertThrows(OrderExpiredException.class,() -> order.confirm());
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }

    @Test
    void checkIfOrderCanBeConfirmedInValidTimeRange()
    {
        Instant submission = Instant.now();
        Instant confirmation = submission.plus(2, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(submission).thenReturn(confirmation);
        order.submit();
        order.confirm();
        assertEquals(order.getOrderState(), Order.State.CONFIRMED);
    }

    @Test
    void checkIfOrderConfirmationWithoutOrderSubmitThrowsOrderStateException()
    {
        assertThrows(OrderStateException.class, () ->order.confirm());
    }

    @Test
    void checkIfOrderRealizationWithoutConfirmationThrowsOrderStateException()
    {
        Instant submission = Instant.now();
        Instant confirmation = submission.plus(2, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(submission).thenReturn(confirmation);
        order.submit();
        assertThrows(OrderStateException.class, () ->order.realize());
    }
}

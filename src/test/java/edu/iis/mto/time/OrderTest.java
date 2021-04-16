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
    private static final long INVALID_PERIOD_HOURS = Order.VALID_PERIOD_HOURS + 1;
    @Mock
    private Clock clock;
    private Order order;


    @BeforeEach
    void setUp()  {
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clock);
    }

    @Test
    void ThrowOrderExpiredExceptionTest() {
        Instant submissionTime = Instant.EPOCH;
        Instant confirmationTime = submissionTime.plus(INVALID_PERIOD_HOURS, ChronoUnit.HOURS);
        Order.State expectedState = Order.State.CANCELLED;

        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(submissionTime).thenReturn(confirmationTime);
        order.submit();

        try{
            order.confirm();
        }catch (RuntimeException ignored){}

        assertEquals(expectedState, order.getOrderState());
    }

}

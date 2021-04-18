package edu.iis.mto.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Clock clock;

    private Instant correct;
    private Order order;

    @BeforeEach
    void setUp() {
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clock);
        correct = Instant.now();
    }

    @Test
    void shouldThrowOrderExpiredException_OneHourExceed() {
        //Given
        Instant exceed = Instant.now().plus(25, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();

        //Then
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(Order.State.CANCELLED, order.getOrderState());
    }

    @Test
    void shouldThrowOrderExpiredException_TwelveHourExceed() {
        //Given
        Instant exceed = Instant.now().plus(36, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();

        //Then
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(Order.State.CANCELLED, order.getOrderState());
    }

}

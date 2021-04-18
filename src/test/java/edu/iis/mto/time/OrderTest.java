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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Clock clock;

    private Instant correct;
    private Order order;

    @BeforeEach
    void setUp() {
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
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

    @Test
    void shouldThrowOrderStateException_ConfirmMethodWithCreatedState() {
        //Given
        Instant exceed = Instant.now().plus(20, ChronoUnit.HOURS);

        try {
            order.confirm();
            fail("Method should throw OrderStateException");
        } catch (OrderStateException ignored) {}
    }

    @Test
    void shouldThrowOrderStateException_SubmitMethodWithSubmittedState() {
        //Given
        Instant exceed = Instant.now().plus(20, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();

        //Then
        assertThrows(OrderStateException.class, order::submit);
    }

    @Test
    void shouldThrowOrderStateException_SubmitMethodWithConfirmedState() {
        //Given
        Instant exceed = Instant.now().plus(20, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();
        order.confirm();

        //Then
        assertThrows(OrderStateException.class, order::submit);
    }

    @Test
    void shouldThrowOrderStateException_AddItemMethodWithConfirmedState() {
        //Given
        Instant exceed = Instant.now().plus(20, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();
        order.confirm();

        //Then
        try {
            order.addItem(new OrderItem());
            fail("Method should throw OrderStateException");
        } catch (OrderStateException ignored) {}
    }

    @Test
    void shouldThrowOrderStateException_RealizeMethodWithSubmittedState() {
        //Given
        Instant exceed = Instant.now().plus(20, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();

        //Then
        assertThrows(OrderStateException.class, order::realize);
    }

    @Test
    void shouldThrowOrderStateException_RealizeMethodWithCreatedState() {
        assertThrows(OrderStateException.class, order::realize);
    }

    @Test
    void shouldSubmitOrder_TwentyFourHoursElapsed() {
        //Given
        Instant exceed = Instant.now().plus(24, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();

        //Then
        assertEquals(Order.State.SUBMITTED, order.getOrderState());
    }

    @Test
    void shouldSubmitOrder_TwentyThreeHoursElapsed() {
        //Given
        Instant exceed = Instant.now().plus(23, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();

        //Then
        assertEquals(Order.State.SUBMITTED, order.getOrderState());
    }

    @Test
    void shouldSubmitOrder_TwentyHoursElapsed() {
        //Given
        Instant exceed = Instant.now().plus(20, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();

        //Then
        assertEquals(Order.State.SUBMITTED, order.getOrderState());
    }

    @Test
    void shouldSubmitOrder_TenHoursElapsed() {
        //Given
        Instant exceed = Instant.now().plus(10, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(correct).thenReturn(exceed);

        //When
        order.submit();

        //Then
        assertEquals(Order.State.SUBMITTED, order.getOrderState());
    }

}

package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

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
    private TimeProvider fakeTime;

    private Order order;
    private LocalDateTime referenceTime = LocalDateTime.of(2021, 04, 16, 0, 0, 0);

    @BeforeEach
    void setUp(){
        order = new Order(fakeTime);
    }

    @Test
    void OrderExpireTimeExceeded() {
        Mockito.when(fakeTime.getCurrentTime()).thenReturn(
                referenceTime,
                referenceTime.plusDays(1).plusHours(1)
        );

        order.submit();

        Exception e = assertThrows(OrderExpiredException.class, () -> {
             order.confirm();
        });
    }

    @Test
    void OrderConfirmedWithinTimeLimit() {
        Order order = new Order(fakeTime);
        Mockito.when(fakeTime.getCurrentTime()).thenReturn(
                referenceTime,
                referenceTime.plusHours(23).plusMinutes(59).plusSeconds(59)
        );

        order.submit();
        order.confirm();
    }

    @Test
    void OrderConfirmedAtTheSameTimeAsSubmitting() {
        Order order = new Order(fakeTime);
        Mockito.when(fakeTime.getCurrentTime()).thenReturn(
                LocalDateTime.of(0, 1, 1, 0, 0, 0)
        );

        order.submit();
        order.confirm();
    }

    @Test
    void CheckIfConfirmAndSubmitAreCheckingTime(){
        Order order = new Order(fakeTime);
        Mockito.when(fakeTime.getCurrentTime()).thenReturn(
                LocalDateTime.of(0, 1, 1, 0, 0, 0)
        );

        order.submit();
        order.confirm();

        Mockito.verify(fakeTime, times(2)).getCurrentTime();
    }
}

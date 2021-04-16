package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    Clock clock;
	
    private Order order;
    private Instant submittionTime;
    
    @BeforeEach
    void setUp() {
    	order = new Order(clock);
    	when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    	submittionTime = Instant.now();
    }

    @Test
    void confirmShouldThrowOrderExpiredExceptionAndOrderStateShouldEqualCancelled() {
    	Instant expiredTime = submittionTime.plus(25, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(submittionTime).thenReturn(expiredTime);
        
        order.submit();
        
        assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(Order.State.CANCELLED, order.getOrderState());
    }
}

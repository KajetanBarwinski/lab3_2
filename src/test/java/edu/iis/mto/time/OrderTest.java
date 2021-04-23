package edu.iis.mto.time;

import edu.iis.mto.time.Order.State;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @Mock
    Clock clock;
    
    @BeforeEach
    void setUp() throws Exception {}

    @Test
    void test_order_canceled_1() {
        
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(Instant.parse("2020-01-01T12:00:00Z")).thenReturn(Instant.parse("2020-01-02T13:00:00Z"));
                
        Order order = new Order(clock);
        order.submit();
        
        try {
            order.confirm();
        }catch (OrderExpiredException e)
        {
            Assertions.assertTrue((order.getOrderState()).equals(State.CANCELLED));
        }
 
        Assertions.assertTrue((order.getOrderState()).equals(State.CANCELLED));
    }
    
    @Test
    void test_order_canceled_2() {
        
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(Instant.parse("2020-01-01T12:00:00Z")).thenReturn(Instant.parse("2021-01-01T12:00:00Z"));
                
        Order order = new Order(clock);
        order.submit();
        
        try {
            order.confirm();
        }catch (OrderExpiredException e)
        {
            Assertions.assertTrue((order.getOrderState()).equals(State.CANCELLED));
        }
 
        Assertions.assertTrue((order.getOrderState()).equals(State.CANCELLED));
    }
    
    @Test
    void test_order_not_canceled_1() {
        
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(Instant.parse("2020-01-01T12:00:00Z")).thenReturn(Instant.parse("2020-01-02T12:00:00Z"));
                
        Order order = new Order(clock);
        order.submit();              
        order.confirm();
        
        Assertions.assertTrue((order.getOrderState()).equals(State.CONFIRMED));
    }
    
    @Test
    void test_order_not_canceled_2() {
        
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(Instant.parse("2020-01-01T12:00:00Z")).thenReturn(Instant.parse("2020-01-01T12:00:10Z"));
                
        Order order = new Order(clock);
        order.submit();             
        order.confirm();
        
        Assertions.assertTrue((order.getOrderState()).equals(State.CONFIRMED));
    }

}

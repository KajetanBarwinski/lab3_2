package edu.iis.mto.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @BeforeEach
    void setUp() throws Exception {}

    @Test
    void test() {
        Clock clock = Clock.fixed(Instant.parse("2030-12-22T10:15:30.00Z"), ZoneId.of("UTC"));
 

        LocalDateTime dateTime = LocalDateTime.now(clock);

    
        Order order = new Order();
        order.submit();
        
        LocalDateTime expected = LocalDateTime.of(2030, Month.DECEMBER, 22, 10, 15, 30);

        
        Assertions.assertTrue((dateTime).equals(expected));
    }

}

package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @Mock
    private Clock clock;


    @BeforeEach
    void setUp() throws Exception {}

    @Test
    public void shouldThrowExceptionWhenOrderExpired(){
        Instant confirmationTime = Instant.parse("2020-12-07T09:00:00Z");
        Instant submissionTime = Instant.parse("2020-12-06T08:00:00Z");
        Mockito.when(this.clock.instant()).thenReturn(submissionTime).thenReturn(confirmationTime);
        Mockito.when(this.clock.getZone()).thenReturn(ZoneId.systemDefault());
        Order order = new Order(this.clock);

        order.submit();
        try {
            order.confirm();
            fail("Exception was not thrown");
        } catch (OrderExpiredException ignored) {}

    }
    @Test
    public void shouldNotThrowExceptionWhenOrderExpired(){
        Instant confirmationTime = Instant.parse("2020-12-06T09:00:00Z");
        Instant submissionTime = Instant.parse("2020-12-06T08:00:00Z");
        Mockito.when(this.clock.instant()).thenReturn(submissionTime).thenReturn(confirmationTime);
        Mockito.when(this.clock.getZone()).thenReturn(ZoneId.systemDefault());
        Order order = new Order(this.clock);

        order.submit();
        try {
            order.confirm();
        } catch (OrderExpiredException e) {
            fail("Exception was not thrown");
        }

    }

}

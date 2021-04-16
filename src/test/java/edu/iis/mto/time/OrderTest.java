package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;

class OrderTest {
    @BeforeEach
    void setUp() throws Exception {}

    @Test
    void ConfirmOrderPlacedOneDayAgo() {
        LocalDateTime target_date = LocalDateTime.now().minusDays(1).minusHours(1);
        Order order = new Order();
        order.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, order.getOrderState());
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(target_date);
            order.submit();
            assertEquals(Order.State.SUBMITTED, order.getOrderState());
        }
        Assertions.assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(Order.State.CANCELLED, order.getOrderState());
    }
    @Test
    void ConfirmOrderPlacedTenDaysAgo() {
        LocalDateTime target_date = LocalDateTime.now().minusDays(10).minusHours(1);
        Order order = new Order();
        order.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, order.getOrderState());
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(target_date);
            order.submit();
            assertEquals(Order.State.SUBMITTED, order.getOrderState());
        }
        Assertions.assertThrows(OrderExpiredException.class, order::confirm);
        assertEquals(Order.State.CANCELLED, order.getOrderState());
    }
    @Test
    void ConfirmOrderPlacedTommorow() {
        LocalDateTime target_date = LocalDateTime.now().plusDays(1).plusHours(1);
        Order order = new Order();
        order.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, order.getOrderState());
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(target_date);
            order.submit();
            assertEquals(Order.State.SUBMITTED, order.getOrderState());
        }
        order.confirm();
        assertEquals(Order.State.CONFIRMED, order.getOrderState());
    }
    @Test
    void ConfirmOrderPlacedTenDaysFromNow() {
        LocalDateTime target_date = LocalDateTime.now().plusDays(1).plusHours(1);
        Order order = new Order();
        order.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, order.getOrderState());
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(target_date);
            order.submit();
            assertEquals(Order.State.SUBMITTED, order.getOrderState());
        }
        order.confirm();
        assertEquals(Order.State.CONFIRMED, order.getOrderState());
    }
    @Test
    void ConfirmOrderPlacedNow() {
        LocalDateTime target_date = LocalDateTime.now();
        Order order = new Order();
        order.addItem(new OrderItem());
        assertEquals(Order.State.CREATED, order.getOrderState());
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(target_date);
            order.submit();
            assertEquals(Order.State.SUBMITTED, order.getOrderState());
        }
        order.confirm();
        assertEquals(Order.State.CONFIRMED, order.getOrderState());
    }
}

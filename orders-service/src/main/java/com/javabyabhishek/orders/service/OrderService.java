package com.javabyabhishek.orders.service;

import com.javabyabhishek.core.dto.Order;

import java.util.UUID;

public interface OrderService {
    Order placeOrder(Order order);
    void approveOrder(UUID orderId);
}

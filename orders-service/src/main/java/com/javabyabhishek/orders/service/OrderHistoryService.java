package com.javabyabhishek.orders.service;

import com.javabyabhishek.core.types.OrderStatus;
import com.javabyabhishek.orders.dto.OrderHistory;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    void add(UUID orderId, OrderStatus orderStatus);

    List<OrderHistory> findByOrderId(UUID orderId);
}

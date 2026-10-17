package com.javabyabhishek.payments.service;

import com.javabyabhishek.core.dto.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment process(Payment payment);
}

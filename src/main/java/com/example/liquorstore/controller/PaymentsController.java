package com.example.liquorstore.controller;

import com.example.liquorstore.dto.PaymentDto;
import com.example.liquorstore.service.PaymentsIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("payments/api/v1")
public class PaymentsController {
    @Autowired
    private PaymentsIntegrationService paymentService;

    @PostMapping("/stkPush")
    public ResponseEntity<?> stkPush(@RequestBody PaymentDto paymentDto) {
        try {
            return paymentService.stkPush(paymentDto.getMobileNumber(), paymentDto.getAmount());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

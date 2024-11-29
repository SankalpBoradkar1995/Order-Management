package com.process.payment.service.impl;

import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.process.payment.entity.PaymentEntity;
import com.process.payment.repository.PaymentRepository;
import com.process.payment.request.PaymentRequest;
import com.process.payment.service.PaymentService;

@Service
@ConditionalOnProperty(name = "payment.service", havingValue = "default", matchIfMissing = true)
public class DefaultPayment implements PaymentService{
	
	private final PaymentRepository paymentRepository;
	
	public DefaultPayment(PaymentRepository paymentRepository)
	{
		this.paymentRepository = paymentRepository;
	}

	@Override
	public String executepayment(PaymentRequest paymentRequest) {
		PaymentEntity processedPayment = getProcessedPayment(paymentRequest);
		paymentRepository.save(processedPayment);
		return processedPayment.getPaymentStatus();
	}
	
	private String generateRandomOrderId() {
		// Generate a random UUID and convert it to a string
		String randomOrderId = "TXN ID-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

		return randomOrderId;
	}

	private PaymentEntity getProcessedPayment(PaymentRequest paymentRequest)
	{
		Long phoneNumber = Long.parseLong(paymentRequest.getPhoneNumber());
		if(phoneNumber % 2==0)
		{
			return new PaymentEntity(paymentRequest.getOrderId(), "PAYMENT-SUCCESS", generateRandomOrderId());
		}
		else
			return new PaymentEntity(paymentRequest.getOrderId(), "PAYMENT-FAILED", generateRandomOrderId());
			
	}
}

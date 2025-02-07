package io.app.services;

import io.app.dto.ApiResponse;
import io.app.dto.FeesDto;
import io.app.dto.PaymentRequest;
import org.springframework.web.bind.annotation.RequestParam;

public interface FeesService {
    public ApiResponse recordPayment(long batchId, long studentId, PaymentRequest paymentRequest);
    public FeesDto getFeesByStudentAndBatch(long studentId,long batchId);
}

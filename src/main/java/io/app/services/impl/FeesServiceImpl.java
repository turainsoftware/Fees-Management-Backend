package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.FeesDto;
import io.app.dto.FeesHistoryDto;
import io.app.dto.PaymentRequest;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.NotAllowedException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Fees;
import io.app.model.FeesHistory;
import io.app.repository.FeesHistoryRepository;
import io.app.repository.FeesRepository;
import io.app.services.FeesService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeesServiceImpl implements FeesService {

    private final FeesRepository feesRepository;
    private final FeesHistoryRepository feesHistoryRepository;

    @Override
    public ApiResponse recordPayment(long batchId, long studentId, PaymentRequest paymentRequest) {
        Fees fees=feesRepository.findByStudentIdAndBatchId(studentId,batchId)
                .orElseThrow(()->new ResourceNotFoundException("Fees not found for the batch or student"));
        YearMonth paymentYm=YearMonth.of(paymentRequest.getYear(),paymentRequest.getMonth());
        YearMonth startYm=YearMonth.of(fees.getStartYear(),fees.getEndMonth());
        YearMonth endYm=YearMonth.of(fees.getEndYear(),fees.getEndMonth());

//        if(paymentYm.isBefore(startYm) || paymentYm.isAfter(endYm)){
//            throw new NotAllowedException("Payment date is outside the fees period");
//        }

        boolean isPaymentExist=fees.getFeesHistories().stream()
                .anyMatch(h->h.getMonth()==paymentRequest.getMonth() && h.getYear()==paymentRequest.getYear());

        if (isPaymentExist){
            throw new DuplicateFoundException("Payment for this month and year already exists");
        }

        FeesHistory history=FeesHistory.builder()
                .fees(fees)
                .year(paymentRequest.getYear())
                .month(paymentRequest.getMonth())
                .amountPaid(paymentRequest.getAmountPaid())
                .paymentDate(new Date())
                .build();
        feesHistoryRepository.save(history);

        fees.setTotalPaid(fees.getTotalPaid()+paymentRequest.getAmountPaid());
        fees.setTotalDue(fees.getTotalFees()-fees.getTotalPaid());
        feesRepository.save(fees);
        return ApiResponse.builder()
                .message("Successfully paid")
                .status(true)
                .build();
    }

    @Override
    public FeesDto getFeesByStudentAndBatch(long studentId, long batchId) {
        Fees fees=feesRepository.findByStudentIdAndBatchId(studentId,batchId)
                .orElseThrow(()->new ResourceNotFoundException("Fees Details not found"));
        FeesDto feesDto=convertToFeesDto(fees);
        return feesDto;
    }

    public FeesDto convertToFeesDto(Fees fees){
        FeesDto feesDto=FeesDto.builder()
                .id(fees.getId())
                .studentId(fees.getStudent().getId())
                .batchId(fees.getBatch().getId())
                .startYear(fees.getStartYear())
                .startMonth(fees.getStartMonth())
                .endYear(fees.getEndYear())
                .endMonth(fees.getEndMonth())
                .totalFees(fees.getTotalFees())
                .totalPaid(fees.getTotalPaid())
                .totalDue(fees.getTotalDue())
                .monthlyFees(fees.getMonthlyFees())
                .build();

        List<FeesHistoryDto> feesHistoryDtos=fees.getFeesHistories()
                .stream().map(data->convertToFeesHistoryDto(data))
                .collect(Collectors.toList());
        feesDto.setFeesHistories(feesHistoryDtos);
        return feesDto;
    }

    public FeesHistoryDto convertToFeesHistoryDto(FeesHistory feesHistory){
        FeesHistoryDto feesHistoryDto=FeesHistoryDto.builder()
                .id(feesHistory.getId())
                .amountPaid(feesHistory.getAmountPaid())
                .description(feesHistory.getDescription())
                .month(feesHistory.getMonth())
                .year(feesHistory.getYear())
                .paymentDate(feesHistory.getPaymentDate())
                .build();
        return feesHistoryDto;
    }

}

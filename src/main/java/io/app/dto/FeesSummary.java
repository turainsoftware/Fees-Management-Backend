package io.app.dto;

public record FeesSummary(int year,
                          int month,
                          double totalAmountPaid) {
}

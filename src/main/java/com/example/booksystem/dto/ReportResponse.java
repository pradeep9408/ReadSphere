package com.example.booksystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponse {

    private int totalBooks;
    private double averagePrice;
    private double highestPrice;
    private String highestPriceBook;
    private double lowestPrice;
    private String lowestPriceBook;
    private int totalQuantity;
    private double totalCost;
}
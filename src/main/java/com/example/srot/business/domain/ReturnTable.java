package com.example.srot.business.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnTable {
    private SortedMap<Integer, ReturnRow> rows = new TreeMap<Integer, ReturnRow>();
    private Long lastPayout;
    private Long totalPayoutPreTax;
    private Long totalPayoutPostTax;
    private String returnPercentagePreTax;
    private String returnPercentagePostTax;
    private Double totalSolarAssets;

    public void addReturnRow(Integer index, ReturnRow row) {
        rows.put(index, row);
    }
}

package com.example.srot.business.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnRow {
    private Long grossPayout;
    private Long tds;
    private Long netPayout;
}

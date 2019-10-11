package com.mrfofo.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {
    private String id;
    private Product product;
    private String date;
    private Long amount;
    private String seat;
}

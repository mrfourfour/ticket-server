package com.mrfofo.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;
    private String name;
    private Long sellerId;
    private String image;
    private String category;
    private String subCategory;
    private String description;
    private String region;
    private String price;
    private ProductOption option;
}

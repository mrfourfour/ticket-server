package com.mrfofo.ticket.payload;

import com.mrfofo.ticket.model.Product;
import lombok.Data;

@Data
public class ReviewPayLoad {
    private String productId;
    private Product.Review review;
}
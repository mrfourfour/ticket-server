package com.mrfofo.ticket.objectmapper;

import com.mrfofo.ticket.model.Product;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Component
public class ProductMapper {
    public Product toProduct(Map<String, AttributeValue> map) {
        return Product.builder()
                .id(map.get("SK").s())
                .name(map.get("name").s())
                .sellerId(Long.parseLong(map.get("seller_id").n()))
                .image(map.get("image"))
                .build();
    }
}

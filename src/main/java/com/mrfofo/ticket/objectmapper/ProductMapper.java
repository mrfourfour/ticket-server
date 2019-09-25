package com.mrfofo.ticket.objectmapper;

import com.mrfofo.ticket.model.Product;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Component
public class ProductMapper implements DynamoDbMapper<Product>{
    public Product toObj(Map<String, AttributeValue> map) {
        return Product.builder()
                .id(map.get("SK").s())
                .name(map.get("name").s())
                .sellerId(map.get("seller_id").s())
                .image(map.get("image").s())
                .category(map.get("category").s())
                .subCategory(map.get("sub_category").s())
                .info(map.get("info").s())
                .area(map.get("area").s())
                .price(Long.parseLong(map.get("price").n()))
                .option(map.get("option").s())
                .build();
    }

    public Map<String, AttributeValue> toMap(Product product) {
        return Map.ofEntries(
                Map.entry("PK", AttributeValue.builder().s("Product").build()),
                Map.entry("SK", AttributeValue.builder().s(product.getId()).build()),
                Map.entry("name", AttributeValue.builder().s(product.getName()).build()),
                Map.entry("seller_id", AttributeValue.builder().s(product.getSellerId()).build()),
                Map.entry("image", AttributeValue.builder().s(product.getImage()).build()),
                Map.entry("category", AttributeValue.builder().s(product.getCategory()).build()),
                Map.entry("sub_category", AttributeValue.builder().s(product.getSubCategory()).build()),
                Map.entry("info", AttributeValue.builder().s(product.getInfo()).build()),
                Map.entry("area", AttributeValue.builder().s(product.getArea()).build()),
                Map.entry("price", AttributeValue.builder().n(String.valueOf(product.getPrice())).build()),
                Map.entry("option", AttributeValue.builder().s(product.getOption()).build())
        );
    }
}

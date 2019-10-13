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
                .category(Enum.valueOf(Product.ProductCategory.class, map.get("category").s()))
                .subCategory(Enum.valueOf(Product.ProductSubCategory.class, map.get("sub_category").s()))
                .info(map.get("info").s())
                .area(Product.ProductArea.valueOf(map.get("area").s()))
                .price(Long.parseLong(map.get("price").n()))
                .option(map.get("option").s())
                .date(map.get("date").s())
                .build();
    }

    public Map<String, AttributeValue> toMap(Product product) {
        return Map.ofEntries(
                Map.entry("PK", AttributeValue.builder().s("Product").build()),
                Map.entry("SK", AttributeValue.builder().s(product.getId()).build()),
                Map.entry("name", AttributeValue.builder().s(product.getName()).build()),
                Map.entry("seller_id", AttributeValue.builder().s(product.getSellerId()).build()),
                Map.entry("image", AttributeValue.builder().s(product.getImage()).build()),
                Map.entry("category", AttributeValue.builder().s(product.getCategory().getKey()).build()),
                Map.entry("sub_category", AttributeValue.builder().s(product.getSubCategory().getKey()).build()),
                Map.entry("info", AttributeValue.builder().s(product.getInfo()).build()),
                Map.entry("area", AttributeValue.builder().s(product.getArea().getKey()).build()),
                Map.entry("price", AttributeValue.builder().n(String.valueOf(product.getPrice())).build()),
                Map.entry("option", AttributeValue.builder().s(product.getOption()).build()),
                Map.entry("date", AttributeValue.builder().s(product.getDate()).build())
        );
    }
}

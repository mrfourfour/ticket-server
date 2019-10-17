package com.mrfofo.ticket.objectmapper;

import com.mrfofo.ticket.model.Product;
import com.mrfofo.ticket.model.Product.Review;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductMapper implements DynamoDbMapper<Product>{
    public Product toObj(Map<String, AttributeValue> map) {
        Product product = Product.builder()
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
        .reviews(map.get("reviews").l().parallelStream().map(AttributeValue::m).map(valueMap -> 
            Review.builder()
                .userId(valueMap.get("user_id").s())
                .title(valueMap.get("title").s())
                .description(valueMap.get("description").s())
                .rate(Long.parseLong(valueMap.get("rate").n()))
                .build()).collect(Collectors.toList()))
        .date(map.get("date").s())
        .build();

        product.setAverageRate(product.getReviews().parallelStream().mapToLong(Review::getRate).average().orElse(0));

        return product;
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
            Map.entry("reviews", AttributeValue.builder().l(product.getReviews().parallelStream().map(review -> 
                AttributeValue.builder().m(Map.ofEntries(
                    Map.entry("user_id", AttributeValue.builder().s(review.getUserId()).build()),
                    Map.entry("title", AttributeValue.builder().s(review.getTitle()).build()),
                    Map.entry("description", AttributeValue.builder().s(review.getDescription()).build()),
                    Map.entry("rate", AttributeValue.builder().n(String.valueOf(review.getRate())).build()))
                ).build()
            ).collect(Collectors.toList())).build()),
            Map.entry("date", AttributeValue.builder().s(product.getDate()).build())
        );
    }
}

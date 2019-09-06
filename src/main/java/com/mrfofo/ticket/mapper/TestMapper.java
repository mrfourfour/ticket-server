package com.mrfofo.ticket.mapper;

import com.mrfofo.ticket.model.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public class TestMapper {
    private static class TestMapperHolder {
        static final TestMapper instance = new TestMapper();
    }
    public static TestMapper getInstance() {
        return TestMapperHolder.instance;
    }

    public Map<String, AttributeValue> toMap(Test test) {
        return Map.of(
                "id", AttributeValue.builder().n(test.getId().toString()).build(),
                "title", AttributeValue.builder().s(test.getTitle()).build(),
                "description", AttributeValue.builder().s(test.getDescription()).build()
        );
    }

    public Test fromMap(Long key, Map<String, AttributeValue> map) {
        return Test.builder().id(key).title(map.get("title").s()).description(map.get("description").s()).build();
    }
}

package com.mrfofo.ticket.objectmapper;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public interface DynamoDbMapper<T> {
    T toObj(Map<String, AttributeValue> map);
    Map<String, AttributeValue> toMap(T t);
}

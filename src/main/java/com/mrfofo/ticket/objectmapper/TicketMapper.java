package com.mrfofo.ticket.objectmapper;

import com.mrfofo.ticket.model.Ticket;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Component
public class TicketMapper implements DynamoDbMapper<Ticket> {
    public Ticket toObj(Map<String, AttributeValue> map) {
        return Ticket.builder()
                .id(map.get("SK").s())
                .productId(map.get("product_id").s())
                .optionId(map.get("option_id").s())
                .userId(map.get("user_id").s())
                .amount(Integer.parseInt(map.get("amount").n()))
                .status(Enum.valueOf(Ticket.TicketStatus.class, map.get("status").s()))
                .date(map.get("date").s())
                .totalPrice(Long.parseLong(map.get("total_price").n()))
//                .qrData(map.get("qr_data").s())
                .build();
    }

    public Map<String, AttributeValue> toMap(Ticket ticket) {
        return Map.of(
                "PK", AttributeValue.builder().s("Ticket").build(),
                "SK", AttributeValue.builder().s(ticket.getId()).build(),
                "product_id", AttributeValue.builder().s(ticket.getProductId()).build(),
                "option_id", AttributeValue.builder().s(ticket.getOptionId()).build(),
                "user_id", AttributeValue.builder().s(ticket.getUserId()).build(),
                "status", AttributeValue.builder().s(ticket.getStatus().getKey()).build(),
                "amount", AttributeValue.builder().n(String.valueOf(ticket.getAmount())).build(),
                "total_price", AttributeValue.builder().n(String.valueOf(ticket.getTotalPrice())).build(),
                "date", AttributeValue.builder().s(ticket.getDate()).build()
//                "qr_data", AttributeValue.builder().s(ticket.getQrData()).build()
        );
    }
}

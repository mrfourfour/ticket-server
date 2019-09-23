package com.mrfofo.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    private String id;
    private String status;
    private int amount;
    private int totalPrice;
    private String productId;
    private String date;
    private String qrData;

    public enum TicketStatus  {
        ON_PROGRESS("사용중"),
        NOT_USED("미사용"),
        USED("사용됨"),
        CANCELED("취소");

        private String value;
        TicketStatus(String value) {
            this.value = value;
        }

        public String getKey() {
            return name();
        }
        public String getValue() {
            return value;
        }
    }
}

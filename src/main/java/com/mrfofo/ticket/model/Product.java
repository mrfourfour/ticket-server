package com.mrfofo.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;
    private String date;
    private String name;
    private String sellerId;
    private String image;
    private ProductCategory category;
    private ProductSubCategory subCategory;
    private String info;
    private ProductArea area;
    private Long price;
    private String option;

    public enum ProductCategory {
        TOUR("관광"),
        LEISURE("레져");

        private String value;
        ProductCategory(String category) {
            this.value = category;
        }

        public String getKey() { return name(); }
        public String getValue() { return value; }
    }

    public enum ProductSubCategory {
        GUE("뀨뀨"),
        MAX("맥스");

        private String value;
        ProductSubCategory(String subCategory) {
            this.value = subCategory;
        }

        public String getKey() { return name(); }
        public String getValue() { return value; }
    }
    public enum ProductArea {
        SEOUL("서울"),
        BUSAN("부산"),
        JEJU("제주");
        private String value;
        ProductArea(String value) {
            this.value = value;
        }
        public String getKey() { return name(); }
        public String getValue() { return value; }
    }
}

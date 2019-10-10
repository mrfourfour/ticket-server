package com.mrfofo.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
        LEISURE("레져"),
        ATTRACTION("어트랙션"),
        CONCERT("콘서트"),
        THEATER("연극");

        private String value;
        private ProductSubCategory productSubCategory;
        ProductCategory(String category, String productSubCategory) {
            this.value = category;
            setProductSubCategory(productSubCategory);
        }

        private void setProductSubCategory(String productSubCategory) {

        }

        public String getKey() { return name(); }
        public String getValue() { return value; }
    }
    public interface ProductSubCategory {

    }
//    public String getCategory() { return this.category.getValue(); }
    public enum TourSubCategory implements ProductSubCategory {
        GUE("뀨뀨"),
        MAX("맥스");
        private String value;
        TourSubCategory(String subCategory) {
            this.value = subCategory;
        }
        public String getKey() { return name(); }
        public String getValue() { return value; }
    }
    public enum ProductArea {
        SEOUL("서울"),
        INCHEON("인천"),
        DAEGU("대구"),
        DAEJEON("대전"),
        BUSAN("부산"),
        ULSAN("울산"),
        GWANGJU("광주"),
        GYEONGGI("경기"),
        GANGWON("강원"),
        CHUNGCHEONG("충청"),
        JEOLLA("전라"),
        GYEONGSANG("경상"),
        JEJU("제주");

        private String value;
        ProductArea(String value) {
            this.value = value;
        }
        public String getKey() { return name(); }
        public String getValue() { return value; }
    }
}

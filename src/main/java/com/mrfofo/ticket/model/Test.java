package com.mrfofo.ticket.model;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Table("test")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    private Long id;
    private String title;
    private String description;
}

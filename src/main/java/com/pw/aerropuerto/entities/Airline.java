package com.pw.aerropuerto.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "airlines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airline {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String code;
    private List<Flight> flights;

    @Builder.Default

}

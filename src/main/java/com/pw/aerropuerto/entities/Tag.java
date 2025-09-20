package com.pw.aerropuerto.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    Set<Flight> flights;

}

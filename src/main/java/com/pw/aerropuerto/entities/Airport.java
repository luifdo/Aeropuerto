package com.pw.aerropuerto.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@Table(name = "airports")
public class Airport {

    @Id @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;
    String Code;
    String Name;
    String City;

    @Builder.Default
}

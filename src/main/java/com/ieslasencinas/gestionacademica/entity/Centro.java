package com.ieslasencinas.gestionacademica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "centros")
@Getter
@Setter
@NoArgsConstructor
public class Centro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_centro")
    private Long idCentro;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String telefono;
}
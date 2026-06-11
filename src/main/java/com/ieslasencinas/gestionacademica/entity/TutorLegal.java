package com.ieslasencinas.gestionacademica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tutores_legales")
@Getter
@Setter
@NoArgsConstructor
public class TutorLegal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tutor_legal")
    private Long idTutorLegal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", unique = true, nullable = false)
    private Usuario usuario;

    @ManyToMany(mappedBy = "tutoresLegales", fetch = FetchType.LAZY)
    private Set<Alumno> alumnos = new HashSet<>();
}
package com.ieslasencinas.gestionacademica.mapper;

import com.ieslasencinas.gestionacademica.dto.TutorLegalResponseDTO;
import com.ieslasencinas.gestionacademica.entity.TutorLegal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class TutorLegalMapper {

    private final AlumnoMapper alumnoMapper;

    public TutorLegalResponseDTO toResponseDTO(TutorLegal tutor) {
        if (tutor == null) return null;

        return new TutorLegalResponseDTO(
                tutor.getIdTutorLegal(),
                tutor.getUsuario().getNombre(),
                tutor.getUsuario().getApellidos(),
                tutor.getUsuario().getEmail(),
                tutor.getAlumnos().stream()
                        .map(alumnoMapper::toResponseDTO)
                        .collect(Collectors.toList())
        );
    }
}

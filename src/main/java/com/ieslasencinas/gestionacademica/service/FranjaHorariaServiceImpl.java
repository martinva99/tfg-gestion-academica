package com.ieslasencinas.gestionacademica.service;

import com.ieslasencinas.gestionacademica.dto.FranjaHorariaDTO;
import com.ieslasencinas.gestionacademica.entity.FranjaHoraria;
import com.ieslasencinas.gestionacademica.exception.ResourceNotFoundException;
import com.ieslasencinas.gestionacademica.mapper.FranjaHorariaMapper;
import com.ieslasencinas.gestionacademica.repository.FranjaHorariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FranjaHorariaServiceImpl implements FranjaHorariaService {

    private final FranjaHorariaRepository franjaHorariaRepository;
    private final FranjaHorariaMapper franjaHorariaMapper;

    @Override
    @Transactional
    public FranjaHorariaDTO crear(FranjaHorariaDTO dto) {
        FranjaHoraria franja = franjaHorariaMapper.toEntity(dto);
        return franjaHorariaMapper.toDTO(franjaHorariaRepository.save(franja));
    }

    @Override
    @Transactional(readOnly = true)
    public FranjaHorariaDTO buscarPorId(Long idFranja) {
        return franjaHorariaRepository.findById(idFranja)
                .map(franjaHorariaMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Franja horaria no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FranjaHorariaDTO> buscarTodos() {
        return franjaHorariaRepository.findAll().stream()
                .map(franjaHorariaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FranjaHorariaDTO actualizar(Long idFranja, FranjaHorariaDTO dto) {
        FranjaHoraria franja = franjaHorariaRepository.findById(idFranja)
                .orElseThrow(() -> new ResourceNotFoundException("Franja horaria no encontrada"));

        franja.setDiaSemana(dto.diaSemana());
        franja.setHoraInicio(dto.horaInicio());
        franja.setHoraFin(dto.horaFin());

        return franjaHorariaMapper.toDTO(franjaHorariaRepository.save(franja));
    }

    @Override
    @Transactional
    public void eliminar(Long idFranja) {
        if (!franjaHorariaRepository.existsById(idFranja)) {
            throw new ResourceNotFoundException("Franja horaria no encontrada");
        }
        franjaHorariaRepository.deleteById(idFranja);
    }
}

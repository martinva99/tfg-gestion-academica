package com.ieslasencinas.gestionacademica.config;

import com.ieslasencinas.gestionacademica.entity.*;
import com.ieslasencinas.gestionacademica.entity.enums.*;
import com.ieslasencinas.gestionacademica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CentroRepository centroRepository;
    private final DocenteRepository docenteRepository;
    private final AlumnoRepository alumnoRepository;
    private final TutorLegalRepository tutorLegalRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final GrupoRepository grupoRepository;
    private final AnioAcademicoRepository anioAcademicoRepository;
    private final MatriculaRepository matriculaRepository;
    private final FranjaHorariaRepository franjaHorariaRepository;
    private final SesionHorariaRepository sesionHorariaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final CalificacionRepository calificacionRepository;
    private final FaltaAsistenciaRepository faltaAsistenciaRepository;
    private final ParteDisciplinarioRepository parteDisciplinarioRepository;
    private final NotificacionRepository notificacionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (asignaturaRepository.count() > 0) {
            System.out.println("✅ La base de datos ya contiene datos académicos. Seeder omitido.");
            return;
        }

        System.out.println("🌱 Iniciando el sembrado masivo de la Base de Datos (Seeder)...");
        String passHash = passwordEncoder.encode("1234");

        
        
        
        Rol rDocente = obtenerOCrearRol("ROLE_DOCENTE");
        Rol rAlumno = obtenerOCrearRol("ROLE_ALUMNO");
        Rol rTutor = obtenerOCrearRol("ROLE_TUTOR_LEGAL");
        Rol rJefatura = obtenerOCrearRol("ROLE_JEFATURA");
        Rol rSecretaria = obtenerOCrearRol("ROLE_SECRETARIA");
        Rol rAdmin = obtenerOCrearRol("ROLE_ADMINISTRADOR");

        
        
        
        Centro centro = new Centro();
        centro.setNombre("IES Las Encinas");
        centro.setDireccion("Calle Falsa 123");
        centro.setTelefono("900123456");
        centro = centroRepository.save(centro);

        
        
        
        crearUsuario("Gabriel", "Veiga", "admin@test.com", passHash, Set.of(rAdmin));
        crearUsuario("Marta", "Pérez", "jefatura@test.com", passHash, Set.of(rJefatura));
        crearUsuario("Paul", "Torres", "secretaria@test.com", passHash, Set.of(rSecretaria));

        
        
        
        Docente docLaura = new Docente();
        docLaura.setUsuario(crearUsuario("Laura", "Gómez", "laura@test.com", passHash, Set.of(rDocente)));
        docLaura.setEspecialidad("Informática");
        docLaura = docenteRepository.save(docLaura);

        Docente docPedro = new Docente();
        docPedro.setUsuario(crearUsuario("Pedro", "Martínez", "pedro@test.com", passHash, Set.of(rDocente)));
        docPedro.setEspecialidad("Sistemas");
        docPedro = docenteRepository.save(docPedro);

        Docente docAntonio = new Docente();
        docAntonio.setUsuario(crearUsuario("Antonio", "Gómez", "antonio@test.com", passHash, Set.of(rDocente)));
        docAntonio.setEspecialidad("Sistemas");
        docAntonio = docenteRepository.save(docAntonio);

        Docente docRamira = new Docente();
        docRamira.setUsuario(crearUsuario("Ramira", "Ramírez", "ramira@test.com", passHash, Set.of(rDocente)));
        docRamira.setEspecialidad("Sistemas");
        docRamira = docenteRepository.save(docRamira);

        Docente docCarmen = new Docente();
        docCarmen.setUsuario(crearUsuario("Carmen", "Iglesias", "carmen@test.com", passHash, Set.of(rDocente)));
        docCarmen.setEspecialidad("Sistemas");
        docCarmen = docenteRepository.save(docCarmen);

        Docente docTania = new Docente();
        docTania.setUsuario(crearUsuario("Tania", "Núñez", "tania@test.com", passHash, Set.of(rDocente)));
        docTania.setEspecialidad("Sistemas");
        docTania = docenteRepository.save(docTania);

        Docente docGonzalo = new Docente();
        docGonzalo.setUsuario(crearUsuario("Gonzalo", "Núñez", "gonzalo@test.com", passHash, Set.of(rDocente)));
        docGonzalo.setEspecialidad("Sistemas");
        docGonzalo = docenteRepository.save(docGonzalo);

        Docente docLourdes = new Docente();
        docLourdes.setUsuario(crearUsuario("Lourdes", "Blanco", "lourdes@test.com", passHash, Set.of(rDocente)));
        docLourdes.setEspecialidad("Sistemas");
        docLourdes = docenteRepository.save(docLourdes);

        
        
        
        TutorLegal tutorAna = new TutorLegal();
        tutorAna.setUsuario(crearUsuario("Ana", "López (Madre)", "ana@test.com", passHash, Set.of(rTutor)));
        tutorAna = tutorLegalRepository.save(tutorAna);

        Alumno aluCarlos = crearAlumno("Carlos", "López", "carlos@test.com", passHash, rAlumno, LocalDate.of(2005, 5, 15));
        Alumno aluLucia = crearAlumno("Lucía", "López", "lucia@test.com", passHash, rAlumno, LocalDate.of(2006, 8, 20));
        Alumno aluMiguel = crearAlumno("Miguel", "Sánchez", "miguel@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluCarla = crearAlumno("Carla", "Tebas", "carla@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluMonica = crearAlumno("Mónica", "Martín", "monica@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluJoel = crearAlumno("Joel", "Esteban", "joel@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluAlfonso = crearAlumno("Alfonso", "Corella", "alfonso@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluEnrique = crearAlumno("Enrique", "Jiménez", "enrique@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluMaite = crearAlumno("Maite", "Palazón", "maite@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluFrancisco = crearAlumno("Francisco", "Prada", "paco@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluMaria = crearAlumno("María", "Ramos", "maria@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluAlicia = crearAlumno("Alicia", "González", "alicia@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluGuillermo = crearAlumno("Guillermo", "Lamas", "guille@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));
        Alumno aluSandra = crearAlumno("Sandra", "Sánchez", "sandra@test.com", passHash, rAlumno, LocalDate.of(2005, 2, 10));


        aluCarlos.getTutoresLegales().add(tutorAna);
        aluLucia.getTutoresLegales().add(tutorAna);
        alumnoRepository.save(aluCarlos);
        alumnoRepository.save(aluLucia);
        alumnoRepository.save(aluMiguel);
        alumnoRepository.save(aluCarla);
        alumnoRepository.save(aluMonica);
        alumnoRepository.save(aluJoel);
        alumnoRepository.save(aluAlfonso);
        alumnoRepository.save(aluEnrique);
        alumnoRepository.save(aluMaite);
        alumnoRepository.save(aluFrancisco);
        alumnoRepository.save(aluMaria);
        alumnoRepository.save(aluAlicia);
        alumnoRepository.save(aluGuillermo);
        alumnoRepository.save(aluSandra);

        
        
        
        AnioAcademico anio = new AnioAcademico();
        anio.setCursoEscolar("2025/2026");
        anio.setFechaInicio(LocalDate.of(2025, 9, 1));
        anio.setFechaFin(LocalDate.of(2026, 6, 30));
        anio = anioAcademicoRepository.save(anio);

        Asignatura asigProg = crearAsignatura("Programación", 8.0);
        Asignatura asigBD = crearAsignatura("Bases de Datos", 6.0);
        Asignatura asigEntornos = crearAsignatura("Entornos de Desarrollo", 3.0);
        Asignatura asigAccesoDatos = crearAsignatura("Acceso a Datos", 5.0);
        Asignatura asigInterfaces = crearAsignatura("Desarrollo de Interfaces", 5.0);
        Asignatura asigFol = crearAsignatura("Itinerario Personal Empleabilidad", 2.0);
        Asignatura asigSostenibilidad = crearAsignatura("Sostenibilidad", 1.0);
        Asignatura asigPsp = crearAsignatura("Programación de Servicios y Procesos", 4.0);
        Asignatura asigMoviles = crearAsignatura("Programación multimedia y móviles", 4.0);
        Asignatura asigCiber = crearAsignatura("Ciberseguridad", 2.0);
        Asignatura asigIngles = crearAsignatura("Inglés", 2.0);
        Asignatura asigDigi = crearAsignatura("Digitalización", 1.0);
        Asignatura asigSge = crearAsignatura("Sistemas de gestión empresarial", 4.0);


        Grupo grupo1DAM = new Grupo();
        grupo1DAM.setNombre("1º DAM");
        grupo1DAM.setAnioAcademico(anio);
        grupo1DAM.setCentro(centro);
        grupo1DAM.getAsignaturas().addAll(Set.of(asigProg, asigBD, asigEntornos));
        grupo1DAM = grupoRepository.save(grupo1DAM);

        Grupo grupo2DAM = new Grupo();
        grupo2DAM.setNombre("2º DAM");
        grupo2DAM.setAnioAcademico(anio);
        grupo2DAM.setCentro(centro);
        grupo2DAM.getAsignaturas().addAll(Set.of(asigAccesoDatos, asigInterfaces, asigSge, asigFol, asigMoviles, asigPsp, asigSostenibilidad, asigCiber, asigIngles, asigDigi));
        grupo2DAM = grupoRepository.save(grupo2DAM);

        
        
        
        Evaluacion eval1 = new Evaluacion();
        eval1.setTrimestre(1);
        eval1.setEstado(EstadoEvaluacion.ABIERTA);
        eval1.setGrupo(grupo1DAM);
        eval1 = evaluacionRepository.save(eval1);

        Evaluacion eval2 = new Evaluacion();
        eval2.setTrimestre(2);
        eval2.setEstado(EstadoEvaluacion.CERRADA);
        eval2.setGrupo(grupo1DAM);
        eval2 = evaluacionRepository.save(eval2);

        Evaluacion eval3 = new Evaluacion();
        eval3.setTrimestre(3);
        eval3.setEstado(EstadoEvaluacion.CERRADA);
        eval3.setGrupo(grupo1DAM);
        eval3 = evaluacionRepository.save(eval3);

        Evaluacion eval4 = new Evaluacion();
        eval4.setTrimestre(1);
        eval4.setEstado(EstadoEvaluacion.ABIERTA);
        eval4.setGrupo(grupo2DAM);
        eval4 = evaluacionRepository.save(eval4);

        Evaluacion eval5 = new Evaluacion();
        eval5.setTrimestre(2);
        eval5.setEstado(EstadoEvaluacion.CERRADA);
        eval5.setGrupo(grupo2DAM);
        eval5 = evaluacionRepository.save(eval5);

        Evaluacion eval6 = new Evaluacion();
        eval6.setTrimestre(3);
        eval6.setEstado(EstadoEvaluacion.CERRADA);
        eval6.setGrupo(grupo2DAM);
        eval6 = evaluacionRepository.save(eval6);

        
        
        
        Matricula matCarlosProg = matricular(aluCarlos, grupo1DAM, asigProg, anio);
        Matricula matCarlosBD = matricular(aluCarlos, grupo1DAM, asigBD, anio);
        Matricula matCarlosEntornos = matricular(aluCarlos, grupo1DAM, asigEntornos, anio);
        Matricula matLuciaProg = matricular(aluLucia, grupo1DAM, asigProg, anio);
        Matricula matMiguelProg = matricular(aluMiguel, grupo1DAM, asigProg, anio);
        Matricula matMiguelEntornos = matricular(aluMiguel, grupo1DAM, asigEntornos, anio);

        
        
        
        DiaSemana[] dias = {DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES, DiaSemana.JUEVES, DiaSemana.VIERNES};
        int[][] horas = {
                {8, 30, 9, 25},   
                {9, 25, 10, 15},  
                {10, 15, 11, 10}, 
                {11, 35, 12, 30}, 
                {12, 30, 13, 25}, 
                {13, 25, 14, 20}, 
                {14, 30, 15, 25}  
        };

        List<FranjaHoraria> todasLasFranjas = new ArrayList<>();
        for (DiaSemana dia : dias) {
            for (int[] h : horas) {
                todasLasFranjas.add(crearFranja(dia, h[0], h[1], h[2], h[3]));
            }
        }

        SesionHoraria sProg = crearSesion(docLaura, grupo1DAM, asigProg, todasLasFranjas.get(0), "Aula 101");
        SesionHoraria sBD = crearSesion(docLaura, grupo1DAM, asigBD, todasLasFranjas.get(1), "Aula 101");
        SesionHoraria sEntornos = crearSesion(docPedro, grupo1DAM, asigEntornos, todasLasFranjas.get(9), "Aula 102");

        
        
        
        crearFalta(matCarlosProg, sProg, LocalDate.now(), TipoFalta.FALTA, false);
        crearFalta(matMiguelEntornos, sBD, LocalDate.now(), TipoFalta.RETRASO, true);

        
        
        
        crearParte(aluCarlos, docLaura, LocalDate.now(), TipoParte.LEVE, "Hablar en clase repetidamente", EstadoParte.PENDIENTE);
        crearParte(aluMiguel, docTania, LocalDate.now().minusDays(2), TipoParte.GRAVE, "Falta de respeto a un compañero", EstadoParte.FIRMADO);

        
        
        
        crearNotificacion(aluCarlos.getUsuario(), "Tienes una nueva falta de asistencia en Programación.", false);
        crearNotificacion(tutorAna.getUsuario(), "Su hijo Carlos ha recibido un parte disciplinario.", false);
        crearNotificacion(docLaura.getUsuario(), "Recordatorio: Claustro de profesores mañana a las 16:00.", true);

        System.out.println("✅ ¡Base de datos poblada con éxito!");
    }

    
    
    

    private Rol obtenerOCrearRol(String nombreRol) {
        return rolRepository.findByNombre(nombreRol).orElseGet(() -> {
            Rol r = new Rol();
            r.setNombre(nombreRol);
            return rolRepository.save(r);
        });
    }

    private Usuario crearUsuario(String nombre, String apellidos, String email, String passHash, Set<Rol> roles) {
        Usuario u = new Usuario();
        u.setNombre(nombre); u.setApellidos(apellidos); u.setEmail(email);
        u.setContraseniaHash(passHash); u.setActivo(true);
        u.setRoles(roles);
        return usuarioRepository.save(u);
    }

    private Alumno crearAlumno(String nombre, String apellidos, String email, String passHash, Rol rol, LocalDate fechaNac) {
        Usuario u = crearUsuario(nombre, apellidos, email, passHash, Set.of(rol));
        Alumno a = new Alumno();
        a.setUsuario(u); a.setFechaNacimiento(fechaNac);
        return alumnoRepository.save(a);
    }

    private Asignatura crearAsignatura(String nombre, Double horas) {
        Asignatura a = new Asignatura();
        a.setNombre(nombre); a.setHorasSemanales(horas);
        return asignaturaRepository.save(a);
    }

    private Matricula matricular(Alumno a, Grupo g, Asignatura asig, AnioAcademico anio) {
        Matricula m = new Matricula();
        m.setAlumno(a); m.setGrupo(g); m.setAsignatura(asig); m.setAnioAcademico(anio);
        return matriculaRepository.save(m);
    }

    private FranjaHoraria crearFranja(DiaSemana dia, int hIni, int mIni, int hFin, int mFin) {
        FranjaHoraria f = new FranjaHoraria();
        f.setDiaSemana(dia); f.setHoraInicio(LocalTime.of(hIni, mIni)); f.setHoraFin(LocalTime.of(hFin, mFin));
        return franjaHorariaRepository.save(f);
    }

    private SesionHoraria crearSesion(Docente d, Grupo g, Asignatura a, FranjaHoraria f, String aula) {
        SesionHoraria s = new SesionHoraria();
        s.setDocente(d); s.setGrupo(g); s.setAsignatura(a); s.setFranjaHoraria(f); s.setAula(aula);
        return sesionHorariaRepository.save(s);
    }

    private void crearCalificacion(Matricula m, Evaluacion e, Docente d, int nota, String obs) {
        Calificacion c = new Calificacion();
        c.setMatricula(m); c.setEvaluacion(e); c.setDocente(d); c.setNota(nota); c.setObservaciones(obs);
        calificacionRepository.save(c);
    }

    private void crearFalta(Matricula m, SesionHoraria s, LocalDate fecha, TipoFalta tipo, boolean justificada) {
        FaltaAsistencia f = new FaltaAsistencia();
        f.setMatricula(m); f.setSesionHoraria(s); f.setFecha(fecha); f.setTipo(tipo); f.setJustificada(justificada);
        faltaAsistenciaRepository.save(f);
    }

    private void crearParte(Alumno a, Docente d, LocalDate fecha, TipoParte tipo, String desc, EstadoParte estado) {
        ParteDisciplinario p = new ParteDisciplinario();
        p.setAlumno(a); p.setDocente(d); p.setFecha(fecha); p.setTipo(tipo); p.setDescripcion(desc); p.setEstado(estado);
        parteDisciplinarioRepository.save(p);
    }

    private void crearNotificacion(Usuario u, String mensaje, boolean leida) {
        Notificacion n = new Notificacion();
        n.setUsuario(u); n.setMensaje(mensaje); n.setLeida(leida);
        notificacionRepository.save(n);
    }
}
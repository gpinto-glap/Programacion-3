package com.ejemplo;

import com.ejemplo.modelo.Consulta;
import com.ejemplo.modelo.HistoriaClinica;
import com.ejemplo.modelo.Medicamento;
import com.ejemplo.modelo.Medico;
import com.ejemplo.modelo.Paciente;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

public class MainClinica {

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ClinicaPU");
        EntityManager em = emf.createEntityManager();

        try {
            // =========================================================
            // 1. PERSISTENCIA DE DATOS
            // =========================================================
            em.getTransaction().begin();

            // 3 Medicamentos
            Medicamento med1 = new Medicamento("Actron 600", "Ibuprofeno", 600);
            Medicamento med2 = new Medicamento("Tafirol 1g", "Paracetamol", 1000);
            Medicamento med3 = new Medicamento("Amoxidal 500", "Amoxicilina", 500);

            em.persist(med1);
            em.persist(med2);
            em.persist(med3);

            // 2 Historias Clínicas
            HistoriaClinica hc1 = new HistoriaClinica("Paciente con antecedentes de alergia estacional. Sin cirugías previas.");
            HistoriaClinica hc2 = new HistoriaClinica("Hipertensión leve controlada con dieta. Chequeos anuales normales.");

            // 3 Pacientes (dos mayores de 30 años y uno menor, con obras sociales variadas)
            Paciente p1 = new Paciente("Juan", "Pérez", 35, 34567890, "OSDE", LocalDate.of(1991, 5, 14), 'M');
            p1.setHistoriaClinica(hc1);
            p1.getMedicamentos().add(med1);
            p1.getMedicamentos().add(med2);

            Paciente p2 = new Paciente("María", "Gómez", 42, 28901234, "Swiss Medical", LocalDate.of(1984, 8, 22), 'F');
            p2.setHistoriaClinica(hc2);
            p2.getMedicamentos().add(med2);
            p2.getMedicamentos().add(med3);

            Paciente p3 = new Paciente("Lucas", "Fernández", 24, 43123456, "OSDE", LocalDate.of(2002, 2, 10), 'M');
            // p3 queda sin historia clínica para probar LEFT JOIN en la última consulta
            p3.getMedicamentos().add(med1);

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            // 2 Médicos
            Medico m1 = new Medico("Carlos", "Rodríguez", 50, "Clínica Médica", "MP-10234");
            Medico m2 = new Medico("Ana", "Martínez", 39, "Cardiología", "MP-20456");

            em.persist(m1);
            em.persist(m2);

            // 4 Consultas con fechas distintas y diagnósticos variados
            Consulta c1 = new Consulta(LocalDate.of(2026, 10, 5), "Faringitis aguda", p1, m1);
            Consulta c2 = new Consulta(LocalDate.of(2026, 10, 18), "Control cardiológico anual", p2, m2);
            Consulta c3 = new Consulta(LocalDate.of(2026, 11, 2), "Lumbalgia post-esfuerzo", p3, m1);
            Consulta c4 = new Consulta(LocalDate.of(2026, 11, 15), "Cuadro gripal estacional", p1, m1);

            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.persist(c4);

            em.getTransaction().commit();
            System.out.println("\n>>> DATOS PERSISTIDOS CORRECTAMENTE <<<\n");

            // =========================================================
            // 2. CONSULTAS JPQL SOLICITADAS
            // =========================================================

            // 1. Listar todos los pacientes mayores de 30 años.
            System.out.println("--- 1. Pacientes mayores de 30 años ---");
            List<Paciente> mayores30 = em.createQuery(
                    "SELECT p FROM Paciente p WHERE p.edad > 30", Paciente.class)
                    .getResultList();
            for (Paciente p : mayores30) {
                System.out.println("- " + p.getNombre() + " " + p.getApellido() + " (" + p.getEdad() + " años)");
            }

            // 2. Obtener todas las consultas realizadas por un médico específico.
            System.out.println("\n--- 2. Consultas realizadas por el Dr. Carlos Rodríguez ---");
            List<Consulta> consultasMedico = em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.medico.matricula = :matricula", Consulta.class)
                    .setParameter("matricula", "MP-10234")
                    .getResultList();
            for (Consulta c : consultasMedico) {
                System.out.println("- Fecha: " + c.getFecha() + " | Diagnóstico: " + c.getDiagnostico()
                        + " | Paciente: " + c.getPaciente().getNombre() + " " + c.getPaciente().getApellido());
            }

            // 3. Mostrar todos los medicamentos asociados a un paciente.
            System.out.println("\n--- 3. Medicamentos asociados a Juan Pérez ---");
            List<Medicamento> medsPaciente = em.createQuery(
                    "SELECT m FROM Paciente p JOIN p.medicamentos m WHERE p.DNI = :dni", Medicamento.class)
                    .setParameter("dni", 34567890)
                    .getResultList();
            for (Medicamento m : medsPaciente) {
                System.out.println("- " + m.getNombre() + " (" + m.getDroga() + " - " + m.getPesoEnGramos() + "mg)");
            }

            // 4. Listar las consultas con su diagnóstico y el nombre del paciente.
            System.out.println("\n--- 4. Consultas con diagnóstico y nombre del paciente ---");
            List<Object[]> consultasDiagnostico = em.createQuery(
                    "SELECT c.fecha, c.diagnostico, p.nombre, p.apellido FROM Consulta c JOIN c.paciente p", Object[].class)
                    .getResultList();
            for (Object[] fila : consultasDiagnostico) {
                System.out.println("- [" + fila[0] + "] Diagnóstico: " + fila[1] + " -> Paciente: " + fila[2] + " " + fila[3]);
            }

            // 5. Calcular el promedio de edad de los pacientes.
            System.out.println("\n--- 5. Promedio de edad de los pacientes ---");
            Double promedioEdad = em.createQuery(
                    "SELECT AVG(p.edad) FROM Paciente p", Double.class)
                    .getSingleResult();
            System.out.printf("- Promedio de edad: %.2f años%n", promedioEdad);

            // 6. Listar todos los pacientes que tienen una obra social específica.
            System.out.println("\n--- 6. Pacientes con obra social OSDE ---");
            List<Paciente> pacientesOSDE = em.createQuery(
                    "SELECT p FROM Paciente p WHERE p.obraSocial = :os", Paciente.class)
                    .setParameter("os", "OSDE")
                    .getResultList();
            for (Paciente p : pacientesOSDE) {
                System.out.println("- " + p.getNombre() + " " + p.getApellido() + " (DNI: " + p.getDNI() + ")");
            }

            // 7. Mostrar los médicos y la cantidad de consultas que atendieron.
            System.out.println("\n--- 7. Médicos y cantidad de consultas atendidas ---");
            List<Object[]> medicosCantidad = em.createQuery(
                    "SELECT m.nombre, m.apellido, COUNT(c) FROM Medico m LEFT JOIN m.consultas c GROUP BY m.id, m.nombre, m.apellido", Object[].class)
                    .getResultList();
            for (Object[] fila : medicosCantidad) {
                System.out.println("- Dr/a. " + fila[0] + " " + fila[1] + ": " + fila[2] + " consulta(s)");
            }

            // 8. Obtener todos los pacientes junto con la descripción de su historia clínica.
            System.out.println("\n--- 8. Pacientes y descripción de su historia clínica ---");
            List<Object[]> pacientesHistoria = em.createQuery(
                    "SELECT p.nombre, p.apellido, hc.descripcion FROM Paciente p LEFT JOIN p.historiaClinica hc", Object[].class)
                    .getResultList();
            for (Object[] fila : pacientesHistoria) {
                String desc = (fila[2] != null) ? (String) fila[2] : "Sin historia clínica registrada";
                System.out.println("- " + fila[0] + " " + fila[1] + " -> HC: " + desc);
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
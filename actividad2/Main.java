import java.util.ArrayList;
import java.util.List;

/*
 * CONSIGNA
 *
 * Crear un programa que:
 * 1. Defina una clase base Persona con atributos nombre y edad.
 * 2. Defina una clase Estudiante que herede de Persona y agregue el atributo carrera.
 * 3. Sobrescriba toString() en ambas clases para mostrar la información de manera legible.
 * 4. Almacene varios objetos Estudiante en una lista y muestre su contenido usando
 *    System.out.println(lista).
 */

// RESOLUCIÓN
public class Main {
    public static void main(String[] args) {
        List<Estudiante> estudiantes = new ArrayList<>();

        estudiantes.add(new Estudiante("Pedro", 22, "Ingeniería Química"));
        estudiantes.add(new Estudiante("Mario", 30, "Diseño Gráfico"));
        estudiantes.add(new Estudiante("Carla", 19, "Medicina"));

        System.out.println(estudiantes);
    }
}

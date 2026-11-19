package com.ejemplo.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Medico extends Base {

    private String nombre;
    private String apellido;
    private int edad;
    private String especialidad;
    private String matricula;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();

    public Medico() {
    }

    public Medico(String nombre, String apellido, int edad, String especialidad, String matricula) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.especialidad = especialidad;
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
package com.ejemplo.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String droga;
    private int pesoEnGramos;

    @ManyToMany(mappedBy = "medicamentos")
    private List<Paciente> pacientes = new ArrayList<>();

    public Medicamento() {
    }

    public Medicamento(String nombre, String droga, int pesoEnGramos) {
        this.nombre = nombre;
        this.droga = droga;
        this.pesoEnGramos = pesoEnGramos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDroga() {
        return droga;
    }

    public void setDroga(String droga) {
        this.droga = droga;
    }

    public int getPesoEnGramos() {
        return pesoEnGramos;
    }

    public void setPesoEnGramos(int pesoEnGramos) {
        this.pesoEnGramos = pesoEnGramos;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }
}
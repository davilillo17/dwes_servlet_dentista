package com.dentista.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private int idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private int idPaciente;
    private int idOdontologo;
    private String motivo;

    // Campos auxiliares para mostrar los nombres en las vistas
    private String nombrePaciente;
    private String nombreOdontologo;

    public Cita() {}

    public Cita(int idCita, LocalDate fecha, LocalTime hora, int idPaciente, int idOdontologo, String motivo) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.idPaciente = idPaciente;
        this.idOdontologo = idOdontologo;
        this.motivo = motivo;
    }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdOdontologo() { return idOdontologo; }
    public void setIdOdontologo(int idOdontologo) { this.idOdontologo = idOdontologo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getNombreOdontologo() { return nombreOdontologo; }
    public void setNombreOdontologo(String nombreOdontologo) { this.nombreOdontologo = nombreOdontologo; }
}
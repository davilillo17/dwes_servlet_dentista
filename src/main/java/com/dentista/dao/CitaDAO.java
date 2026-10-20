package com.dentista.dao;

import com.dentista.config.Conexion;
import com.dentista.model.Cita;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    public List<Cita> listarCitas(boolean todas) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.*, CONCAT(p.nombre, ' ', p.apellidos) AS paciente_nombre, o.nombre AS odontologo_nombre " +
                     "FROM cita c " +
                     "JOIN paciente p ON c.id_paciente = p.id_paciente " +
                     "JOIN odontologos o ON c.id_odontologo = o.id_odontologo ";
        
        if (!todas) {
            sql += "WHERE c.fecha >= CURDATE() ";
        }
        sql += "ORDER BY c.fecha ASC, c.hora ASC";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cita c = new Cita(
                    rs.getInt("id_cita"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getTime("hora").toLocalTime(),
                    rs.getInt("id_paciente"),
                    rs.getInt("id_odontologo"),
                    rs.getString("motivo")
                );
                c.setNombrePaciente(rs.getString("paciente_nombre"));
                c.setNombreOdontologo(rs.getString("odontologo_nombre"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insertar(Cita c) {
        String sql = "INSERT INTO cita (fecha, hora, id_paciente, id_odontologo, motivo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(c.getFecha()));
            stmt.setTime(2, Time.valueOf(c.getHora()));
            stmt.setInt(3, c.getIdPaciente());
            stmt.setInt(4, c.getIdOdontologo());
            stmt.setString(5, c.getMotivo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean borrar(int idCita) {
        String sql = "DELETE FROM cita WHERE id_cita = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCita);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
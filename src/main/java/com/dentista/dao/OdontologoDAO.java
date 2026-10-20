package com.dentista.dao;

import com.dentista.config.Conexion;
import com.dentista.model.Odontologo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDAO {

    public List<Odontologo> listarTodos() {
        List<Odontologo> lista = new ArrayList<>();
        String sql = "SELECT * FROM odontologos ORDER BY nombre";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Odontologo o = new Odontologo(
                    rs.getInt("id_odontologo"),
                    rs.getString("nombre"),
                    rs.getString("especialidad")
                );
                lista.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
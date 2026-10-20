package com.dentista.controller;

import com.dentista.dao.PacienteDAO;
import com.dentista.model.Paciente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "PacienteServlet", urlPatterns = {"/pacientes", "/paciente-guardar", "/paciente-borrar", "/paciente-form"})
public class PacienteServlet extends HttpServlet {
    private PacienteDAO pacienteDAO;

    @Override
    public void init() {
        pacienteDAO = new PacienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        
        if ("/pacientes".equals(action)) {
            List<Paciente> lista = pacienteDAO.listarTodos();
            request.setAttribute("pacientes", lista);
            request.getRequestDispatcher("/WEB-INF/listadoPacientes.jsp").forward(request, response);
        } else if ("/paciente-form".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/formularioPaciente.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        if ("/paciente-guardar".equals(action)) {
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String dni = request.getParameter("dni");
            String fechaNacStr = request.getParameter("fecha_nacimiento");
            LocalDate fechaNac = (fechaNacStr != null && !fechaNacStr.isEmpty()) ? LocalDate.parse(fechaNacStr) : null;
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");
            String direccion = request.getParameter("direccion");
            String alergias = request.getParameter("alergias");

            Paciente p = new Paciente(0, nombre, apellidos, dni, fechaNac, telefono, email, direccion, alergias);
            pacienteDAO.insertar(p);
            
            response.sendRedirect(request.getContextPath() + "/pacientes");
            
        } else if ("/paciente-borrar".equals(action)) {
            int idPaciente = Integer.parseInt(request.getParameter("id_paciente"));
            pacienteDAO.borrar(idPaciente);
            
            response.sendRedirect(request.getContextPath() + "/pacientes");
        }
    }
}
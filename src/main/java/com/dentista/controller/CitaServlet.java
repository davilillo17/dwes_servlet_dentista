package com.dentista.controller;

import com.dentista.dao.CitaDAO;
import com.dentista.dao.OdontologoDAO;
import com.dentista.dao.PacienteDAO;
import com.dentista.model.Cita;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet(name = "CitaServlet", urlPatterns = {"/citas", "/cita-form", "/cita-guardar", "/cita-borrar"})
public class CitaServlet extends HttpServlet {
    private CitaDAO citaDAO;
    private OdontologoDAO odontologoDAO;
    private PacienteDAO pacienteDAO;

    @Override
    public void init() {
        citaDAO = new CitaDAO();
        odontologoDAO = new OdontologoDAO();
        pacienteDAO = new PacienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        if ("/citas".equals(action)) {
            boolean todas = "1".equals(request.getParameter("todas"));
            List<Cita> lista = citaDAO.listarCitas(todas);
            request.setAttribute("citas", lista);
            request.setAttribute("todas", todas);
            request.getRequestDispatcher("/WEB-INF/listadoCitas.jsp").forward(request, response);
            
        } else if ("/cita-form".equals(action)) {
            request.setAttribute("odontologos", odontologoDAO.listarTodos());
            request.setAttribute("pacientes", pacienteDAO.listarTodos());
            request.getRequestDispatcher("/WEB-INF/formularioCita.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        if ("/cita-guardar".equals(action)) {
            LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
            LocalTime hora = LocalTime.parse(request.getParameter("hora"));
            int idPaciente = Integer.parseInt(request.getParameter("id_paciente"));
            int idOdontologo = Integer.parseInt(request.getParameter("id_odontologo"));
            String motivo = request.getParameter("motivo");

            Cita c = new Cita(0, fecha, hora, idPaciente, idOdontologo, motivo);
            citaDAO.insertar(c);

            response.sendRedirect(request.getContextPath() + "/citas");
            
        } else if ("/cita-borrar".equals(action)) {
            int idCita = Integer.parseInt(request.getParameter("id_cita"));
            citaDAO.borrar(idCita);

            response.sendRedirect(request.getContextPath() + "/citas");
        }
    }
}
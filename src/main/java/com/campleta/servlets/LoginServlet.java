/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.campleta.servlets;

import com.campleta.config.DatabaseCfg;
import com.campleta.models.Role;
import com.campleta.models.User;
import com.campleta.repo.impl.UserRepo;
import com.campleta.services.AuthenticationService;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.campleta.repo.interfaces.IUserRepo;
import javax.persistence.Persistence;

/**
 *
 * @author Vixo
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private AuthenticationService authService;
    private IUserRepo userRepo;
    
    public LoginServlet() {
        super();
        this.userRepo = new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME));
    }
    
    @Inject
    public LoginServlet(IUserRepo userRepo) {
        super();
        this.userRepo = userRepo;
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        authService = new AuthenticationService(userRepo);
        
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        
        try {
            User user = authService.login(email, pass);
        
            if(user != null) {                
                HttpSession session = request.getSession();
                session.setAttribute("User", "Campleta");
                session.setAttribute("Email", email);
                session.setAttribute("Firstname", user.getFirstname());
                List<Role> rolesList = user.getRoles();
                if(rolesList != null) {
                    for(Role role : rolesList) {
                        session.setAttribute("Has"+role.getName()+"Role", true);
                    }
                }
                if(user.getCampsites() != null) {
                    session.setAttribute("campsite", user.getCampsites().get(0).getId().toString());
                }
                session.setMaxInactiveInterval(2*60);
                Cookie cookie = new Cookie("campleta", user.getId().toString());
                cookie.setMaxAge(2*60);

                String redirectUrl = response.encodeRedirectURL("index.jsp");
                response.addCookie(cookie);
                response.sendRedirect(redirectUrl);
            } else {
                JsonObject json = new JsonObject();
                json.addProperty("status", "500");
                json.addProperty("message", "Test failure");
                request.setAttribute("loginFailed", json);
                doGet(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("loginFailed", true);
            request.setAttribute("status", "500");
            request.setAttribute("message", e.getMessage());
            doGet(request, response);
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

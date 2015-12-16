/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author subha
 */
@WebServlet(name = "ActNiceServlet", urlPatterns = {"/ActNiceServlet"})
    public class ActNiceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
        private static final String ACT_NICE_BEAN_SESSION_KEY = "ActNice";

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

	    // Obtain the EJB from the HTTP session
            ActNiceBeanLocal ActNiceBean = (ActNiceBeanLocal) request.getSession().getAttribute(ACT_NICE_BEAN_SESSION_KEY);

            if (ActNiceBean == null) {
                // EJB ActNiceBean not present in the HTTP session
                // so let's fetch a new one from the container
                try {
                    InitialContext ic = new InitialContext();
                    ActNiceBean = (ActNiceBeanLocal) ic.lookup("java:comp/env/ejb/ActNiceBean");

                    // put EJB in HTTP session for future servlet calls
                    request.getSession().setAttribute(ACT_NICE_BEAN_SESSION_KEY, ActNiceBean);
                } catch (NamingException e) {
                    processRequest( request,  response);
                    // throw new ServletException(e);
                }
            }

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>ActNice</title>");
                out.println("</head>");
                out.println("<body bgcolor=\"#F5F6CE\">");
                out.println("<h1 align=\"center\">Act Nice Together!</h1>");
                out.println("<div align=\"center\">");

                
                //getting the actors from the request and processing the data
                boolean error = false;
                boolean actor1Present = false;
                String actor1 = request.getParameter("actor1");
                if (actor1 != null && actor1.trim().length() > 0) {                   
                    actor1Present = true;
                }
            
                //adding actor2 to the list of actors
                String actor2 = request.getParameter("actor2");
                boolean actor2Present = false;
                if (actor2 != null && actor2.trim().length() > 0) {
                    actor2Present = true; 
                }
                
                if(actor1Present && actor2Present) {
                    actor1.toLowerCase();
                    String[] split = actor1.split(" ");
                    String name = split[0];
                    for(int i = 1 ; i < split.length ; i++) {
                        name =name + "+" + split[i];
                    }
                    Actor actor = new Actor(name);
                    try{
                        ActNiceBean.findDetails(actor);
                    }catch(Exception e) {
                        try{
                          ActNiceBean.findDetails(actor);
                        }catch(Exception x) {
                            out.println("There was an error. Try again!<br>");
                            error = true;
                        }
                    }
                    
                    actor2.toLowerCase();
                    split = actor2.split(" ");
                    name = split[0];
                    for(int i = 1 ; i < split.length ; i++) {
                        name = name+ "+" + split[i];
                    }
                    actor = new Actor(name);
                    try{
                        ActNiceBean.findDetails(actor);
                    }catch(Exception e) {
                        try{
                          ActNiceBean.findDetails(actor);
                        }catch(Exception x) {
                            out.println("There was an error. Try again!<br>");
                            error = true;
                        }
                    }
                }else{  
                    out.println("Please enter names for both actors and try again!");
                    error = true;
                }
            
                if(!error){
            
                    //finding the common movies and publishing the data
                    ActNiceBean.findCommonMovie(out);
                    ActNiceBean.publishData(out);
                }
                
                out.println("</body>");
                out.println("</html>");
            }
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
    @Override
	public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    }

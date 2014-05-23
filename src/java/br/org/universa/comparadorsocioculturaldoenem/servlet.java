/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.universa.comparadorsocioculturaldoenem;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Matheus
 */
public class servlet extends HttpServlet {

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
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            String fator = request.getParameter("fator");
            if(fator!=null){
                String s =
"{ \"labels\" : [\"DF\",\"GO\",\"SP\",\"MG\",\"RS\",\"SC\"],"+
	"\"datasets\" : ["+
		"{"+
			"\"fillColor\" : \"rgba(220,220,220,0.5)\","+
			"\"strokeColor\" : \"rgba(220,220,220,1)\","+
			"\"pointColor\" : \"rgba(220,220,220,1)\","+
			"\"pointStrokeColor\" : \"#fff\","+
			"\"data\" : [65,59,90,81,56,55,40]"+
		"},"+
		"{"+
                	"\"fillColor\" : \"rgba(151,187,205,0.5)\","+
			"\"strokeColor\" : \"rgba(151,187,205,1)\","+
			"\"pointColor\" : \"rgba(151,187,205,1)\","+
			"\"pointStrokeColor\" : \"#fff\","+
			"\"data\" : [28,48,40,19,96,27,100]"+
		"}"+
	"]"+
"}";
                out.println(s);
            } else {
                
            }
        } finally {
            out.close();
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
        return "Retorna dados para o gráfico. É preciso passar o parâmetro 'fator' com um valor entre 1 e 62.";
    }// </editor-fold>

}

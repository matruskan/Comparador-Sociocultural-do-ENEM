/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.universa.comparadorsocioculturaldoenem;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
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
//                String s =
//"{ \"labels\" : [\"DF\",\"GO\",\"SP\",\"MG\",\"RS\",\"SC\"],"+
//	"\"datasets\" : ["+
//		"{"+
//			"\"fillColor\" : \"rgba(220,220,220,0.5)\","+
//			"\"strokeColor\" : \"rgba(220,220,220,1)\","+
//			"\"pointColor\" : \"rgba(220,220,220,1)\","+
//			"\"pointStrokeColor\" : \"#fff\","+
//			"\"data\" : [65,59,90,81,56,55,40]"+
//		"},"+
//		"{"+
//                	"\"fillColor\" : \"rgba(151,187,205,0.5)\","+
//			"\"strokeColor\" : \"rgba(151,187,205,1)\","+
//			"\"pointColor\" : \"rgba(151,187,205,1)\","+
//			"\"pointStrokeColor\" : \"#fff\","+
//			"\"data\" : [28,48,40,19,96,27,100]"+
//		"}"+
//	"]"+
//"}";
//                out.println(s);
                write(out,getDxChart());
            } else {
                
            }
        } finally {
            out.close();
        }
    }
    
    private JsonObject getDxChart(){
        return Json.createObjectBuilder()
                .add("palette", "Soft Pastel")
                .add("dataSource", getDataSource())
                .add("commonSeriesSettings",getCommonSeriesSettings())
                .add("series",Json.createArrayBuilder()
                    .add(getSeriesNota())
                    .add(getSeriesEscolhida()).build())
                .build();
    }
    
    private void write(Writer writer, JsonObject model){
        try (JsonWriter jsonWriter = Json.createWriter(writer)) {
           jsonWriter.writeObject(model);
        }
    }
    
    private JsonObject getCommonSeriesSettings(){
        return Json.createObjectBuilder()
                .add("argumentField","UF")
                .build();
    }
    
    private JsonObject getSeriesNota() {
        return Json.createObjectBuilder()
                .add("valueField", "nota")
                .add("name","Nota").build();
    }
    
    private JsonObject getSeriesEscolhida() {
        return Json.createObjectBuilder()
                .add("valueField", "R")
                .add("name","Até quando seu pai estudou?").build();
    }
    
    private JsonArray getDataSource(){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(int i = 0; i< UF.values().length; i++){
                jsonArrayBuilder.add(getDataUF(UF.values()[i]));
        }
        return jsonArrayBuilder.build();
    }
    
    private JsonObject getDataUF(UF uf){
        char[] valores = {'a','b','c','d','e','f'};
        return Json.createObjectBuilder()
                .add("UF",uf.toString())
                .add("nota",(int)(Math.random()*100))
                .add("R",valores[(int)(Math.random()*valores.length)]+"")
                .build();
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

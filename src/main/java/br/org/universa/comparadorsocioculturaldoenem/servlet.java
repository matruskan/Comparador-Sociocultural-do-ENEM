/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.universa.comparadorsocioculturaldoenem;

import br.org.universa.comparadorsocioculturaldoenem.modelo.UF;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
    private static final String NOTA = "Nota";
    private static final String RESPOSTA = "Resposta";
    
    private String[] valores = {"a","b","c","d","e","f"};

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
            String prova = request.getParameter("prova");
            if(fator!=null && prova!=null){
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
                write(out,getDxChart(prova, fator));
            } else {
                
            }
        } finally {
            out.close();
        }
    }
    
    private JsonObject getDxChart(String prova, String fator){
        return Json.createObjectBuilder()
                .add("palette", "Soft Pastel")
                .add("dataSource", getDataSource(prova, fator))
                .add("commonSeriesSettings",getCommonSeriesSettings())
                .add("series",getSeries())
                .add("valueAxis",Json.createArrayBuilder()
                    .add(getAxisNota())
                    .add(getAxisEscolha()).build())
                .add("legend",Json.createObjectBuilder()
                    .add("verticalAlignment", "bottom")
                    .add("horizontalAlignment", "center").build())
                .add("tooltip",getTooltip())
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
                .add("axis", NOTA)
                .add("type","fullstackedbar")
                .build();
    }
    
    private JsonObject getSeriesNota() {
        return Json.createObjectBuilder()
                .add("valueField", NOTA)
                .add("name",NOTA)
                .add("type","line").build();
    }
    
    private JsonArray getSeries() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
//        for(int i=0; i<valores.length;i++){
//            jab.add(Json.createObjectBuilder()
//                .add("valueField", valores[i])
//                .add("axis", RESPOSTA)
//                .add("name",valores[i])
//                .build());
//        }
        return jab.add(getSeriesNota()).build();
    }
    
    private JsonArray getDataSource(String prova, String fator){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(int i = 0; i< UF.values().length; i++){
            jsonArrayBuilder.add(getDataUF(UF.values()[i], prova));
        }
        return jsonArrayBuilder.build();
    }
    
    private JsonObject getDataUF(UF uf, String prova){
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("UF",uf.toString());
        if(prova.equals("CN")) {
            job.add(NOTA, uf.getMediaCN());
        }
        if (prova.equals("CH")) {
            job.add(NOTA, uf.getMediaCH());
        }
        if (prova.equals("LC")) {
            job.add(NOTA, uf.getMediaLC());
        }
        if (prova.equals("MT")) {
            job.add(NOTA, uf.getMediaMT());
        }
        for(int i = 0; i<valores.length; i++){
            job.add(valores[i],(int)(Math.random()*100));
        }
        return job.build();
    }
    
    private JsonObject getAxisNota(){
        return Json.createObjectBuilder()
                .add("name",NOTA)
                .add("position","right")
                .add("grid",Json.createObjectBuilder()
                    .add("visible",true).build())
                .add("title",Json.createObjectBuilder()
                    .add("text","Nota").build())
                .build();
    }
    
    private JsonObject getAxisEscolha(){
        return Json.createObjectBuilder()
                .add("name",RESPOSTA)
                .add("position","left")
//                .add("type","discrete")
//                .add("categories",Json.createArrayBuilder()
//                    .add("a").add("b")
//                    .add("c").add("d")
//                    .add("e").add("f").build())
//                .add("grid",Json.createObjectBuilder()
//                    .add("visible",true).build())
                .add("title",Json.createObjectBuilder()
                    .add("text","Resposta").build())
//                .add("label",Json.createObjectBuilder()
//                    .add("rotationAngle",-90).build())
                .build();
    }
    
    private JsonObject getTooltip(){
        return Json.createObjectBuilder()
                .add("enabled",true)
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

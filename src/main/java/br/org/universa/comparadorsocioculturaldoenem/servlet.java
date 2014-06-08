/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.universa.comparadorsocioculturaldoenem;

import br.org.universa.comparadorsocioculturaldoenem.modelo.Resposta;
import br.org.universa.comparadorsocioculturaldoenem.modelo.RespostaDAO;
import br.org.universa.comparadorsocioculturaldoenem.modelo.UF;
import br.org.universa.comparadorsocioculturaldoenem.questoes.Questoes;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.util.List;
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

    public static final String NOTA = "Nota";
    private static final String RESPOSTA = "Resposta";

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
            int numeroDaQuestao = Integer.parseInt(fator);
            String prova = request.getParameter("prova");
            if (fator != null && prova != null) {
                JsonObject jsonObject = getDxChart(prova, numeroDaQuestao);
                write(out, jsonObject);
            } else {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
        }
    }

    private JsonObject getDxChart(String prova, int numeroDaQuestao) {

        UF[] ufs = getUFsOrdenadosPorNota(prova);
        DataSourceBuilder dsb = new DataSourceBuilder(prova, numeroDaQuestao, ufs);
        return Json.createObjectBuilder()
                .add("provaEscolhida", getProvaEscolhida(prova))
                .add("fatorEscolhido", numeroDaQuestao)
                .add("estadosComMaiorNota", getEstadosComMaiorNota(ufs))
                .add("estadosComMenorNota", getEstadosComMenorNota(ufs))
                .add("palette", getPalette(numeroDaQuestao))
                //                .add("palette", "Soft Pastel")
                .add("dataSource", dsb.getDataSource())
                .add("commonSeriesSettings", getCommonSeriesSettings())
                .add("series", getSeries(numeroDaQuestao))
                .add("valueAxis", Json.createArrayBuilder()
                        .add(getAxisNota())
                        .add(getAxisEscolha()).build())
                .add("legend", Json.createObjectBuilder()
                        .add("verticalAlignment", "bottom")
                        .add("horizontalAlignment", "center").build())
                .add("tooltip", Json.createObjectBuilder()
                        .add("enabled", true).build())
                .add("respostasMaiorNota", getJsonArray(dsb.getRespostasMaioresNotas()))
                .add("respostasMenorNota", getJsonArray(dsb.getRespostasMenoresNotas()))
                .build();
    }

    private UF[] getUFsOrdenadosPorNota(String prova) {
        if (prova.equals("CN")) {
            return UF.ordenadoPorCN();
        }
        if (prova.equals("CH")) {
            return UF.ordenadoPorCH();
        }
        if (prova.equals("LC")) {
            return UF.ordenadoPorLC();
        }
        if (prova.equals("MT")) {
            return UF.ordenadoPorMT();
        }
        return UF.values();
    }

    private String getProvaEscolhida(String prova) {
        if (prova.equals("CN")) {
            return "Ciências da Natureza";
        }
        if (prova.equals("CH")) {
            return "Ciências Humanas";
        }
        if (prova.equals("LC")) {
            return "Linguagens e Códigos";
        }
        if (prova.equals("MT")) {
            return "Matemática";
        }
        return "";
    }

    private String getEstadosComMaiorNota(UF[] ufs) {
        StringBuilder sb = new StringBuilder(26);
        for (int i = ufs.length - 1; i > ufs.length - 7; i--) {
            sb.append(ufs[i].toString()).append(", ");
        }
        sb.append(ufs[ufs.length - 7]);
        return sb.toString();
    }

    private String getEstadosComMenorNota(UF[] ufs) {
        StringBuilder sb = new StringBuilder(26);
        for (int i = 0; i < 6; i++) {
            sb.append(ufs[i].toString()).append(", ");
        }
        sb.append(ufs[6]);
        return sb.toString();
    }

    private JsonArray getJsonArray(List<String> strings) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (String s : strings) {
            jab.add(s);
        }
        return jab.build();
    }

    private void write(Writer writer, JsonObject model) {
        try (JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.writeObject(model);
        }
    }

    private JsonArray getPalette(int numeroDaQuestao) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        int quantidadeDeRespostas = new Questoes().getOpcoes(numeroDaQuestao).size();
        for (int i = 0; i < quantidadeDeRespostas; i++) {
            String cor = Integer.toString(220 * (i + 1) / (quantidadeDeRespostas + 1));
            cor = "rgba(" + cor + "," + cor + "," + cor + ",0.7)";
//            System.out.println("Cor: "+cor);
            jab.add(cor);
        }
        return jab.build();
    }

    private JsonObject getCommonSeriesSettings() {
        return Json.createObjectBuilder()
                .add("argumentField", "UF")
                .add("axis", NOTA)
                .add("type", "spline")
                .add("hoverStyle", Json.createObjectBuilder()
                        .add("color", "red").build())
                .add("selectionStyle", Json.createObjectBuilder()
                        .add("border", Json.createObjectBuilder()
                                .add("color", "red").build()).build())
                .build();
    }

    private JsonObject getSeriesNota() {
        return Json.createObjectBuilder()
                .add("color", "blue")
                .add("valueField", NOTA)
                .add("name", NOTA)
                .add("type", "spline").build();
    }

    private JsonArray getSeries(int numeroDaQuestao) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        List<String> opcoes = new Questoes().getOpcoes(numeroDaQuestao);
        for (int i = opcoes.size() - 1; i >= 0; i--) {
            if (!opcoes.get(i).trim().isEmpty()) {
                jab.add(Json.createObjectBuilder()
                        .add("valueField", i)
                        .add("axis", RESPOSTA)
                        .add("name", opcoes.get(i))
                        .build());
            }
        }
        return jab.add(getSeriesNota()).build();
    }

    private JsonObject getAxisNota() {
        return Json.createObjectBuilder()
                .add("name", NOTA)
                .add("position", "right")
                .add("grid", Json.createObjectBuilder()
                        .add("visible", true).build())
                .add("title", Json.createObjectBuilder()
                        .add("text", "Média das Notas").build())
                .build();
    }

    private JsonObject getAxisEscolha() {
        return Json.createObjectBuilder()
                .add("name", RESPOSTA)
                .add("position", "left")
                //                .add("type","discrete")
                //                .add("categories",Json.createArrayBuilder()
                //                    .add("a").add("b")
                //                    .add("c").add("d")
                //                    .add("e").add("f").build())
                //                .add("grid",Json.createObjectBuilder()
                //                    .add("visible",true).build())
                .add("title", Json.createObjectBuilder()
                        .add("text", "Quantidade de Respostas").build())
                .add("label", Json.createObjectBuilder()
                        .add("customizeText", "customize(value)").build())
                //                    .add("rotationAngle",-90).build())
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

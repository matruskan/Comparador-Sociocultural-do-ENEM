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

    private static final String NOTA = "Nota";
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

        return Json.createObjectBuilder()
                .add("palette", getPalette(numeroDaQuestao))
//                .add("palette", "Soft Pastel")
                .add("dataSource", getDataSource(prova, numeroDaQuestao))
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
                .build();
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
            String cor = Integer.toHexString(0xff * (i + 1) / (quantidadeDeRespostas + 1));
            cor = "#" + cor + cor + cor;
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
            jab.add(Json.createObjectBuilder()
                    .add("valueField", i + 1)
                    .add("axis", RESPOSTA)
                    .add("name", opcoes.get(i))
                    .build());
        }
        return jab.add(getSeriesNota()).build();
    }

    private JsonArray getDataSource(String prova, int numeroDaQuestao) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < UF.values().length; i++) {
            jsonArrayBuilder.add(getDataUF(UF.ordenadoPorCN()[i], prova, numeroDaQuestao));
        }
        return jsonArrayBuilder.build();
    }

    private JsonObject getDataUF(UF uf, String prova, int numeroDaQuestao) {
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("UF", uf.toString());
        if (prova.equals("CN")) {
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
        adicionaRespostas(job, uf, numeroDaQuestao);
        return job.build();
    }

    private void adicionaRespostas(JsonObjectBuilder job, UF uf, int numeroDaQuestao) {
        try (Connection conexao = new Conexao().getConexao()) {
            RespostaDAO respostaDAO = new RespostaDAO(conexao);
            List<Resposta> respostas = respostaDAO.getRespostas(uf, numeroDaQuestao);
            int total = 0;
            for (Resposta resposta : respostas) {
                total += resposta.getTotal();
            }
//            List<String> opcoes = new Questoes().getOpcoes(numeroDaQuestao);
            for (Resposta resposta : respostas) {
//                System.out.println("Resposta: "+resposta.getOpcao()+", total: "+resposta.getTotal());
                job.add(resposta.getOpcao().toString(), resposta.getTotal() * 100 / total);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

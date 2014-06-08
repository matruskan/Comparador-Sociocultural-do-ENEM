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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Matheus
 */
public class DataSourceBuilder {

    private String prova;
    private int numeroDaQuestao;
    private UF[] ufs;

    private int[][] porcentagemPorUFePorResposta = new int[UF.values().length][50];

    private List<String> respostasMaioresNotas = new ArrayList<>();
    private List<String> respostasMenoresNotas = new ArrayList<>();

    private List<String> opcoes;

    public DataSourceBuilder(String prova, int numeroDaQuestao, UF[] ufs) {
        this.prova = prova;
        this.numeroDaQuestao = numeroDaQuestao;
        this.ufs = ufs;
        opcoes = new Questoes().getOpcoes(numeroDaQuestao);
    }

    public JsonArray getDataSource() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < ufs.length; i++) {
            jsonArrayBuilder.add(getDataUF(ufs[i], prova, numeroDaQuestao));
        }
        calcularMaioresRespostas();
        return jsonArrayBuilder.build();
    }

    private JsonObject getDataUF(UF uf, String prova, int numeroDaQuestao) {
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("UF", uf.toString());
        if (prova.equals("CN")) {
            job.add(servlet.NOTA, uf.getMediaCN());
        }
        if (prova.equals("CH")) {
            job.add(servlet.NOTA, uf.getMediaCH());
        }
        if (prova.equals("LC")) {
            job.add(servlet.NOTA, uf.getMediaLC());
        }
        if (prova.equals("MT")) {
            job.add(servlet.NOTA, uf.getMediaMT());
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
            for (Resposta resposta : respostas) {
                int porcentagem = resposta.getTotal() * 100 / total;
                porcentagemPorUFePorResposta[uf.ordinal()][resposta.getOpcao()] = porcentagem;
//                System.out.println("Resposta: "+resposta.getOpcao()+", total: "+resposta.getTotal());
                job.add(resposta.getOpcao().toString(), porcentagem);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void calcularMaioresRespostas() {
        for (int numeroDaResposta = 0; numeroDaResposta < porcentagemPorUFePorResposta[0].length; numeroDaResposta++) {
            int porcentagemTotalPioresNotas = 0;
            for (int i = 0; i < 7; i++) {
                porcentagemTotalPioresNotas += porcentagemPorUFePorResposta[ufs[i].ordinal()][numeroDaResposta];

            }
            int porcentagemTotalMelhoresNotas = 0;
            for (int i = ufs.length - 1; i > ufs.length - 8; i--) {
                porcentagemTotalMelhoresNotas += porcentagemPorUFePorResposta[ufs[i].ordinal()][numeroDaResposta];
            }
            if (porcentagemTotalPioresNotas > porcentagemTotalMelhoresNotas) {
                respostasMenoresNotas.add(opcoes.get(numeroDaResposta));
            }
            if (porcentagemTotalPioresNotas < porcentagemTotalMelhoresNotas) {
                respostasMaioresNotas.add(opcoes.get(numeroDaResposta));
            }
        }
    }

    public List<String> getRespostasMaioresNotas() {
        return respostasMaioresNotas;
    }

    public List<String> getRespostasMenoresNotas() {
        return respostasMenoresNotas;
    }
}

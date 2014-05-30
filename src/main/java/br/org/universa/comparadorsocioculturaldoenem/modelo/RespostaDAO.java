/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.universa.comparadorsocioculturaldoenem.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matheus
 */
public class RespostaDAO {

    private static final String TABLE = "resposta";
    private static final String RESP_OPCAO = "resp_opcao";
    private static final String RESP_PERGUNTA = "resp_pergunta";
    private static final String RESP_UF = "resp_uf";
    private static final String RESP_TOTAL = "resp_total";
    private static final String INSERT
            = "INSERT INTO " + TABLE + " ("
            + RESP_UF + ", " + RESP_PERGUNTA + ", " + RESP_OPCAO + ", " + RESP_TOTAL
            + ") VALUES(?,?,?,?)";
    private static final String BUSCA
            = "SELECT " + RESP_OPCAO + ", " + RESP_TOTAL + " FROM " + TABLE
            + " WHERE " + RESP_UF + "=? AND " + RESP_PERGUNTA + "=?";
    private static final String CREATE
            = "CREATE TABLE IF NOT EXISTS " + TABLE + "("
            + "resp_id SERIAL NOT NULL PRIMARY KEY,"
            + RESP_UF + " INT,"
            + RESP_PERGUNTA + " INT,"
            + RESP_OPCAO + " INT,"
            + RESP_TOTAL + " INT"
            + ")";

    private Connection conexao;

    public RespostaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public int criarDatabase() throws SQLException {
        int retorno;
        try (Statement statement = conexao.createStatement()) {
            retorno = statement.executeUpdate(CREATE);
        }
        return retorno;
    }

    public int inserirResposta(UF uf, int pergunta, int opcao, int total) throws SQLException {
        int retorno;
        try (PreparedStatement preparedStatement = conexao.prepareStatement(INSERT)) {
            preparedStatement.setInt(1, uf.ordinal());
            preparedStatement.setInt(2, pergunta);
            preparedStatement.setInt(3, opcao);
            preparedStatement.setInt(4, total);
            retorno = preparedStatement.executeUpdate();
        }
        return retorno;
    }

    public int inserirRespostas(List<Resposta> respostas) throws SQLException {
        int retorno = 0;
        try (PreparedStatement preparedStatement = conexao.prepareStatement(INSERT)) {
            for (Resposta r : respostas) {
                preparedStatement.setInt(1, r.getUf().ordinal());
                preparedStatement.setInt(2, r.getPergunta());
                preparedStatement.setInt(3, r.getOpcao());
                preparedStatement.setInt(4, r.getTotal());
                retorno += preparedStatement.executeUpdate();
            }
        }
        return retorno;
    }

    public List<Resposta> getRespostas(UF uf, int pergunta) throws SQLException {
        List<Resposta> respostas = new ArrayList<>();
        try (PreparedStatement preparedStatement = conexao.prepareStatement(BUSCA)) {
            preparedStatement.setInt(1, uf.ordinal());
            preparedStatement.setInt(2, pergunta);

            ResultSet rs = preparedStatement.executeQuery();
            Resposta r;
            while (rs.next()) {
                r = new Resposta();
                r.setUf(uf);
                r.setPergunta(pergunta);
                r.setOpcao(rs.getInt(RESP_OPCAO));
                r.setTotal(rs.getInt(RESP_TOTAL));
                respostas.add(r);
            }
        }
        return respostas;
    }
}

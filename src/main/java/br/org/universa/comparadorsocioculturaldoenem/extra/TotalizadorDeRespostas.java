/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.universa.comparadorsocioculturaldoenem.extra;

import br.org.universa.comparadorsocioculturaldoenem.Conexao;
import br.org.universa.comparadorsocioculturaldoenem.modelo.Resposta;
import br.org.universa.comparadorsocioculturaldoenem.modelo.RespostaDAO;
import br.org.universa.comparadorsocioculturaldoenem.modelo.UF;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Matheus
 */
public class TotalizadorDeRespostas {

    static final int COLUNA_UF = 6;
    static final int PRIMEIRA_COLUNA_COM_RESPOSTAS = 2;
    static final int QUANTIDADE_DE_RESPOSTAS = 62;
    Map<String, Integer> mapaUfs;
    // Arquivo com dados (contém a UF)
    String arquivoDados = "C:\\Users\\Matheus\\Desktop\\DADOS_ENEM_2012.csv";
    // Arquivo com questionário
    String arquivoQuestionario = "C:\\Users\\Matheus\\Desktop\\QUESTIONARIO_ENEM_2012.csv";

    // [UF][Questão][Resposta]
    // Questão 4 tem mais questões, vai de 1 a 20.
    // A posição [21] das respostas será usada para contar as perguntas não
    // respondidas.
    int total[][][] = new int[UF.values().length][QUANTIDADE_DE_RESPOSTAS][50];

    public static void main(String args[]) throws IOException, URISyntaxException, SQLException, ClassNotFoundException {
        long tempo = System.currentTimeMillis();
        new TotalizadorDeRespostas().total();
        tempo = System.currentTimeMillis() - tempo;
        System.out.println("Tempo: " + tempo + " ms");
    }

    public void total() throws IOException, URISyntaxException, SQLException, ClassNotFoundException {
        construirMapaUfs();
        try (BufferedReader dados = new BufferedReader(new FileReader(arquivoDados));
                BufferedReader questionario = new BufferedReader(new FileReader(arquivoQuestionario))) {
            dados.readLine();
            questionario.readLine();
            String linhaDados;
            String linhaQuestionario;
            String[] respostas;
            int uf;
            while ((linhaDados = dados.readLine()) != null
                    && (linhaQuestionario = questionario.readLine()) != null) {
                linhaDados = linhaDados.replaceAll("\"", " ");
                linhaQuestionario = linhaQuestionario.replaceAll("\"", " ");
                uf = mapaUfs.get(linhaDados.split(",")[COLUNA_UF].trim());
//                System.out.print("UF:"+uf);
                respostas = linhaQuestionario.split(",");

                for (int i = 0; i < QUANTIDADE_DE_RESPOSTAS; i++) {
//                    System.out.print(i+". ");
                    total[uf][i][getResposta(respostas[i + PRIMEIRA_COLUNA_COM_RESPOSTAS])]++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        imprimirTotal();
        salvarTotais();
    }

    private int getResposta(String leitura) {
        leitura = leitura.trim();
        if (leitura.isEmpty()) {
            return 21;
        }
        try {
            return Integer.parseInt(leitura);
        } catch (NumberFormatException nfex) {
//            System.out.println("leitura:"+leitura);
            return ((int) leitura.charAt(0)) - 64; //char 'A' = int 65
        }
    }

    private void imprimirTotal() {
        for (UF uf : UF.values()) {
            System.out.print(uf);
            System.out.print(" ");
            for (int i = 0; i < QUANTIDADE_DE_RESPOSTAS; i++) {
                System.out.print("Q" + (i + 1) + ":[");
                for (int j = 0; j < 22; j++) {
                    System.out.print(total[uf.ordinal()][i][j]);
                    System.out.print("\t");
                }
                System.out.print("]\t");
            }
            System.out.println();
        }
    }

    private void salvarTotais() throws URISyntaxException, SQLException, ClassNotFoundException {
        Resposta r;
        int totalDeRespostas;
        try (Connection conexao = new Conexao().getConexao()) {
            RespostaDAO respostaDAO = new RespostaDAO(conexao);
            for (UF uf : UF.values()) {
                for (int i = 0; i < QUANTIDADE_DE_RESPOSTAS; i++) {
                    List<Resposta> respostas = new ArrayList<>();
//                    System.out.print("Q" + (i + 1) + ":[");
                    for (int j = 0; j < 22; j++) {
                        totalDeRespostas = total[uf.ordinal()][i][j];
                        if (totalDeRespostas > 0) {
                            r = new Resposta(uf, i + 1, j, totalDeRespostas);
                            respostas.add(r);
                        }
                    }
                    respostaDAO.inserirRespostas(respostas);
                }
                System.out.println();
            }
        }
    }

    private void construirMapaUfs() {
        mapaUfs = new HashMap<>(27);
        for (UF uf : UF.values()) {
            mapaUfs.put(uf.toString(), uf.ordinal());
        }
    }
}

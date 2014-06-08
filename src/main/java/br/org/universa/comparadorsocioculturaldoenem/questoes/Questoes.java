/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.universa.comparadorsocioculturaldoenem.questoes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matheus
 */
public class Questoes {
    
    public List<String> getOpcoes(int numeroDaQuestao) {
        if (numeroDaQuestao > 7 && numeroDaQuestao <= 21) {
            numeroDaQuestao = 7;
        }
        if (numeroDaQuestao > 23 && numeroDaQuestao <= 29) {
            numeroDaQuestao = 23;
        }
        String nomeDoArquivo = "questao" + numeroDaQuestao + ".txt";
        List<String> opcoes = new ArrayList<String>();
//        System.out.println(getClass().getClassLoader().getResource(nomeDoArquivo).getFile());
        try (BufferedReader buff = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(nomeDoArquivo), "UTF-8"))) {
            String linha;
            while ((linha = buff.readLine()) != null) {
                opcoes.add(linha);
            }
        } catch (Exception ex) {
            Logger.getLogger(Questoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return opcoes;
    }

}

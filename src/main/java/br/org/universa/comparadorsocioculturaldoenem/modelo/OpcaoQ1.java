/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.universa.comparadorsocioculturaldoenem.modelo;

/**
 *
 * @author Matheus
 */
public enum OpcaoQ1 {

    A("Não estudou"),
    B("Da 1ª à 4ª série do Ensino Fundamental (antigo primário);"),
    C("Da 5ª à 8ª série do Ensino Fundamental (antigo ginásio);"),
    D("Ensino Médio (antigo 2º grau) incompleto;"),
    E("Ensino Médio (antigo 2º grau);"),
    F("Ensino Superior incompleto;"),
    G("Ensino Superior;"),
    H("Pós-graduação;"),
    I("Não sei;");
    
    private String label;

    private OpcaoQ1(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public static OpcaoQ1 getOpcao(int resposta){
        return OpcaoQ1.values()[resposta-1];
    }
}

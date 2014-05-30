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
public class Resposta {
    private UF uf;
    private Integer pergunta;
    private Integer opcao;
    private Integer total;

    public Resposta() {
    }
    
    public Resposta(UF uf, Integer pergunta, Integer opcao, Integer total) {
        this.uf = uf;
        this.pergunta = pergunta;
        this.opcao = opcao;
        this.total = total;
    }
    
    public UF getUf() {
        return uf;
    }

    public void setUf(UF uf) {
        this.uf = uf;
    }

    public Integer getPergunta() {
        return pergunta;
    }

    public void setPergunta(Integer pergunta) {
        this.pergunta = pergunta;
    }

    public Integer getOpcao() {
        return opcao;
    }

    public void setOpcao(Integer opcao) {
        this.opcao = opcao;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
    
}

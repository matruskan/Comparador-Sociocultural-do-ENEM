package br.org.universa.comparadorsocioculturaldoenem.modelo;

import java.util.Arrays;
import java.util.Comparator;

/**
 * As médias foram obtidas da saída de uma execução do Totalizador.
 * 
 * @author Matheus
 */
public enum UF {

    AC("Acre",443.53, 490.27, 465.93, 447.48),
    AL("Alagoas",456.15, 506.54, 475.37, 478.60),
    AM("Amazonas",441.95, 495.65, 466.18, 458.26),
    AP("Amapá",450.96, 507.52, 474.39, 460.39),
    BA("Bahia",460.31, 514.96, 484.11, 482.76),
    CE("Ceará",464.00, 514.40, 484.56, 494.16),
    DF("Distrito Federal",471.90, 530.77, 502.82, 510.53),
    ES("Espirito Santo",485.08, 527.29, 497.40, 528.37),
    GO("Goias",469.38, 517.00, 491.61, 505.24),
    MA("Maranhão",449.00, 501.18, 471.90, 466.44),
    MG("Minas Gerais",488.79, 535.12, 508.18, 540.23),
    MS("Mato Grosso Sul",456.90, 505.66, 477.93, 485.36),
    MT("Mato Grosso",453.97, 503.76, 477.04, 479.37),
    PA("Pará",453.59, 505.85, 473.33, 464.43),
    PB("Paraiba",459.53, 510.75, 481.22, 487.38),
    PE("Pernanbuco",466.41, 517.21, 487.38, 496.52),
    PI("Piauí",454.93, 503.05, 472.53, 472.50),
    PR("Parana",484.61, 532.10, 503.25, 528.14),
    RJ("Rio de Janeiro",489.57, 545.97, 514.04, 542.90),
    RN("Rio Grande do Norte",465.95, 513.70, 483.85, 491.86),
    RO("Rondônia",455.80, 501.49, 476.67, 478.86),
    RR("Roraima",455.23, 507.45, 477.19, 476.87),
    RS("Rio Grande do Sul",478.80, 523.20, 499.17, 522.04),
    SC("Santa Catarina",495.03, 536.53, 503.47, 540.62),
    SE("Sergipe",453.92, 503.84, 473.39, 473.16),
    SP("São Paulo",487.44, 540.22, 514.72, 541.18),
    TO("Tocantins",452.22, 499.92, 474.26, 475.97);
    private String nome;
    private double mediaCN;
    private double mediaCH;
    private double mediaLC;
    private double mediaMT;

    private UF(String nome, double mediaCN, double mediaCH, double mediaLC, double mediaMT) {
        this.nome = nome;
        this.mediaCN = mediaCN;
        this.mediaCH = mediaCH;
        this.mediaLC = mediaLC;
        this.mediaMT = mediaMT;
    }

//    private UF(String nome, double mediaCN) {
//        this.nome = nome;
//        this.mediaCN = mediaCN;
//    }

    public String getNome() {
        return nome;
    }

    public double getMediaCN() {
        return mediaCN;
    }

    public double getMediaCH() {
        return mediaCH;
    }

    public double getMediaLC() {
        return mediaLC;
    }

    public double getMediaMT() {
        return mediaMT;
    }
    
    public static UF[] ordenadoPorCN(){
        UF[] ufs = UF.values();
        Arrays.sort(ufs, new Comparator<UF>(){

            @Override
            public int compare(UF uf1, UF uf2) {
                if(uf1.getMediaCN()>uf2.getMediaCN()) return 1;
                if(uf1.getMediaCN()<uf2.getMediaCN()) return -1;
                return 0;
            }
            
        });
        return ufs;
    }
}

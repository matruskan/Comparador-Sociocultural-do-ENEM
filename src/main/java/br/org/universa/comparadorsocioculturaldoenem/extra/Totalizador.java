/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.universa.comparadorsocioculturaldoenem.extra;

import br.org.universa.comparadorsocioculturaldoenem.modelo.UF;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * À partir do arquivo DADOS_ENEM_2012.csv, obtém a média das notas das provas
 * do ENEM.
 * 
 * @author Matheus
 */
public class Totalizador {

    static final int COLUNA_UF = 6;
    static final int COLUNA_NOTA_CN = 56;
    static final int COLUNA_NOTA_CH = 57;
    static final int COLUNA_NOTA_LC = 58;
    static final int COLUNA_NOTA_MT = 59;
    static final double TAMANHO_DA_AMOSTRA = 5000;
    String arquivo = "C:\\Users\\Matheus\\Desktop\\DADOS_ENEM_2012.csv";
    Map<String,Integer> mapaUfs;
    List<Double> notasCN[] = new ArrayList[27];
    List<Double> notasCH[] = new ArrayList[27];
    List<Double> notasLC[] = new ArrayList[27];
    List<Double> notasMT[] = new ArrayList[27];

    public static void main(String args[]) throws IOException {
        long tempo = System.currentTimeMillis();
        new Totalizador().total();
        tempo = System.currentTimeMillis() - tempo;
        System.out.println("Tempo: "+tempo+" ms");
    }
    
    public void total() throws IOException {
        construirMapaUfs();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = null;
            double notaCN[] = new double[27];
            double notaCH[] = new double[27];
            double notaLC[] = new double[27];
            double notaMT[] = new double[27];
            int contadorCN[] = new int[27];
            int contadorCH[] = new int[27];
            int contadorLC[] = new int[27];
            int contadorMT[] = new int[27];
            String valores[];
            int uf;
            double media;
            br.readLine();
            while ((linha = br.readLine()) != null) {
                linha = linha.replaceAll("\"", "");
                valores = linha.split(",");
                uf = mapaUfs.get(valores[COLUNA_UF]);
                try{
                    notaCN[uf] += Double.parseDouble(valores[COLUNA_NOTA_CN]);
                    if(contadorCN[uf]++ == TAMANHO_DA_AMOSTRA) {
                        media = notaCN[uf] / TAMANHO_DA_AMOSTRA;
                        notasCN[uf].add(media);
//                        System.out.println(valores[COLUNA_UF]+": CN-"+media);
                        contadorCN[uf] = 0;
                        notaCN[uf] = 0;
                    }
                }catch(NumberFormatException nfex){}//Nota 0
                try{
                    notaCH[uf] += Double.parseDouble(valores[COLUNA_NOTA_CH]);
                    if(contadorCH[uf]++ == TAMANHO_DA_AMOSTRA) {
                        media = notaCH[uf] / TAMANHO_DA_AMOSTRA;
                        notasCH[uf].add(media);
//                        System.out.println(valores[COLUNA_UF]+": CH-"+media);
                        contadorCH[uf] = 0;
                        notaCH[uf] = 0;
                    }
                }catch(NumberFormatException nfex){}//Nota 0
                try{
                    notaLC[uf] += Double.parseDouble(valores[COLUNA_NOTA_LC]);
                    if(contadorLC[uf]++ == TAMANHO_DA_AMOSTRA) {
                        media = notaLC[uf] / TAMANHO_DA_AMOSTRA;
                        notasLC[uf].add(media);
//                        System.out.println(valores[COLUNA_UF]+": LC-"+media);
                        contadorLC[uf] = 0;
                        notaLC[uf] = 0;
                    }
                }catch(NumberFormatException nfex){}//Nota 0
                try{
                    notaMT[uf] += Double.parseDouble(valores[COLUNA_NOTA_MT]);
                    if(contadorMT[uf]++ == TAMANHO_DA_AMOSTRA) {
                        media = notaMT[uf] / TAMANHO_DA_AMOSTRA;
                        notasMT[uf].add(media);
//                        System.out.println(valores[COLUNA_UF]+": MT-"+media);
                        contadorMT[uf] = 0;
                        notaMT[uf] = 0;
                    }
                }catch(NumberFormatException nfex){}//Nota 0
            }//fim while ((linha = br.readLine()) != null)
        }
        for(int i = 0;i<27; i++) {
            BigDecimal totalCN = calcularMedia(notasCN[i]);
            BigDecimal totalCH = calcularMedia(notasCH[i]);
            BigDecimal totalLC = calcularMedia(notasLC[i]);
            BigDecimal totalMT = calcularMedia(notasMT[i]);
            System.out.println(UF.values()[i]+": "+totalCN.toString()+", "+totalCH.toString()+", "+totalLC.toString()+", "+totalMT.toString()+"),");//+" maior: "+maioresNotas[i]+" menor: "+menoresNotas[i]);
        }
    }
    
    private BigDecimal calcularMedia(List<Double> amostras) {
        BigDecimal total = new BigDecimal(0);
        for (Double d : amostras) {
            total = total.add(new BigDecimal(d));
        }
        return total.divide(new BigDecimal(amostras.size()), 2, RoundingMode.HALF_UP);
    }
    
    private void construirMapaUfs(){
        mapaUfs = new HashMap<>(27);
        for(UF uf : UF.values()){
            mapaUfs.put(uf.toString(), uf.ordinal());
        }
        for(int i = 0; i<27;i++){
            notasCN[i] = new ArrayList<>();
            notasCH[i] = new ArrayList<>();
            notasLC[i] = new ArrayList<>();
            notasMT[i] = new ArrayList<>();
        }
    }
}

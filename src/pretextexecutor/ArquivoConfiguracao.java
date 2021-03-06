/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pretextexecutor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pretextexecutor.PretextExecutor.lerArquivo;
import static pretextexecutor.PretextExecutor.printFile;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class ArquivoConfiguracao {

    List<Parametro> parametros;

    public ArquivoConfiguracao(List<Parametro> parametros) {
        this.parametros = parametros;
        criarArquivConfiguracao(parametros);
    }

    private void criarArquivConfiguracao(List<Parametro> parametros) {
        String arquivoConfiguracao = criarModelo();

        String stopFile = "";
        String gram = "";
        String nomeBaseDados = "";
        String diretorioStopList = "";

        for (Parametro parametro : parametros) {
            switch (parametro.getNome()) {
                case "stopFile":
                    stopFile = parametro.getValor();
                    break;
                case "gram":
                    gram = parametro.getValor();
                    break;
                case "nomeBaseDados":
                    nomeBaseDados = parametro.getValor();
                    break;
                case "diretorioStopList":
                    diretorioStopList = parametro.getValor();
                    break;
            }
        }

        //configura base de dados
        arquivoConfiguracao = arquivoConfiguracao.replace("\t dir=\"textos\"\n", getExpressaoBaseDados(nomeBaseDados));

        //configura diretorio stoplist
        arquivoConfiguracao = arquivoConfiguracao.replace("\t\t<stoplist dir=\"stoplist\">\n",
                getExpressaoDiretorioStoplist(diretorioStopList));

        //configura stopfile
        arquivoConfiguracao = arquivoConfiguracao.replace("\t\t\t<stopfile>ingl.xml</stopfile>\n",
                getExpressaoStopfiles(stopFile));

        //configura n-gram
        arquivoConfiguracao = arquivoConfiguracao.replace("\t\t<quantidadeGram/>\n",
                getExpressaoNGram(gram));

        //configura detalhes ngram    
        arquivoConfiguracao = arquivoConfiguracao.replace("\t\t<definicaoGram/>", getExpressaoDetalhesNGram(parametros));

        try {

            //salva o arquivo
            printFile("config.xml", arquivoConfiguracao.toString());
        } catch (IOException ex) {
            Logger.getLogger(PretextExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String getExpressaoNGram(String gram) {
        if (!gram.contains("-")) {

            return "\t\t<gram n=\"" + gram + "\"/>\n";

        } else {
            String[] grams = gram.split("-");
            String expressao = "";
            for (String novaGram : grams) {
                expressao += "\t\t<gram n=\"" + novaGram + "\"/>\n";
            }
            //String primeiroGram = gram.substring(0, 1);
            //String segundoGram = gram.substring(1);

            //return "\t\t<gram n=\"" + primeiroGram + "\"/>\n"
//                    + "\t\t<gram n=\"" + segundoGram + "\"/>\n";
            return expressao;
        }

    }

    private String getExpressaoDetalhesNGram(List<Parametro> parametros) {

        String termoFrequencia = "";
        String minFile = "";
        String maxFile = "";
        String minFreq = "";
        String maxFreq = "";
        String gram = "";
        String corteDesvioPadrao = "";

        for (Parametro parametro : parametros) {
            switch (parametro.getNome()) {
                case "desvio":
                    corteDesvioPadrao = parametro.getValor();
                    break;
                case "cf":
                    termoFrequencia = parametro.getValor();
                    break;
                case "corteArquivo":
                    minFile = parametro.getValor().split("-")[0];
                    maxFile = parametro.getValor().split("-")[1];
                    break;
                case "corteFrequencia":
                    minFreq = parametro.getValor().split("-")[0];
                    maxFreq = parametro.getValor().split("-")[1];
                    break;
                case "gram":
                    gram = parametro.getValor();
                    break;
            }
        }

        String expressaoDetalhesNGram = "";
        if (!gram.contains("-")) {
            expressaoDetalhesNGram = "\t\t<gram n=\"" + gram + "\"\n";
            expressaoDetalhesNGram += getExpressaoTermoFrequencia(termoFrequencia);
            expressaoDetalhesNGram += getExpressaoCorteDesvioPadrao(corteDesvioPadrao);
            expressaoDetalhesNGram += getExpressaoCorteFrequencia(minFreq, maxFreq);
            expressaoDetalhesNGram += getExpressaoCorteArquivo(minFile, maxFile);
            expressaoDetalhesNGram += "\t\t/>\n";
        } else {
            String[] ngrams = gram.split("-");
            //String primeiroGram = gram.substring(0, 1);
            //String segundoGram = gram.substring(1);
            expressaoDetalhesNGram = "";
            for (String novoGram : ngrams) {
                expressaoDetalhesNGram += "\t\t<gram n=\"" + novoGram + "\"\n";
                expressaoDetalhesNGram += getExpressaoTermoFrequencia(termoFrequencia);
                expressaoDetalhesNGram += getExpressaoCorteDesvioPadrao(corteDesvioPadrao);
                expressaoDetalhesNGram += getExpressaoCorteFrequencia(minFreq, maxFreq);
                expressaoDetalhesNGram += getExpressaoCorteArquivo(minFile, maxFile);
                expressaoDetalhesNGram += "\t\t/>\n";
            }
//            expressaoDetalhesNGram = "\t\t<gram n=\"" + primeiroGram + "\"\n";
//            expressaoDetalhesNGram += getExpressaoTermoFrequencia(termoFrequencia);
//            expressaoDetalhesNGram += getExpressaoCorteDesvioPadrao(corteDesvioPadrao);
//            expressaoDetalhesNGram += getExpressaoCorteFrequencia(minFreq, maxFreq);
//            expressaoDetalhesNGram += getExpressaoCorteArquivo(minFile, maxFile);
//            expressaoDetalhesNGram += "\t\t/>\n";
//            expressaoDetalhesNGram += "\t\t<gram n=\"" + segundoGram + "\"\n";
//            expressaoDetalhesNGram += getExpressaoTermoFrequencia(termoFrequencia);
//            expressaoDetalhesNGram += getExpressaoCorteDesvioPadrao(corteDesvioPadrao);
//            expressaoDetalhesNGram += getExpressaoCorteFrequencia(minFreq, maxFreq);
//            expressaoDetalhesNGram += getExpressaoCorteArquivo(minFile, maxFile);
//            expressaoDetalhesNGram += "\t\t/>\n";
        }

        return expressaoDetalhesNGram;
    }

    private String getExpressaoCorteArquivo(String minFile, String maxFile) {
        String expressaoCorteArquivo = "";
        if (!minFile.equals("0")) {
            expressaoCorteArquivo += "\t\t\t minfiles=\"" + minFile + "\"\n";
        }
        if (!maxFile.equals("0")) {
            expressaoCorteArquivo += "\t\t\t maxfiles=\"" + maxFile + "\"\n";
        }
        return expressaoCorteArquivo;
    }

    private String getExpressaoCorteFrequencia(String minFreq, String maxFreq) {
        String expressaoCorteFrequencia = "";
        if (!minFreq.equals("0")) {
            expressaoCorteFrequencia += "\t\t\t min=\"" + minFreq + "\"\n";
        }
        if (!maxFreq.equals("0")) {
            expressaoCorteFrequencia += "\t\t\t max=\"" + maxFreq + "\"\n";
        }
        return expressaoCorteFrequencia;
    }

    private String getExpressaoCorteDesvioPadrao(String desvioPadrao) {
        String parametro = "";

        if (!desvioPadrao.equals("0")) {
            parametro += "\t\t\t std_dev=\"" + desvioPadrao + "\"\n";
        }
        return parametro;
    }

    private String getExpressaoTermoFrequencia(String termoFrequencia) {
        String parametro;
        parametro = "\t\t\t measure=\"" + termoFrequencia.replace("Smooth", "") + "\"\n";
        if (termoFrequencia.equals("tfidfSmooth") | termoFrequencia.equals("tflinearSmooth")) {
            parametro += "\t\t\t smooth=\"enabled\"\n";
        } else {
            parametro += "\t\t\t smooth=\"disabled\"\n";
        }
        return parametro;
    }

    private String getExpressaoBaseDados(String nomeBaseDados) {
        return "\tdir=\"" + nomeBaseDados + "\"\n";
    }

    private String getExpressaoDiretorioStoplist(String diretorioStopList) {
        return "\t\t<stoplist dir=\"" + diretorioStopList + "\">\n";
    }

    private String getExpressaoStopfiles(String stopFile) {
        if (!stopFile.contains("-")) {
            return "\t\t\t<stopfile>" + stopFile + "</stopfile>\n";
        } else {
//            System.out.println(stopFile + " *******************");
            String[] stopfiles = stopFile.split("-");
            String expressao = "";
            for (String sf : stopfiles) {
//                System.out.println(sf);
                expressao += "\t\t\t<stopfile>" + sf + "</stopfile>\n";
            }
            //String primeiroGram = gram.substring(0, 1);
            //String segundoGram = gram.substring(1);

            //return "\t\t<gram n=\"" + primeiroGram + "\"/>\n"
//                    + "\t\t<gram n=\"" + segundoGram + "\"/>\n";
            return expressao;
        }
    }

    private String criarModelo() {
        StringBuilder arquivoConfiguracao = new StringBuilder();
        arquivoConfiguracao.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        arquivoConfiguracao.append("<pretext\n");
        arquivoConfiguracao.append("\t lang=\"en\"\n");
        arquivoConfiguracao.append("\t dir=\"textos\"\n");
        arquivoConfiguracao.append("\t log=\"pretext.log\"\n");
        arquivoConfiguracao.append("\t silence=\"off\">\n\n");
        arquivoConfiguracao.append("\t<maid>\n");
        arquivoConfiguracao.append("\t\t<number/>\n");
        arquivoConfiguracao.append("\t\t<html/>\n");
        arquivoConfiguracao.append("\t\t<simbols/>\n");
        arquivoConfiguracao.append("\t\t<stoplist dir=\"stoplist\">\n");
        arquivoConfiguracao.append("\t\t\t<stopfile>ingl.xml</stopfile>\n");
        arquivoConfiguracao.append("\t\t</stoplist>\n");
        arquivoConfiguracao.append("\t\t<stemming dir=\"steminfo\"/>\n");
        arquivoConfiguracao.append("\t</maid>\n\n");
        arquivoConfiguracao.append("\t<ngram dir=\"ngraminfo\">\n");
        arquivoConfiguracao.append("\t\t<quantidadeGram/>\n");
        arquivoConfiguracao.append("\t</ngram>\n\n");
        arquivoConfiguracao.append("\t<report\n");
        arquivoConfiguracao.append("\t\t ngramdir=\"ngraminfo\"\n");
        arquivoConfiguracao.append("\t\t discover=\"discover\"\n");
        arquivoConfiguracao.append("\t\t graphics=\"graphics\"\n");
        arquivoConfiguracao.append("\t\t taxonomy=\"taxonomia.txt\"\n");
        arquivoConfiguracao.append("\t\t transpose=\"disabled\">\n\n");
        arquivoConfiguracao.append("\t\t<definicaoGram/>\n");
        arquivoConfiguracao.append("\t</report>\n");
        arquivoConfiguracao.append("</pretext>\n");

        return arquivoConfiguracao.toString();
    }

    private void converteArquivoConfiguracao(int nrGrams, int min, int minFiles, String stopfile, String textos) {

        try {
            String arquivoConfiguracao = lerArquivo("config.xml");
            //arquivoConfiguracao = arquivoConfiguracao.replaceAll("min=\"[0-9]+\"", "min=\"" + min + "\"");
            //arquivoConfiguracao = arquivoConfiguracao.replaceAll("gram n=\"[0-9]+\"", "gram n=\"" + nrGrams + "\"");
            //arquivoConfiguracao = arquivoConfiguracao.replaceAll("minfiles=\"[0-9]+\"", "minfiles=\"" + minFiles + "\"");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("<stopfile>[a-zA-Z]+.xml</stopfile>", "<stopfile>" + stopfile + ".xml</stopfile>");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("  dir=\"[0-9a-zA-Z]+\"", "  dir=\"" + textos + "\"");
            // System.out.println(arquivoConfiguracao);

            printFile("config.xml", arquivoConfiguracao);
        } catch (IOException ex) {
            System.out.println("Erro na leitura ou escrita");
            Logger.getLogger(PretextExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String buildArquivoConfiguracao(List<Parametro> parametros) {
        return "";
    }

}

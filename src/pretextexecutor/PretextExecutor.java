/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pretextexecutor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class PretextExecutor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       int min;
       int ngram;

        for (int i = 1; i < 8; i++) {
            min =  (i + 1);
            for (int j = 0; j < 1; j++) {
                ngram = (j + 1);
                converteArquivoConfiguracao(ngram, min);
                executaPrograma("perl Start.pl", "saida.txt", "erro.txt");
                String diretorio = System.getProperty("user.dir");
                diretorio += "/discover/";
                executaPrograma("java -jar " + diretorio + "PretextTOWeka.jar " + ngram + "gram" + min + ".arff " + diretorio, "saida2.txt", "erro2.txt");
                //System.out.println("java -jar " + diretorio + "PretextToWeka.jar " + ngram + "gram" + min + ".arff " + diretorio);
            }
        }
//        System.out.println("Executando o primeiro comando");
//        executaPrograma("java -jar LS.jar", "saida1.txt", "erro1.txt");
//        System.out.println("Executando o segundo comando");
//        String diretorio = System.getProperty("user.dir");
//        diretorio += "/Teste2/";
//        // executaPrograma("java -jar " + diretorio + "/Teste2/LS.jar", "saida2.txt", "erro2.txt");
//        System.out.println("java -jar " + diretorio + "LS.jar " + diretorio);
//        executaPrograma("java -jar " + diretorio + "LS.jar " + diretorio, "saida2.txt", "erro2.txt");
    }

    private static void converteArquivoConfiguracao(int nrGrams, int min) {

        try {
            String arquivoConfiguracao = lerArquivo("config.xml");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("min=\"[0-9]+\"", "min=\"" + min + "\"");
            // System.out.println(arquivoConfiguracao);
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("gram n=\"[0-9]+\"", "gram n=\"" + nrGrams + "\"");
            // System.out.println("***********************************");
            // System.out.println(arquivoConfiguracao);

            printFile("config.xml", arquivoConfiguracao);
        } catch (IOException ex) {
            System.out.println("Erro na leitura ou escrita");
            Logger.getLogger(PretextExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String lerArquivo(String filePath) throws FileNotFoundException, IOException {
        StringBuilder linha = new StringBuilder();
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha.append(br.readLine());
                linha.append("\n");
            }
            br.close();
            fr.close();
        }
        return linha.toString();
    }

    public static void printFile(String fileName, String texto) throws IOException {
        try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(texto);

            bw.newLine();

            bw.close();
            fw.close();
        }
    }

    public static void executaPrograma(String comando, String arquivoSaida, String arquivoErro) {
//                if (args.length < 1)
//        {
//            System.out.println("USAGE java GoodWinRedirect <outputfile>");
//            System.exit(1);
//        }

        try {
            //FileOutputStream fos = new FileOutputStream(args[0]);
            FileOutputStream fosError = new FileOutputStream(arquivoErro);
            FileOutputStream fosExit = new FileOutputStream(arquivoSaida);

            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(comando);
            //Process proc = rt.exec("java -cp weka.jar weka.classifiers.misc.HyperPipes -x 10 -t 50reuter4GRAM_tf.arff > teste.txt");
            //Process proc = rt.exec("java -cp weka.jar weka.classifiers.misc.HyperPipes -x 10 -t 50reuter4GRAM_tf.arff");
            // Process proc = rt.exec("perl Start.pl");
            // any error message?
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR", fosError);

            // any output?
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", fosExit);

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error???
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
            // fos.flush();
            // fos.close();        
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}

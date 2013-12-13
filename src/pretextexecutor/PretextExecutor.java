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
        int minFiles;
        int numeroArquivos;
        int numeroClasses;
        String nomeTeste;

        if (args.length < 1) {
            System.out.println("Falta passar parametros");
            // System.exit(1);
        }

        nomeTeste = args.length > 0 ? args[0] : "DefaultName";
        numeroArquivos = args.length > 1 ? Integer.valueOf(args[1]) : 20;
        numeroClasses = args.length > 2 ? Integer.valueOf(args[2]) : 2;
        System.out.println("Passei pelos argumentos");
        for (int i = 3; i < 7; i++) {
            minFiles = i * ((numeroArquivos / numeroClasses) / 10);
            for (min = 2; min < 8; min++) {
                for (ngram = 1; ngram < 2; ngram++) {
                    converteArquivoConfiguracao(ngram, min, minFiles);
                    executaPrograma("perl Start.pl", "saida.txt", "erro.txt");
                    String diretorio = System.getProperty("user.dir");
                    diretorio += "/discover/";
                    String comando = "java -jar " + diretorio + "PretextTOWeka.jar " + nomeTeste + ngram + "gram" + min + "min" + minFiles + "minfiles" + ".arff " + diretorio;
                    executaPrograma(comando, "log_Saida.txt", "log_Erro.txt");
                    //System.out.println(comando);
                }
            }
        }
    }

    private static void converteArquivoConfiguracao(int nrGrams, int min, int minFiles) {

        try {
            String arquivoConfiguracao = lerArquivo("config.xml");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("min=\"[0-9]+\"", "min=\"" + min + "\"");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("gram n=\"[0-9]+\"", "gram n=\"" + nrGrams + "\"");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("minfiles=\"[0-9]+\"", "minfiles=\"" + minFiles + "\"");
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
        } catch (IOException | InterruptedException t) {
            t.printStackTrace();
        }
    }

}

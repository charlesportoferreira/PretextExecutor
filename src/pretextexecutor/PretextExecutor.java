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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
        String stopFile = "";
        int min;
        int ngram=12;
        int minFiles;
        double numeroArquivos = 0.0;
        double numeroClasses = 0.0;
        String nomeTeste = "";

        List<String> argumentos = new ArrayList<>();
        argumentos.addAll(Arrays.asList(args));

        if (args.length == 8) {
            Iterator itr = argumentos.iterator();
            while (itr.hasNext()) {
                String argumento = (String) itr.next();
                switch (argumento) {
                    case "-h":
                        ajuda();
                        break;
                    case "-s":
                        stopFile = (String) itr.next();
                        break;
                    case "-a":
                        numeroArquivos = Double.valueOf((String) itr.next());
                        break;
                    case "-c":
                        numeroClasses = Double.valueOf((String) itr.next());
                        break;
                    case "-n":
                        nomeTeste = (String) itr.next();
                        break;
                    default:
                        System.out.println("Parametro incorreto: " + argumento);
                        ajuda();
                        System.exit(1);
                        break;
                }
            }
        } else {

            System.out.println("Faltou passar algum parametro");
            ajuda();
            System.exit(1);
        }

        // imprimeArgumentos(numeroArquivos, numeroClasses, nomeTeste,stopFile);
        for (double i = 1; i <= 5; i = i + 4) {
            //minFiles = (int) ((i / 100) * ((numeroArquivos / numeroClasses)));
            minFiles = (int)i;
            for (min = 5; min <= 40; min = min + 5) {
                for (ngram = 1; ngram <= 2; ngram++) {
                    converteArquivoConfiguracao(ngram, min, minFiles, stopFile);
                    executaPrograma("perl Start.pl", "saida.txt", "erro.txt");
                    String diretorio = System.getProperty("user.dir");
                    diretorio += "/discover/";
                    String nomeArquivo = nomeTeste + "-" + stopFile + "-" + ngram + "gram" + "-" + min + "min" + "-" + minFiles + "minfiles" + ".arff";
                    String comando = "java -jar " + diretorio + "PretextTOWeka.jar " + nomeArquivo + " " + diretorio;
                    executaPrograma(comando, "log_Saida.txt", "log_Erro.txt");
                    // System.out.println(comando);
                    // System.out.println(nomeArquivo);
                }
            }
        }
    }

    private static void converteArquivoConfiguracao(int nrGrams, int min, int minFiles, String stopfile) {

        try {
            String arquivoConfiguracao = lerArquivo("config.xml");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("min=\"[0-9]+\"", "min=\"" + min + "\"");
          //  arquivoConfiguracao = arquivoConfiguracao.replaceAll("gram n=\"[0-9]+\"", "gram n=\"" + nrGrams + "\"");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("minfiles=\"[0-9]+\"", "minfiles=\"" + minFiles + "\"");
            arquivoConfiguracao = arquivoConfiguracao.replaceAll("<stopfile>[a-zA-Z]+.xml</stopfile>", "<stopfile>" + stopfile + ".xml</stopfile>");
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

    private static void ajuda() {
        System.out.println("-h = help");
        System.out.println("Os tres proximos parametros sao obrigatorios");
        System.out.println("-a = numero de arquivos");
        System.out.println("-c = numero de classes");
        System.out.println("-n = nome do arquivo");
        System.out.println("-s = stopFile");

    }

    private static void imprimeArgumentos(double primeiro, double segundo, String terceiro, String quarto) {
        System.out.println("numero de arquivos: " + primeiro);
        System.out.println("numero de classes: " + segundo);
        System.out.println("nome do arquivo: " + terceiro);
        System.out.println("stopfile: " + quarto);
        System.exit(0);
    }

}

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
import view.Configuracao;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class PretextExecutor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean isIntefaceGraficaAtiva = false;
        String diretorioStopList;
        String[] stopFiles = null;
        String[] ngrams = null;
        String[] cortesFrequencia = null;
        String[] cortesArquivos = null;
        String[] calculoFrequencia = null;
        String[] corteDesvioPadrao = null;

        String nomeBaseDados = "";

        List<String> argumentos = new ArrayList<>(Arrays.asList(args));
        List<Parametro> parametros = new ArrayList<>();

        //if (args.length == 8) {
        Iterator itr = argumentos.iterator();
        while (itr.hasNext()) {
            String argumento = (String) itr.next();
            switch (argumento) {
                case "-h":
                    ajuda();
                    break;
                case "-sl":
                    diretorioStopList = (String) itr.next();
                    parametros.add(new Parametro("diretorioStopList", diretorioStopList));
                    break;
                case "-sf":
                    stopFiles = ((String) itr.next()).split(",");
                    break;
                case "-b":
                    nomeBaseDados = (String) itr.next();
                    parametros.add(new Parametro("nomeBaseDados", nomeBaseDados));
                    break;
                case "-g":
                    ngrams = ((String) itr.next()).split(",");
                    break;
                case "-mf":
                    cortesFrequencia = ((String) itr.next()).split(",");
                    break;
                case "-ma":
                    cortesArquivos = ((String) itr.next()).split(",");
                    break;
                case "-f":
                    calculoFrequencia = ((String) itr.next()).split(",");
                    break;
                case "-v":
                    callConfigurator();
                    isIntefaceGraficaAtiva = true;
                    //System.exit(0);
                    break;

                case "-d":
                    corteDesvioPadrao = ((String) itr.next()).split(",");
                    break;
                default:
                    System.out.println("Parametro incorreto: " + argumento);
                    ajuda();
                    System.exit(1);
                    break;
            }
            // }
            //} else {

//            System.out.println("Faltou passar algum parametro");
//            ajuda();
//            System.exit(1);
        }

        if (!isIntefaceGraficaAtiva) {
            for (String desvio : corteDesvioPadrao) {
                parametros.add(new Parametro("desvio", desvio));
                for (String cf : calculoFrequencia) {
                    parametros.add(new Parametro("cf", cf));
                    // imprimeParametros(calculoFrequencia);
                    for (String corteArquivo : cortesArquivos) {
                        parametros.add(new Parametro("corteArquivo", corteArquivo));
                        //imprimeParametros(cortesArquivos);
                        for (String corteFrequencia : cortesFrequencia) {
                            parametros.add(new Parametro("corteFrequencia", corteFrequencia));
                            // imprimeParametros(cortesFrequencia);
                            for (String stopfile : stopFiles) {
                                parametros.add(new Parametro("stopFile", stopfile));
                                //imprimeParametros(stopFiles);
                                for (String gram : ngrams) {
                                    parametros.add(new Parametro("gram", gram));
                                    //imprimeParametros(ngrams);
                                    //converteArquivoConfiguracao(ngram, min, minFiles, stoplist, nomeBaseDados);
                                    ArquivoConfiguracao ac = new ArquivoConfiguracao(parametros);
                                    //System.exit(0);
                                    executaPrograma("perl Start.pl", "saida.txt", "erro.txt");
                                    String diretorio = System.getProperty("user.dir");
                                    diretorio += "/discover/";
                                    String nomeArquivo = nomeBaseDados + "|" + stopfile + "|" + gram + "gram" + "|" + corteFrequencia + "minFreq" + "|" + desvio + "desvio" + "|" + corteArquivo + "minfiles" + "|" + cf + ".arff";
                                    String comando = "java -jar " + diretorio + "PretextTOWeka.jar " + nomeArquivo + " " + diretorio;
                                    executaPrograma(comando, "log_Saida.txt", "log_Erro.txt");
                                    // System.out.println(comando);
                                    // System.out.println(nomeArquivo);
                                }
                            }
                        }
                    }
                }
            }
            SendMail sendMail = new SendMail();
            sendMail.sendMail("charlesportoferreira@gmail.com", "charlesportoferreira@gmail.com", "PreText finalizado", "Criação de tabelas de atributo valor prontas");
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

    public static void callConfigurator() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Configuracao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                new Configuracao().setVisible(true);

            }

        });
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

    private static void imprimeParametros(String[] Parametros) {
        for (String parametro : Parametros) {
            System.out.println(parametro);
        }
    }

    private static void imprimeArgumentos(double primeiro, double segundo, String terceiro, String quarto) {
        System.out.println("numero de arquivos: " + primeiro);
        System.out.println("numero de classes: " + segundo);
        System.out.println("nome do arquivo: " + terceiro);
        System.out.println("stopfile: " + quarto);
        System.exit(0);
    }

}

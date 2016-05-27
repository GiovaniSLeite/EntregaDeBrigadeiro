package funcoes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*Classe que roda o algoritmo e muda os parametros, tudo isso pelo prompt*/
public class Parametrizacao {

    public static void main(String[] args) {

        /*Nome da funcao a ser executada*/
        String nomeDaFuncao = "Routes";
        
        String[] param0 = {"A-n32-k5.vrp"};//0 - nome do arquivo teste
        String[] param1 = {"1", "2", "3"};//1 - ID
        String[] param2 = {"500", "1000", "10000"};//2 - Numero de individuos
        String[] param3 = {"0", "1"};//3 - criterio de parada
        String[] param4 = {"100"};//4 - numero de geracoes
        String[] param5 = {"1", "1.5"};//5 - numero de crossovers
        String[] param6 = {"0", "1"};//6 - tipo do crossover
        String[] param7 = {"0.7", "0.9", "1"};//7 - probabilidade do crossover
        String[] param8 = {"1"};//8 - tipo da mutacao
        String[] param9 = {"0.05", "0.1"};//9 - probabilidade da mutacao
        String[] param10 = {"0.5"};//10 - prob especial
        String[] param11 = {"1"};//11 - criterio de troca
        String[] param12 = {"true"};//12 - elitismo
        String[] param13 = {"0"};//13 - prob correcao
        String[] param14 = {"0"};//14 - prob reparação (caso de capacidade)
        String[] param15 = {"0"};//15 - prob otimizacao
        String[] param16 = {"10"};//16 - intervalo de impressao;
        
        //Diretorio onde estão os .class das funcoes (IDE: pasta bin - Editor de texto: mesma pasta dos .java)
        File Diretorio = new File("C:\\Users\\sousa\\OneDrive\\Documentos\\NetBeansProjects\\EP1IA_2\\build\\classes");
        ArrayList<String> comandos = new ArrayList();

        //Gerar todas as combinacoes de testes
        for(String p0 : param0)
            for(String p1 : param1)
                for(String p2 : param2)
                    for(String p3 : param3)
                        for(String p4 : param4)
                            for(String p5 : param5)
                                for(String p6 : param6)
                                    for(String p7 : param7)
                                        for(String p8 : param8)
                                            for(String p9 : param9)
                                                for(String p10 : param10)
                                                    for(String p11 : param11)
                                                        for(String p12 : param12)
                                                            for(String p13 : param13)
                                                                for(String p14 : param14)
                                                                    for(String p15 : param15)
                                                                        for(String p16 : param16)
                                                                            comandos.add("java funcoes." + nomeDaFuncao + " " + p0 + " " + p1 +" "+ p2 +" "+p3 +" "+p4 +" "+p5 +" "+p6 +" "+p7 +" "+p8 +" "+p9 +" "+p10 +" "+p11 +" "+p12 +" "+p13 +" "+p14 +" "+p15 +" "+p16);

        //Comando que eh executado no prompt
        int execucao;
        for (execucao = 0; execucao < comandos.size(); execucao++) {
            try {
                String cmd = comandos.get(execucao);
                System.out.println("EXECUCAO " + execucao + "\n" + cmd);
                run(cmd, Diretorio); //Executa o comando
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected static void run(String command, File path) {
        try {

            //Instancia um objeto Process, classe que realiza comandos no prompt
            Process proc = Runtime.getRuntime().exec(command, null, path);

            //Cria os leitores para poder imprimir na tela o que esta sendo executado no prompt
            BufferedReader stdPrint = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String tmp;

            while ((tmp = stdPrint.readLine()) != null) {
                System.out.println(" > " + tmp);  //Vai imprimindo na tela o que seria impresso no prompt           	 		           	
            }
            while ((tmp = stdError.readLine()) != null) {
                System.out.println(command + " > " + tmp); //Se der erro, indica qual comando foi e qual erro que ocorreu            	
            }
            proc.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

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
        
        String[] param0 = {"A-n32-k5.vrp"};//0 - nome do arquivo teste 32 5
        String[] param1 = {"1", "2", "3"};//1 - ID
        String[] param2 = {"100", "1000", "10000"};//2 - Numero de individuos 10000
        String[] param3 = {"0"};//3 - criterio de parada 0
        String[] param4 = {"100"};//4 - numero de geracoes
        String[] param5 = {"1", "1.5"};//5 - numero de crossovers 1
        String[] param6 = {"0", "1"};//6 - tipo do crossover 1
        String[] param7 = {"0.9", "1.0"};//7 - probabilidade do crossover 1
        String[] param8 = {"1"};//8 - tipo da mutacao 1
        String[] param9 = {"0.05"};//9 - probabilidade da mutacao 0.05
        String[] param10 = {"0.5"};//10 - prob especial mutacao
        String[] param11 = {"0", "1"};//11 - criterio de troca 0
        String[] param12 = {"true", "false"};//12 - elitismo 
        String[] param13 = {"1"};//13 - prob correcao 1
        String[] param14 = {"0.7", "0.9", "1"}; //14 probEspecial para correcao
        String[] param15 = {"0.5", "0.75", "1"};//15 - prob reparação (caso de capacidade) 1
        String[] param16 = {"0.5", "0.75", "1"};//16 - prob otimizacao
        String[] param17 = {"1000"};//17 - intervalo de impressao;
        
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
                                                            if(p11.equalsIgnoreCase("1")){
                                                                for(String p13 : param13)
                                                                    for(String p14 : param14)
                                                                        for(String p15 : param15)
                                                                            for(String p16 : param16)
                                                                                for(String p17 : param17)
                                                                                    comandos.add("java funcoes." + nomeDaFuncao + " " + p0 + " " + p1 +" "+ p2 +" "+p3 +" "+p4 +" "+p5 +" "+p6 +" "+p7 +" "+p8 +" "+p9 +" "+p10 +" "+p11 +" false "+p13 +" "+p14 +" "+p15 +" "+p16+" "+p17);
                                                        }else
                                                            for(String p12 : param12)
                                                                for(String p13 : param13)
                                                                    for(String p14 : param14)
                                                                        for(String p15 : param15)
                                                                            for(String p16 : param16)
                                                                                for(String p17 : param17)
                                                                                    comandos.add("java funcoes." + nomeDaFuncao + " " + p0 + " " + p1 +" "+ p2 +" "+p3 +" "+p4 +" "+p5 +" "+p6 +" "+p7 +" "+p8 +" "+p9 +" "+p10 +" "+p11 +" "+p12 +" "+p13 +" "+p14 +" "+p15 +" "+p16+" "+p17);
                                                                
        //Comando que eh executado no prompt
        int execucao;
        System.out.println(comandos.size());
        for (execucao = 0; execucao <= comandos.size(); execucao++) {
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

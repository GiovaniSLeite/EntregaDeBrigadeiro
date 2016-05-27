
package funcoes;

import Operadores.*;
import algoritmoGenetico.*;
import java.util.ArrayList;

public class Routes extends AlgoritmosGeneticos {
    private static final int DEPOSITO = 0;
    
    public Routes(int qRotas, ArrayList<Cliente> aux)
    {
        this.qtdRotas = qRotas;
        this.clientes = new ArrayList();
        clientes.addAll(aux);
        this.qtdClientes = this.clientes.size();
        this.calcularDistancias();
        
    }
    public static void main(String[] args)
    {
       
        /*args
        0 - nome do arquivo teste
        1 - ID
        2 - Numero de individuos
        3 - criterio de parada
        4 - numero de geracoes
        5 - numero de crossovers
        6 - tipo do crossover
        7 - probabilidade do crossover
        8 - tipo da mutacao
        9 - probabilidade da mutacao
        10 - prob especial
        11 - criterio de troca
        12 - elitismo
        13 - prob correcao
        14 - prob reparação (caso de capacidade)
        15 - prob otimizacao
        16 - intervalo de impressao
        */
        String arquivoTeste = "C:\\Users\\sousa\\OneDrive\\Documentos\\NetBeansProjects\\EP1IA_2\\A-VRP\\"+args[0];
        int qRotas = Utils.getQtdRotas(arquivoTeste);
        ArrayList<Cliente> aux = Utils.getClientes(arquivoTeste);
        Routes r = new Routes(qRotas, aux);
        r.ID = Integer.parseInt(args[1]);
        r.capacidade = Utils.getCapacity(arquivoTeste);
        r.numIndividuos = Integer.parseInt(args[2]);
        r.critParada = Integer.parseInt(args[3]);
        r.numGeracoes = Integer.parseInt(args[4]);
        r.numCross = Integer.parseInt(args[5])*r.numIndividuos;
        r.crossover = args[6].equals("0") ? new CrossoverUmPonto() : args[6].equals("1") ? new CrossoverDoisPontos() : args[6].equals("2") ? new CrossoverSimpleRandom(r.mapaDistancias) : new CrossoverOXadaptado();
        r.probCrossover = Double.parseDouble(args[7]);
        r.mutacao = args[8].equals("0") ? new MutacaoSimples() : args[8].equals("1") ? new MutacaoTroca() : new MutationSimpleRandom(r.mapaDistancias, Double.parseDouble(args[10]));
        r.probMutacao = Double.parseDouble(args[9]);
        r.critTroca = Integer.parseInt(args[11]);
        r.elitismo = args[12].equals("true");
        r.corretor = new Correcao(r.mapaDistancias);
        r.probCorrecao = Double.parseDouble(args[13]);
        r.reparador = new RepairingOperator();
        r.probReparacao = Double.parseDouble(args[14]);
        r.otimizador = new Otimizacao(r.mapaDistancias);
        r.probOtimizacao = Double.parseDouble(args[15]);
        r.intervaloImpressao = Integer.parseInt(args[16]);
        
        r.evolucao();
        
    }
    
    protected double fitness(int[] genotipo)
    {
    	double distanciaPercorrida = 0;
    	int anterior = -1;
    	int atual = 0;
    	
    	for(int i = 0; i < genotipo.length; i++)
    	{
    		if(genotipo[i] == -1)
    		{
    			if(anterior!=-1)
    			{
    				distanciaPercorrida += this.mapaDistancias[anterior][DEPOSITO];
    				anterior = -1;
    			}
    		}
    		else
    		{
    			atual = genotipo[i]-1;
                        
    			if(anterior==-1)
    			{
    				distanciaPercorrida += this.mapaDistancias[DEPOSITO][atual];
    				anterior = atual;
    			}
    			else
    			{
                                    distanciaPercorrida += this.mapaDistancias[anterior][atual];
                                    anterior = atual;       
    			}
    		}
    	}
    	
    	if (genotipo[genotipo.length-1] != -1 && anterior != -1)
    		distanciaPercorrida += this.mapaDistancias[anterior][DEPOSITO];
    	//System.out.println("PENANDO: " + ((double)this.indGeracao/this.IT)*this.alpha + " "+ penalizar(genotipo));
    	return distanciaPercorrida+ ((double)this.indGeracao/this.IT)*this.alpha*penalizar(genotipo);
    }
    
    public int penalizar(int [] genotipo)
    {
        ArrayList<ArrayList<Integer>> rotas = Utils.getRotas(genotipo);
        
        int penalizacao = 0;
        for(ArrayList<Integer> clientesAtendidos : rotas)
        {
            int penalizacaoLocal = -capacidade;
            for(Integer cliAtendido : clientesAtendidos)
                penalizacaoLocal+= clientes.get(cliAtendido-1).demanda;
            if(penalizacaoLocal>0)
                penalizacao+=penalizacaoLocal;
        }
        
        return penalizacao;
        
    }
    
    public void calcAlpha()
    {
        double mnv = (double) Utils.demandaTotal(clientes)/capacidade;
        this.alpha = this.getBetter().fitness/((1.0/IT)*Math.pow(mnv/2 * capacidade, 2));
    }
}

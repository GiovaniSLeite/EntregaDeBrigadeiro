
package funcoes;

import Operadores.*;
import algoritmoGenetico.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Routes extends AlgoritmosGeneticos {
	
	private static final int DEPOSITO = 0;
	double pesoPenalizacao = 10;
	
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
    	//Parâmetro 1 -> Caminho do arquivo de mapa
    	String localArq = args[0];
    	int qRotas = Utils.getQtdRotas(localArq);
    	ArrayList<Cliente> aux = Utils.getClientes(localArq);
    	
    	Routes r = new Routes(qRotas, aux);
    	
    	//Parametro 2 -> Numero de Individuos
    	r.numIndividuos = Integer.valueOf(args[1]);
    	
    	//Parametro 3 -> Criterio de parada: 0 (Convergencia) 1 (Num Geracoes)
    	r.critParada = Integer.valueOf(args[2]);
    	
    	//Parametro 4 -> Numero de geracoes (utilizado somente se o par. 3 for 1)
    	r.numGeracoes = Integer.valueOf(args[3]);
    	
    	//Parametro 5 -> Numero de tentativas de crossover
    	r.numCross = Integer.valueOf(args[4]);
    	
    	//Parametro 6 -> Tipo de Crossover (0 -> Um ponto; 1 -> Dois pontos; 2 -> Simple Random; 3 -> OX Adaptado)
    	int param = Integer.valueOf(args[5]);
    	switch(param)
    	{
    	case 0:
    		r.crossover = new CrossoverUmPonto();
    		break;
    	case 1:
    		r.crossover = new CrossoverDoisPontos();
    		break;
    	case 2:
    		r.crossover = new CrossoverSimpleRandom(r.mapaDistancias);
    		break;
    	case 3:
    		r.crossover = new CrossoverOXadaptado();
    		break;
    	}
    	
    	//Parametro 7 -> Probabilidade de ocorrer crossover
    	r.probCrossover = Double.valueOf(args[6]);
    	
    	//Por uma questao de dependencia, os parametros 9 e 10 serao pegos antes do parametro 8
    	
    	//Parametro 9 -> Probabilidade de ocorrencia de mutacao
    	r.probMutacao = Double.valueOf(args[8]);
    	
    	//Parametro 10 -> Probabilidade especial de inserir no melhor local, caso seja mutacao dirigida
    	r.probEspecial = Double.valueOf(args[9]);
    	
    	//Parametro 8 -> Tipo de Mutacao (0 -> Troca | 1 -> MutationSimpleRandom)
    	param = Integer.valueOf(args[7]);
    	switch(param)
    	{
    	case 0: 
    		r.mutacao = new MutacaoTroca();
    		break;
    	case 1:
    		r.mutacao = new MutationSimpleRandom(r.mapaDistancias, r.probEspecial);
    		break;
    	}
    	
    	//Parametro 11 -> Criterio de Troca (0 -> Com substituicao | 1 -> Sem Substituicao)
    	r.critTroca = Integer.valueOf(args[10]);
    	
    	//Parametro 12 -> Elitismo (0 -> S/ Elitismo | 1 -> C/ Elitismo)
    	r.elitismo = Integer.valueOf(args[11]) == 0 ? false : true;
    	
    	//Parametro 13 -> Utilizacao de Operador de suporte
    	r.suporte = Integer.valueOf(args[12]) == 0 ? false : true;
    	
    	//Parametro 14 -> Utilizacao de Operador de correcao
    	r.correcao = Integer.valueOf(args[13]) == 0 ? false : true;
    	
    	//Parametro 15 -> Capacidade de cada carro
        r.capacidade = Integer.valueOf(args[14]);

        //Mandar evoluir
        r.evolucao();
        
    }
    
    //Função fitness da rota
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
    	
    	return distanciaPercorrida+this.pesoPenalizacao*penalizar(genotipo);
    }
    
    //Metodo de penalizacao
    public int penalizar(int [] genotipo)
    {
        ArrayList<ArrayList<Integer>> rotas = Utils.getRotas(genotipo);
        int penalizacao = 0;
        for(ArrayList<Integer> aux : rotas)
        {
            int penalizacaoLocal = -capacidade;
            for(Integer demanda : aux)
                penalizacaoLocal+= clientes.get(demanda-1).demanda;
            if(penalizacaoLocal>0)
                penalizacao+=penalizacaoLocal;   
        }
        
        return penalizacao;
        
    }
    
}

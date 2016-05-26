
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
       
        int qRotas = Utils.getQtdRotas("C:\\Users\\sousa\\OneDrive\\Documentos\\NetBeansProjects\\EP1IA_2\\A-n32-k5.vrp");
        ArrayList<Cliente> aux = Utils.getClientes("C:\\Users\\sousa\\OneDrive\\Documentos\\NetBeansProjects\\EP1IA_2\\A-n32-k5.vrp");
        Routes r = new Routes(qRotas, aux);
        r.capacidade = 100;
        r.numIndividuos = 10000;
        r.critParada = 0;
        r.numGeracoes = 30;
        r.numCross = r.numIndividuos;
        r.crossover = new CrossoverOXadaptado();
        r.probCrossover = 0.9;
        r.mutacao = new MutationSimpleRandom(r.mapaDistancias, r.qtdRotas);
        r.probMutacao = 0.2;
        r.critTroca = 1;
        r.elitismo = false;
        r.intervaloImpressao = 5;
        //PARA VER MATRIZ DE DISTANCIA P/ EXCEL
        //for(double[] linhas : r.mapaDistancias){
            //for(double abc : linhas)
            //    System.out.print(abc+"\t");
          //  System.out.println();
        //}
        //PARA VER X E Y DE CLIENTES PARA EXCEL
        //System.out.println("\n******************\n");
        //for(Cliente s : r.clientes)
        //    System.out.println(s);
//        System.out.println(r.clientes);
        r.evolucao();
        System.out.println(r.fitness(new int[]{22,32,20,18,14,8,27, -1, 13, 2, 17, 31, -1, 28, 25, -1, 30, 19, 9, 10, 23, 16, 11, 26, 6, 21, -1, 15, 29, 12, 5, 24, 4, 3 ,7}));
        //System.out.println(r.penalizar(new int[]{9, 12, 5, 29, 24, 3, 4, 7, -1, 28, 25, -1, 18, 20, 32, 22, 14, 8, 27, -1, 13, 2, 17, 31, -1, 21, 6, 26, 11, 30, 16, 23, 10, 19, 15}));
//        System.out.println(r.fitness(new int[]{21, 6, 26, 11, 30, 16, 23, 10, 19, 9, 12, 5, 29, 24, 3, 4, 7, -1, -1, 8, 14, 18, 20, 32, 22, 2, -1, -1, 31, 13, 17, 27, 15, 25, 28}));
        
        
        //Modelo de brinquedo
//        r.qtdClientes = 10;
//        r.qtdRotas = 3;
//        
//        r.clientes.clear();
//        r.clientes.add(new Cliente(0,9,0));
//        r.clientes.add(new Cliente(1,8,2));
//        r.clientes.add(new Cliente(2,7,18));
//        r.clientes.add(new Cliente(3,6,4));
//        r.clientes.add(new Cliente(4,5,16));
//        r.clientes.add(new Cliente(5,4,6));
//        r.clientes.add(new Cliente(6,3,14));
//        r.clientes.add(new Cliente(7,2,8));
//        r.clientes.add(new Cliente(8,1,12));
//        r.clientes.add(new Cliente(9,0,10));
//        
//        r.capacidade = 30;
//        
//        r.calcularDistancias();
//        
//        int[] cromossomo1 = Individuo.cromossomoAleatorio(r.qtdRotas, r.qtdClientes);
//        int[] cromossomo2 = Individuo.cromossomoAleatorio(r.qtdRotas, r.qtdClientes);
//        
//        System.out.println(Arrays.toString(cromossomo1));
//        System.out.println(Arrays.toString(cromossomo2));
//        
//        System.out.println();
//        
//        int[][] retorno = r.crossover.executar(cromossomo1, cromossomo2);
//        System.out.println(Arrays.toString(retorno[0]));
//        System.out.println(Arrays.toString(retorno[1]));
        
        //int[] cromossomo = {2, 4, 7, -1, 2, -1, 5, 8, 10, 9, -1};
        //System.out.println(Arrays.toString(cromossomo));
        //System.out.println();
        //Correcao c = new Correcao(r.mapaDistancias);
        
        //System.out.println(Arrays.toString(c.executar(cromossomo, r.qtdClientes, r.qtdRotas)));
        //for(int i = 0; i<r.qtdClientes; i++)
        //System.out.println(Arrays.toString(r.mapaDistancias[i]));
        
        //System.out.println();
        
        //System.out.println(r.fitness(cromossomo));
        //System.out.println(r.fitnessAntigo(cromossomo));
        //System.out.println(r.fitness(new int[]{22, 32, 20, 18, 14, 8, 27, -1, 13, 2, 17, 31, -1, 28, 25, -1, 30, 19, 9, 10, 23, 16, 11, 26, 6, 21, -1, 15, 29, 12, 5, 24, 4, 3, 7}));
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

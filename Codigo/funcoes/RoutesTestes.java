
package funcoes;

import Operadores.*;
import algoritmoGenetico.*;
import java.util.ArrayList;

public class RoutesTestes extends AlgoritmosGeneticos {
	
	private static final int DEPOSITO = 0;
        
    public RoutesTestes(int qRotas, ArrayList<Cliente> aux)
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
        RoutesTestes r = new RoutesTestes(qRotas, aux);
        r.capacidade = 100;
        r.numIndividuos = 1000;
        r.critParada = 0;
        r.numGeracoes = 30;
        r.numCross = (int)(10*r.numIndividuos);
        r.crossover = new CrossoverOXadaptado();
        r.probCrossover = 0.9;
        r.mutacao = new MutacaoTroca();
        r.probMutacao = 0.05;
        r.critTroca = 0;
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
        
        //r.evolucao();
        
        
        
        
        //Modelo de brinquedo
//        r.qtdClientes = 5;
//        r.qtdRotas = 3;
//        
//        r.clientes.clear();
//        r.clientes.add(new Cliente(0,0,0));
//        r.clientes.add(new Cliente(3,3,25));
//        r.clientes.add(new Cliente(1,5,25));
//        r.clientes.add(new Cliente(6,2,8));
//        r.clientes.add(new Cliente(2,4,4));
//        
//        
//        r.capacidade = 40;
//        
        r.calcularDistancias();
        
        System.out.println(r.fitness(new int[]{22,32,20,18,14,8,27, -1, 13, 2, 17, 31, -1, 28, 25, -1, 30, 19, 9, 10, 23, 16, 11, 26, 6, 21, -1, 15, 29, 12, 5, 24, 4, 3 ,7}));
        //System.out.println(r.fitness(new int[]{22, 32, 20, 18, 14, 8, 27, -1, 13, 2, 17, 31, -1, 28, 25, -1, 30, 19, 9, 10, 23, 16, 11, 26, 6, 21, -1, 15, 29, 12, 5, 24, 4, 3, 7}))
//        for(double[] kk : r.mapaDistancias)
//            System.out.println(Arrays.toString(kk));
//        
////        int[] cromossomo1 = Individuo.cromossomoAleatorio(r.qtdRotas, r.qtdClientes);
////        int[] cromossomo2 = Individuo.cromossomoAleatorio(r.qtdRotas, r.qtdClientes);
////        
////        System.out.println(Arrays.toString(cromossomo1));
////        System.out.println(Arrays.toString(cromossomo2));
////        
////        System.out.println();
////        
////        int[][] retorno = r.crossover.executar(cromossomo1, cromossomo2);
////        System.out.println(Arrays.toString(retorno[0]));
////        System.out.println(Arrays.toString(retorno[1]));
//        
//        int[] cromossomo = {3, 2, -1, 4, -1, 5};
//        RepairingOperator abcd = new RepairingOperator();
//        cromossomo = abcd.executar(cromossomo, r.clientes, r.capacidade);
//        System.out.println(Arrays.toString(cromossomo));
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
    	
    	return distanciaPercorrida+penalizar(genotipo);
    }
    
    public double fitnessAntigo(int[] genotipo) {
        double distanciaPercorrida = 0;
        Cliente depot = this.clientes.get(0);
        
        Cliente anterior = null;
        
        for(int i = 1; i < genotipo.length; i++){
            if(genotipo[i] == -1){
                if(anterior!=null){
                    distanciaPercorrida+= anterior.distancia(depot);
                    anterior = null;
                }
            }
            else
            {
                Cliente atual = clientes.get(genotipo[i]-1);
                if(anterior == null)
                {
                    distanciaPercorrida+= atual.distancia(depot);
                    anterior = atual;
                    
                }else
                {
                    distanciaPercorrida+= atual.distancia(anterior);
                    anterior = atual;
                    
                }
            }
        }
        
        if(genotipo[genotipo.length-1] != -1 && anterior != null) distanciaPercorrida += anterior.distancia(depot);
        
        return distanciaPercorrida+penalizar(genotipo);
    }
    
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

    public void calcAlpha()
    {
        double mnv = (double) Utils.demandaTotal(clientes)/capacidade;
        this.alpha = fitness(this.getBetter().genotipo)/((1/IT)*Math.pow(mnv/2 * capacidade, 2));
    }
    
}
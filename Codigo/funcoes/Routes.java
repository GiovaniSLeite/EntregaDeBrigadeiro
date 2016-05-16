/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funcoes;

import Operadores.*;
import algoritmoGenetico.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author sousa
 */
public class Routes extends AlgoritmosGeneticos {
	
	private static final int DEPOSITO = 0;
    
    public static void main(String[] args)
    {
        Routes r = new Routes();
        /*
        r.qtdRotas = Integer.valueOf(args[0]);
        r.qtdClientes = Integer.valueOf(args[1]);
        r.critParada = Integer.valueOf(args[2]);
        r.numGeracoes = Integer.valueOf(args[3]);
        r.numCross = (int)(Double.valueOf(args[4])*r.numIndividuos);
        r.crossover = Integer.valueOf(args[5]) == 0? new CrossoverUmPonto() : new CrossoverDoisPontos();
        r.probCrossover = Double.valueOf(args[6]);
        r.mutacao = Integer.valueOf(args[7]) == 0 ? new MutacaoSimples() : new MutacaoTroca();
        r.probMutacao = Double.valueOf(args[8]);
        r.critTroca = Integer.valueOf(args[9]);
        r.elitismo = Boolean.valueOf(args[10]);
        r.intervaloImpressao = Integer.parseInt(args[11]);*/
        r.qtdRotas = Utils.getQtdRotas("C:\\Users\\ctcca\\Documents\\USP\\5_Semestre\\IA\\Trabalhos\\EP_AlgoritmosGeneticos_Parte2\\EntregaDeBrigadeiro\\bin\\funcoes\\A-n33-k5.vrp");
        r.clientes = Utils.getClientes("C:\\Users\\ctcca\\Documents\\USP\\5_Semestre\\IA\\Trabalhos\\EP_AlgoritmosGeneticos_Parte2\\EntregaDeBrigadeiro\\bin\\funcoes\\A-n33-k5.vrp");
        r.qtdClientes = r.clientes.size();
        r.capacidade = 100;
        r.numIndividuos = 20;
        r.critParada = 0;
        r.numGeracoes = 30;
        r.numCross = (int)(1*r.numIndividuos);
        r.crossover = new CrossoverUmPonto();
        r.probCrossover = 0.9;
        r.mutacao = new MutacaoTroca();
        r.probMutacao = 0.05;
        r.critTroca = 0;
        r.elitismo = false;
        r.intervaloImpressao = 5;
        
        r.evolucao();
        
        //Modelo de brinquedo
        //r.qtdClientes = 10;
        //r.qtdRotas = 3;
        
        //r.clientes.clear();
        //r.clientes.add(new Cliente(0,9,0));
        //r.clientes.add(new Cliente(1,8,2));
        //r.clientes.add(new Cliente(2,7,18));
        //r.clientes.add(new Cliente(3,6,4));
        //r.clientes.add(new Cliente(4,5,16));
        //r.clientes.add(new Cliente(5,4,6));
        //r.clientes.add(new Cliente(6,3,14));
        //r.clientes.add(new Cliente(7,2,8));
        //r.clientes.add(new Cliente(8,1,12));
        //r.clientes.add(new Cliente(9,0,10));
        
        //r.capacidade = 30;
        
        //r.calcularDistancias();
        //int[] cromossomo = Individuo.cromossomoAleatorio(r.qtdRotas, r.qtdClientes);
        
        //int[] cromossomo = {2, 4, 7, -1, 3, 6, 5, 8, 10, 9, -1};
        //System.out.println(Arrays.toString(cromossomo));
        //System.out.println();
        //for(int i = 0; i<r.qtdClientes; i++)
        //System.out.println(Arrays.toString(r.mapaDistancias[i]));
        
        //System.out.println();
        
        //System.out.println(r.fitness(cromossomo));
        //System.out.println(r.fitnessAntigo(cromossomo));
    }
    
    protected double fitness(int[] genotipo)
    {
    	double distanciaPercorrida = 0;
    	int anterior = -1;
    	int atual = 0;
    	
    	for(int i = 1; i < genotipo.length; i++)
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
    	
    	return distanciaPercorrida;
    }
    
    @Override
    protected double fitnessAntigo(int[] genotipo) {
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
        
        return distanciaPercorrida;
    }
    
}

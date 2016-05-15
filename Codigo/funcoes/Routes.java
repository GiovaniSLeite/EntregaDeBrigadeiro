/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funcoes;

import Operadores.*;
import algoritmoGenetico.*;
import java.util.ArrayList;

/**
 *
 * @author sousa
 */
public class Routes extends AlgoritmosGeneticos {
	
	public double[][] mapaDistancias;
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
        r.qtdRotas = Utils.getQtdRotas("A-n32-k5.vrp");
        r.clientes = Utils.getClientes("A-n32-k5.vrp");
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
    
    //Criar um mapa de distâncias para evitar de ficar calculando as 
    //distâncias toda hora
    public void calcularDistancias()
    {
    	int i, j;
    	this.mapaDistancias = new double[this.qtdClientes][this.qtdClientes];
    	
    	for(i = 0; i < mapaDistancias.length; i++)
    		for(j = 0; j < mapaDistancias.length; j++)
    			if(i==j)
    				this.mapaDistancias[i][j] = 0;
    			else
    				this.mapaDistancias[i][j] = this.clientes.get(i).distancia(this.clientes.get(j));
    }
    
}

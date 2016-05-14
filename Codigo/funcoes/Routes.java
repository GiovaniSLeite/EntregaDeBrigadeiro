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
    
    
    
        /*
        //Passar os parametros
                rastr.numGenes = Integer.valueOf(args[0]);
		rastr.numIndividuos = Integer.valueOf(args[1]);
		rastr.critParada = Integer.valueOf(args[2]);
		rastr.numGeracoes = Integer.valueOf(args[3]);
		rastr.numCross = (int)(Double.valueOf(args[4])*rastr.numIndividuos);
		rastr.crossover = Integer.valueOf(args[5]) == 0? new CrossoverUmPonto() : new CrossoverDoisPontos();
		rastr.probCrossover = Double.valueOf(args[6]);
		rastr.mutacao = Integer.valueOf(args[7]) == 0 ? new MutacaoSimples() : new MutacaoTroca();
		rastr.probMutacao = Double.valueOf(args[8]);
		rastr.critTroca = Integer.valueOf(args[9]);
		rastr.elitismo = Boolean.valueOf(args[10]);
                rastr.intervaloImpressao = Integer.parseInt(args[11]);
        */
    
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
    
    
    @Override
    protected double fitness(int[] genotipo) {
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

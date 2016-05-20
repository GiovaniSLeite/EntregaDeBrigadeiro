
package Operadores;

import algoritmoGenetico.Utils;
import java.util.Random;

public class MutationSimpleRandom extends Mutacao{
    //Matriz de distancias de locais
	double dist[][];
    Random rand;
    //Probabilidade especial de inserir um gene aleatório no melhor lugar possível para ele
    double ProbEspecial;
    
    public MutationSimpleRandom(double[][] distancias, double prob){    
        rand = new Random();
        dist = distancias;
        ProbEspecial = prob;
    }
    

    @Override
    public int[] executar(int[] individuo) {
        
        int p = individuo[rand.nextInt(individuo.length)];
        int [] mutante;
        
        //Se o gene mutante for um carro, nao fazer mutacao
        if(p != -1)
            mutante = individuo;
        else{
        	//Remover o gene mutante do individuo
            mutante = Utils.removeClientes(individuo, p);
            
            //Dada a probabilidade especial, inserir no melhor lugar possivel
            if(rand.nextDouble() <= ProbEspecial)
                mutante = Utils.inserirMelhorLugar(mutante, p, dist);
            else
            	//Ou então inserir num lugar aleatorio
                mutante = Utils.inserirAleatorio(individuo, p, rand.nextInt(individuo.length+1));
        }
            
        return mutante;
    }
}
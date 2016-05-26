
package Operadores;

import algoritmoGenetico.Utils;
import java.util.Arrays;
import java.util.Random;

public class MutationSimpleRandom extends Mutacao{
    //Matriz de distancias de locais
	double dist[][];
    Random rand;
    //Probabilidade especial de inserir um gene aleat�rio no melhor lugar poss�vel para ele
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
        if(p == -1)
            mutante = Arrays.copyOf(individuo, individuo.length);
        else{
        	//Remover o gene mutante do individuo
            mutante = Utils.removeClientes(individuo, p);
            
            //Dada a probabilidade especial, inserir no melhor lugar possivel
            if(rand.nextDouble() <= ProbEspecial)
                mutante = Utils.inserirMelhorLugar(mutante, p, dist);
            else
            	//Ou ent�o inserir num lugar aleatorio
                mutante = Utils.inserirAleatorio(mutante, p, rand.nextInt(individuo.length+1));
        }
            
        return mutante;
    }
}
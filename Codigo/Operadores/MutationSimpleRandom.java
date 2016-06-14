
package Operadores;

import algoritmoGenetico.Utils;
import java.util.Arrays;
import java.util.Random;


public class MutationSimpleRandom extends Mutacao{
    
    //Matriz de distancias de locais
    double dist[][];
    //Gerador de aleatorios
    Random rand;
    //Probabilidade especial de inserir um gene aleat�rio no melhor lugar poss�vel para ele
    public double ProbEspecial;
    
    public MutationSimpleRandom(double[][] distancias, double prob){    
        rand = new Random();
        dist = distancias;
        ProbEspecial = prob;
    }
    
    public String toString(){
        
        return "2";
    }
    
    /*
    Operador de mutacao 3: Simple Random Mutation
    Baseado em:
        Solving the Vehicle Routing Problem with Genetic Algorithms (Master thesis 2004)
        Aslaug Soley Bjarnadottir
    
    Remove um cliente do genotipo original, e, dada uma probabilidade, o insere no melhor lugar ou em um lugar aleatorio
*/
    @Override
    public int[] executar(int[] individuo) {
        
        //Sorteia um gene
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
            	//Ou entao inserir num lugar aleatorio
                mutante = Utils.inserirAleatorio(mutante, p, rand.nextInt(mutante.length+1));
        }
            
        return mutante;
    }
}
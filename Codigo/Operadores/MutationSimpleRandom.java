
package Operadores;

import algoritmoGenetico.Utils;
import java.util.Random;

public class MutationSimpleRandom extends Mutacao{
    double dist[][];
    Random rand;
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
        if(p != -1)
            mutante = individuo;
        else{
            mutante = Utils.removeClientes(individuo, p);
            if(rand.nextDouble() <= ProbEspecial)
                mutante = Utils.inserirMelhorLugar(mutante, p, dist);
            else
                mutante = Utils.inserirAleatorio(individuo, p, rand.nextInt(individuo.length+1));
        }
            
        return mutante;
    }
    
    
    
}

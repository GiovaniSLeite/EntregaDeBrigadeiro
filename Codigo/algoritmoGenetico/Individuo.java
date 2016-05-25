
package algoritmoGenetico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
/*-----------------|Classe Individuo|-----------------/
Essa classe contem o genotipo e o fitness de cada individuo
/-----------------|                 |-----------------*/
public class Individuo implements Comparable<Individuo>{
    public int [] genotipo;
    public double fitness;
    
    /*-----------------|
    O metodo construcao recebe o vetor genotipo e o fitness como parametros
    |-----------------*/
    public Individuo(int[] f, double fit)
    {
        genotipo = Arrays.copyOf(f, f.length);
        fitness = fit;
    }
    
    /*-----------------|
    Esse construtor cria um Individuo aleatorio
    |-----------------*/
    
    //Esse metodo foi adaptado para nao gerar infactibilidade no cromossomo
    //Mas mantendo a aleatoriedade
    public static int[] cromossomoAleatorio(int nroRotas, int nroClientes)
    {
        int [] cromossomo = new int[nroRotas+nroClientes-2];
        Random rand = new Random();
        
        //Distribuir os carros no genotipo
        for(int i = 0; i < nroRotas-1; i++)
        {
            int p = rand.nextInt(cromossomo.length);
            
            if(cromossomo[p] != -1) 
            	cromossomo[p] = -1;
            else
                i--;
        }
        
        ArrayList<Integer> clientesDisponiveis = new ArrayList();
        
        //Comeca do 2 porque o cliente 1 e o deposito
        for(int i = 2; i<= nroClientes; i++)
            clientesDisponiveis.add(i);
        
        //Desordenar o array com vista a garaantir a aleatoriedade
        Collections.shuffle(clientesDisponiveis);
        
        Iterator<Integer> it = clientesDisponiveis.iterator();
        for(int i = 0; i < cromossomo.length && it.hasNext(); i++)         
            if(cromossomo[i]==0)
                cromossomo[i] = it.next();
        return cromossomo;
    }
    
    
    int [] getGenotipo()
    {
        return genotipo;
    }

    public String toString()
    {
        return Arrays.toString(genotipo) + "\t" + fitness;
    }
    
    /*-----------------|
    Metodo compara individuos baseado em seu fitness
    |-----------------*/
    @Override
    public int compareTo(Individuo o) {
        return Double.compare(this.fitness, o.fitness);
    }
}
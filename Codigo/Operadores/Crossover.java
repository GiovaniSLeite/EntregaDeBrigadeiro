package Operadores;

import java.util.Random;

public abstract class Crossover {
    
    Random rand;
    
    public abstract int[][] executar(int[] pai, int[] mae);
    
    public Crossover()
    {
        rand = new Random();
    }
}

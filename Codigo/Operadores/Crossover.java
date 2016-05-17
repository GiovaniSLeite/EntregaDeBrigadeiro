package Operadores;

import java.util.Random;

public abstract class Crossover {
    static final int DEPOSITO = 0;
    Random rand;
    
    public abstract int[][] executar(int[] pai, int[] mae);
    
    public Crossover()
    {
        rand = new Random();
    }
    
    
}

package Operadores;

import java.util.Random;

//Classe abstrata que encapsula um operador de crossover
public abstract class Crossover {
    static final int DEPOSITO = 0;
    Random rand;
    
    public abstract int[][] executar(int[] pai, int[] mae);
    
    public Crossover()
    {
        rand = new Random();
    }
    
    
}

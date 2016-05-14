package Operadores;

import java.util.Random;
public abstract class Mutacao {
    Random rand;
    
    public Mutacao()
    {
        rand = new Random();
    }
    
    public abstract int[] executar(int[] individuo);
}

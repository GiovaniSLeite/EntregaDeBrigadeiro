package Operadores;

import java.util.Random;

//Classe abstrata que encapsula um operador de mutacao
public abstract class Mutacao {
    Random rand;
    
    public Mutacao()
    {
        rand = new Random();
    }
    
    public abstract int[] executar(int[] individuo);
}

package Operadores;

public class MutacaoSimples extends Mutacao{
    
    public MutacaoSimples()
    {
        super();
    }
    
    public String toString()
    {
        return "0";
    }

    
    /*-----------------|
    Operador de mutacao 1: Mutacao Simples
    Recebe o cromossomo de um individuo
    Escolhe uma posicao m e a preenche com um item aleatorio do alfabeto (mutacao)
    Retorna o cromossomo mutado
    |-----------------*/
    @Override
    public int[] executar(int[] mutante) {

        //Escolher um gene
        int m = rand.nextInt(mutante.length);
        //Preenche esse gene com um item do alfabeto escolhido aleatoriamente
        mutante[m] = rand.nextInt(2);
        //retorna o genotipo pos mutacao
        return mutante;
    }
    
    
}

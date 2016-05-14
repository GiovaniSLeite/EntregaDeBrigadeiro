package Operadores;
public class MutacaoTroca extends Mutacao{
    
    public MutacaoTroca()
    {
        super();
    }
    
    public String toString()
    {
        return "1";
    }

    
    /*-----------------|
    Operador de mutacao 2: Mutacao Troca
    Recebe o cromossomo de um individuo
    Escolhe uma posicao m1 e outra m2 e troca o conteudo das mesmas no cromossomo
    Retorna o cromossomo mutado
    |-----------------*/
    @Override
    public int[] executar(int[] mutante) {

        //Escolher dois genes
        int m1 = rand.nextInt(mutante.length);
        int m2 = rand.nextInt(mutante.length);

        //Troca os dois de posicao
        int aux = mutante[m1];
        mutante[m1] = mutante[m2];
        mutante[m2] = aux;
        
        return mutante;
    }
    
}

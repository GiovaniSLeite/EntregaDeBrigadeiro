package Operadores;
public class CrossoverUmPonto extends Crossover {
    
    public CrossoverUmPonto()
    {
        super();
    }
    
    
    @Override
    public String toString()
    {
        return "0";
    }

    /*-----------------|
    Operador de cruzamento 1: Cruzamento de um ponto
    Recebe dois individuos a e b da população
    Sorteia uma posicao i, pega de 0 a i do cromossomo de a e de i ao final do cromossomo de b e vice-versa
    Retorna os dois cromossomos criados a partir de a e b (filhos)
    |-----------------*/
    public int[][] executar(int[] pai1, int[] pai2) {
        
        //Posicao i sorteada
        int p = rand.nextInt(pai1.length - 1)+1;

        int[] f1 = new int[pai1.length];
        int[] f2 = new int[pai1.length];

        //Preenche a primeira parte de cada filho (do 0 até i)
        int i;
        for (i = 0; i < p; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }
        //Preenche a segunda parte de cada filho (de i até o final)
        for (i = p; i < f1.length; i++) {
            f1[i] = pai2[i];
            f2[i] = pai1[i];
        }
        //Retorna os filhos
        return new int[][]{f1, f2};
    }
}

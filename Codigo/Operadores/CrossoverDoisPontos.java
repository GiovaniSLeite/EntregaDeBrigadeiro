package Operadores;

import java.util.Arrays;

public class CrossoverDoisPontos extends Crossover {
    
    public CrossoverDoisPontos()
    {
        super();
    }
    
    
    @Override
    public String toString()
    {
        return "1";
    }
    
    
    /*-----------------|
    Operador de cruzamento 2: Cruzamento de dois pontos
    Recebe dois individuos a e b da populacao
    Sorteia uma posicao i e uma j, pega de 0 a i do cromossomo de a, de i a j do cromossomo b, e de j ao final do cromossomo de a
    Faz o mesmo invertendo os papeis (aba para bab), assim formando o cromossomo dos dois filhos
    Retorna os dois cromossomos criados a partir de a e b (filhos)
    |-----------------*/
    public int[][] executar(int[] pai1, int[] pai2) {

        //Sorteia as posicoes i e j
        int p1 = rand.nextInt(pai1.length - 1)+1;
        int p2 = rand.nextInt(pai1.length - 1)+1;
        
        //Para que as posicoes sejam diferentes e j > i
        while (p2 <= p1) {
            if (p2 < p1) {
                int aux = p1;
                p1 = p2;
                p2 = aux;
            } else if (p2 == p1) {
                p1 = rand.nextInt(pai1.length - 1);
                p2 = rand.nextInt(pai1.length - 1);
            }
        }

        int[] f1 = new int[pai1.length];
        int[] f2 = new int[pai1.length];

        //Primeira parte do cromossomo dos filhos
        for (int i = 0; i < p1; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }
        
        //Segunda parte do cromossomo dos filhos
        for (int i = p1; i < p2; i++) {
            f1[i] = pai2[i];
            f2[i] = pai1[i];
        }
        //Terceira parte do cromossomo dos filhos
        for (int i = p2; i < pai1.length; i++) {
            f1[i] = pai1[i];
            f2[i] = pai2[i];
        }
        //Retorna os dois cromossomos (filhos)
        return new int[][]{f1, f2};
    }
    
}

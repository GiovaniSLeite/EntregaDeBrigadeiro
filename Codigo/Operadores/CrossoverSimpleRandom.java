package Operadores;

import algoritmoGenetico.Utils;
import java.util.ArrayList;
import java.util.Arrays;

public class CrossoverSimpleRandom extends Crossover {

    double[][] dist;

    //Para esse metodo sao necessarias as distancias, por conta disso as mesmas sao passadas como parametro
    public CrossoverSimpleRandom(double[][] dist) {
        super();
        this.dist = dist;
    }

    @Override
    public int[][] executar(int[] pai1, int[] pai2) {

        //Filho recebe pai1
        int[] filho = Arrays.copyOf(pai1, pai1.length);
        

        //Escolhe uma subrota do pai2
        ArrayList<Integer> subrota2 = algoritmoGenetico.Utils.encontraRota(pai2, rand.nextInt(pai2.length));
        System.out.println("ALOU: "+ subrota2);
        //Se escolheu uma rota com mais de um cliente
        if (subrota2.size() > 0) {
            //Remove os clientes do filho que sao atendidos na subrota do pai2
            filho = algoritmoGenetico.Utils.removeClientes(filho, subrota2);
            //Insere no melhor Lugar
            int[] novoFilho = Utils.InserirMelhorLugar(filho, subrota2, dist);
            //Retorna o filho completo
            int[][] c = new int[1][novoFilho.length];
            c[0] = novoFilho;
            return c;
        } else {
            return new int[][]{filho};
        }
    }
}
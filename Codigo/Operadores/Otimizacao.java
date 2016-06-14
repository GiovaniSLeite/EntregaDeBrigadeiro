package Operadores;

import java.util.ArrayList;
import java.util.Random;

import algoritmoGenetico.Utils;

public class Otimizacao {
	
    double[][] dist;
    Random rand;

    public Otimizacao(double[][] dist)
    {
            this.dist = dist;
            rand = new Random();
    }
    /*
        Operador de otimizacao
            Recebe um genotipo, escolhe uma rota aleatoria e a reordena da melhor maneira
    */
    public int[] executar(int[] genotipoX)
    {
        int[] genotipo = genotipoX.clone();
        int[] subrota;
        int i = 0;
        int j = 0;
        int ind = 0;

        //Decompor o genotipo em rotas
        ArrayList<ArrayList<Integer>> subrotas = Utils.getRotas(genotipo);
        ind = rand.nextInt(subrotas.size());
        ArrayList<Integer> subRota = subrotas.get(ind);

        //Fazer um array so com os clientes da subrota
        subrota = new int[subRota.size()];
        for(i = 0; i < subRota.size(); i++)
                subrota[i] = subRota.get(i);

        //Otimizar a subrota
        subrota = this.ap2opt(subrota);
        if(subRota.size() > 0){
            //Colocar a nova subrota no genotipo
            for(i = 0; i < genotipo.length; i++)
                    if(genotipo[i]==subRota.get(0))
                            break;

            for(j = 0; j < subrota.length; j++)
                    genotipo[i++] = subrota[j];
        }
        return genotipo;
    }

    //2 - OPT: Heuristica de melhoria -> Recebe um genotipo e reordena-o
    //da melhor maneira possivel
    public int[] ap2opt (int[] genotipo)
    {	
        double melhora, diferencaDist;
        int i, j, t1, t2, t3, t4, t1Best, t4Best, aux;

        t1Best = t4Best = aux = -1;

        melhora = 1;

        while(melhora > 0)
        {
            melhora = 0;
            for(i = 0; i < genotipo.length-1; i++)
            {
                for(j = 0; j < genotipo.length-1; j++)
                {
                    if(i!=j && i+1!=j && i!=j+1 && i+1!=j+1)
                    {
                        t1 = genotipo[i];
                        t2 = genotipo[i+1];
                        t3 = genotipo[j+1];
                        t4 = genotipo[j];

                        diferencaDist = dist[t1-1][t2-1] + dist[t4-1][t3-1] - dist[t2-1][t3-1] - dist[t1-1][t4-1];

                        if(diferencaDist > melhora)
                        {
                            melhora = diferencaDist;
                            t1Best = i;
                            t4Best = j;
                        }
                    }
                }
            }

            //Rearranjar os valores no genotipo eh trocar de posicao
            if(melhora > 0)
            {
            //jogar t4 no lugar de t2 e t2 no lugar de t4
            aux = genotipo[t1Best + 1];
            genotipo[t1Best+1] = genotipo[t4Best];
            genotipo[t4Best] = aux;
            }
        }

        return genotipo;
    }
	
}
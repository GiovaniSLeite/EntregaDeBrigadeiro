
package Operadores;

import java.util.ArrayList;
import java.util.Arrays;

public class CrossoverSimpleRandom extends Crossover{
    double[][] dist;
    
    //Para esse metodo sao necessarias as distancias, por conta disso as mesmas sao passadas como parametro
    public CrossoverSimpleRandom(double[][] dist)
    {
        super();
        this.dist = dist;
    }
    
    @Override
    public int[][] executar(int[] pai1, int[] pai2) {
        
       //Filho recebe pai1
       int [] filho = Arrays.copyOf(pai1, pai1.length);
       
        //Escolhe uma subrota do pai2
        ArrayList<Integer> subrota2 = algoritmoGenetico.Utils.encontraRota(pai2, rand.nextInt(pai2.length));
        //Se escolheu uma rota com mais de um cliente
        if(subrota2.size() > 0){
            //Remove os clientes do filho que sao atendidos na subrota do pai2
            filho = algoritmoGenetico.Utils.removeClientes(filho, subrota2);

            //Procura o melhor lugar para inserir esse filho
            //Para cada espaco entre um cliente a e outro b de cada rota,
            //verifica se eh um bom lugar para inserir a subrota do pai2
            //O melhor lugar eh o com maior PayOff
            //PayOff = distancia entre a e b - a distancia entre a e o primeiro da subrota do pai2 - distancia entre b e o ultimo da subrota do pai2
            double maiorPayOff = 0.0;
            int ondeInserir = 0;
            for(int i =1; i < filho.length; i++)
            {
                if(filho[i-1] == -1 && filho [i] ==-1) continue;
                double payOff = 0.0;
                if(filho[i-1] == -1)
                     payOff = dist[DEPOSITO][filho[i]-1] - dist[DEPOSITO][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)-1][filho[i]-1];
                if(filho[i] == -1)
                    payOff = dist[filho[i-1]-1][DEPOSITO] - dist[filho[i-1]-1][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)-1][DEPOSITO];

                if(payOff > maiorPayOff)
                {
                    maiorPayOff = payOff;
                    ondeInserir = i-1;
                }
            }
            
            //Novo filho
            int[] novoFilho = new int[filho.length + subrota2.size()];
            
            //As partes do filho sem os clientes da subrota do pai 2
            for(int i =0; i <= ondeInserir; i++)
                novoFilho[i] = filho[i];

            int indice = ondeInserir+1;
            
            //A subrota do pai 2 inserida no melhor lugar
            for(int aux : subrota2)
                novoFilho[indice++] = aux;

            //O restante que compoe o filho
            for(int i = indice; i < novoFilho.length; i++)
                novoFilho[i] = filho[i-subrota2.size()];
            //Retorna o filho completo
            int[][] c = new int[1][novoFilho.length];
            c[0] = novoFilho;
            return c;
       }else
           return new int[][]{filho};
    }
}

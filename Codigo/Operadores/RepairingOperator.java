
package Operadores;

import algoritmoGenetico.Cliente;
import algoritmoGenetico.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RepairingOperator {
    Random rand;
    public RepairingOperator()
    {
        rand = new Random();
    }
   
    
    /*
        Operador de reparacao
        Objetivo: Mitigar a infactibilidade de capacidade
        
        Verifica as subrotas, se a que atende a maior demanda estoura a capacidade, remove um cliente aleatorio e o insere ao final da rota com menor demanda.
    */
    public int[] executar(int [] genotipo, List<Cliente> clientes, int capacidade)
    {
        /*Divide o genotipo em uma lista de subrotas*/
        ArrayList<ArrayList<Integer>> rotas = Utils.getRotas(genotipo);
        
        
        double maiorCusto = Double.MIN_VALUE;
        int maisCustosa = 0;
        double menorCusto = Double.MAX_VALUE;
        int menosCustosa = 0;
        
        /*Percorre as subrotas*/
        for(int i = 0; i < rotas.size(); i++)
        {
            /*Calcula a demanda de cada uma das subrotas*/
            ArrayList<Integer> sub = rotas.get(i);
            double demand=0;
            for(Integer cliente : sub)
                demand+= clientes.get(cliente-1).demanda;
            
            /*Guarda a com maior demanda*/
            if(demand > maiorCusto){
                maiorCusto = demand;
                maisCustosa = i;
            }else /*Bem como a menor demanda*/
                if(demand < menorCusto)
                {
                    menorCusto = demand;
                    menosCustosa = i;
                }
        }
        
        /*Se a maior estoura a capacidade*/
        if(maiorCusto > capacidade)
        {
            /*Remove um cliente aleatorio da rota com maior demanda*/            
            Integer aleatorioRemovido = rotas.get(maisCustosa).get(rand.nextInt(rotas.get(maisCustosa).size()));
            rotas.get(maisCustosa).remove(aleatorioRemovido);
            
            /*Insere na rota com menor demanda*/
            if(!rotas.get(menosCustosa).contains(aleatorioRemovido))
                rotas.get(menosCustosa).add(aleatorioRemovido);
            
            /*Converte de volta a genotipo*/
            genotipo = Utils.converteListasEmGenotipo(rotas);
        }
        
        return genotipo;
    }
    
}

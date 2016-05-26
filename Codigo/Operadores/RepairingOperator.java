
package Operadores;

import algoritmoGenetico.Cliente;
import algoritmoGenetico.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RepairingOperator {
    Random rand;
    public RepairingOperator()
    {
        rand = new Random();
    }
    public int[] executar(int [] genotipo, List<Cliente> clientes, int capacidade)
    {
        ArrayList<ArrayList<Integer>> rotas = Utils.getRotas(genotipo);
        double maiorCusto = Double.MIN_VALUE;
        int maisCustosa = 0;
        double menorCusto = Double.MAX_VALUE;
        int menosCustosa = 0;
        
        for(int i = 0; i < rotas.size(); i++)
        {
            ArrayList<Integer> sub = rotas.get(i);
            double demand=0;
            for(Integer cliente : sub)
                demand+= clientes.get(cliente-1).demanda;
            
            if(demand > maiorCusto){
                maiorCusto = demand;
                maisCustosa = i;
            }else
                if(demand < menorCusto)
                {
                    menorCusto = demand;
                    menosCustosa = i;
                }
        }
        
        if(maiorCusto > capacidade)
        {
            Integer aleatorioRemovido = rotas.get(maisCustosa).get(rand.nextInt(rotas.get(maisCustosa).size()));
            rotas.get(maisCustosa).remove(aleatorioRemovido);
            
            rotas.get(menosCustosa).add(aleatorioRemovido);
            
            genotipo = Utils.converteListasEmGenotipo(rotas);
        }
        
        return genotipo;
    }
    
}

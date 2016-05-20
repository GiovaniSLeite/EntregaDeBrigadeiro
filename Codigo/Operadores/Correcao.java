
package Operadores;

import algoritmoGenetico.Utils;
import java.util.ArrayList;
import java.util.Random;


public class Correcao {
    static Random rand;
    double[][] dist;
    public Correcao(double [][] dist)
    {
        this.dist = dist;
        rand = new Random();
    }
    
    //executar(new int[]{1,-1,3,4,5,-1,1,8,7},22);
    
    
    public int[] executar(int[] genotipo, int clientes, int motoristas)
    {
        //Pega as rotas do genotipo
        ArrayList<ArrayList<Integer>> rotas = Utils.getRotas(genotipo);
        //Remove os clientes repetidos
        for(int i = 0; i < rotas.size(); i++)
            for(int j = i+1; j < rotas.size(); j++)
            {
                ArrayList<Integer> aux = new ArrayList();
                aux.addAll(rotas.get(i));
                aux.retainAll(rotas.get(j));
                
                if(!aux.isEmpty())
                    for(Integer repetidos : aux){
                        rotas.get(i).remove(repetidos);
                        rotas.get(j).remove(repetidos);
                    }
            }
        //Adiciona as rotas faltantes/remove as sobressalentes
        while(rotas.size() > motoristas)
            rotas.remove(rand.nextInt(rotas.size()));
        while(rotas.size() < motoristas)
            rotas.add(new ArrayList());
        //Seleciona os clientes faltantes
        ArrayList<Integer> clientesFaltantes = new ArrayList();
        for(int i = 2; i < clientes; i++)
            clientesFaltantes.add(i);
        for(ArrayList<Integer> subRotas : rotas)
            clientesFaltantes.removeAll(subRotas);
        
        //Transforma em um genotipo
        genotipo = Utils.converteListasEmGenotipo(rotas);
        
        for(int cl : clientesFaltantes)
            genotipo = Utils.inserirMelhorLugar(genotipo, cl, dist);
     
        return genotipo;
    }
}


package Operadores;

import algoritmoGenetico.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Correcao {
    static Random rand;
    double[][] dist;
    double probEspecial;
    public Correcao(double [][] dist, double pb)
    {
        this.dist = dist;
        rand = new Random();
        probEspecial = pb;
    }
    
    //Mtodo executar, recebe um gentipo e o devolve corrigido.
    public int[] executar(int[] genotipo, int clientes, int motoristas)
    {
        //Pega as rotas do genotipo - se existirem clientes repetidos
    	//dentro de uma rota, ja serao corrigidos aqui
        ArrayList<ArrayList<Integer>> rotas = Utils.getRotas(genotipo);
        
        //Remove os clientes repetidos entre rotas
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
        for(int i = 2; i <= clientes; i++)
            clientesFaltantes.add(i);
        for(ArrayList<Integer> subRotas : rotas)
            clientesFaltantes.removeAll(subRotas);
        
        //Transforma em um genotipo
        genotipo = Utils.converteListasEmGenotipo(rotas);
        
        Collections.shuffle(clientesFaltantes);
        //Para cada cliente faltante, inserir no melhor lugar poss�vel
        for(Integer cl : clientesFaltantes)
            if(probEspecial <= rand.nextDouble())
                genotipo = Utils.inserirAleatorio(genotipo, cl, rand.nextInt(genotipo.length+1)); 
            else genotipo = Utils.inserirMelhorLugar(genotipo, cl, dist);
     
        return genotipo;
    }
}
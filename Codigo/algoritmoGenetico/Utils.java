package algoritmoGenetico;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*-----------------|Classe Utils|-----------------/
//Espaco de metodos utilitarios para calculos
/-----------------|            |-----------------*/
public class Utils {

	//Metodo que faz leitura do arquivo e armazena os clientes numa lista
    public static ArrayList<Cliente> getClientes(String arq) {
        
        ArrayList<Cliente> clientes = new ArrayList();
        
        try {
            BufferedReader ler = new BufferedReader(new FileReader(new File(arq)));
            
            //Ler as primeiras tres linhas
            int i = 3;
            while (i > 0) {
                i--;
                ler.readLine();
            }
            
            //Pegar quantidade de clientes depois do : da 4 linha
            int qtdClientes = Integer.valueOf(ler.readLine().split(":")[1].trim());
            
            //Ler as proximas tres linhas
            i = 3;
            while (i > 0) {
                i--;
                ler.readLine();
            }
            
            //Ler as coordenadas dos qtdClientes clientes
            ArrayList<Integer> x = new ArrayList();
            ArrayList<Integer> y = new ArrayList();
            for (i = 0; i < qtdClientes; i++) {
                String[] coord = ler.readLine().trim().split("\\s+");
                x.add(Integer.valueOf(coord[1]));
                y.add(Integer.valueOf(coord[2]));
            }
            ler.readLine();

            //Ler a demanda de cada cliente
            ArrayList<Integer> demand = new ArrayList();

            for (i = 0; i < qtdClientes; i++) {
                String[] coord = ler.readLine().split("\\s+");
                demand.add(Integer.valueOf(coord[1]));
            }

            //Adicionar os clientes na estrutura especifica
            for (i = 0; i < x.size(); i++) {
                clientes.add(new Cliente(x.get(i), y.get(i), demand.get(i)));
            }
            
        } catch (IOException | NumberFormatException ex) {
            System.out.println("GETCLIENTES: IO ou NumberFormat");
        }
        
        //Retornar a lista de clientes
        return clientes;
    }

    //Pegar a quantidade de caminhoes necessarios para solucionar o problema
    //Esse metodo esta pegando o numero de caminhoes da segunda linha do arquivo
    public static int getQtdRotas(String arq){
        int resp = 1;
        try {
            BufferedReader ler = new BufferedReader(new FileReader(new File(arq)));
            ler.readLine();
            String[] comment = ler.readLine().split("\\D+");
            resp = Integer.valueOf(comment[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resp;
    }
    
    //Metodo que recebe um genotipo, uma posicao, e retorna a rota
    //em que esse cliente esta inserido
    //Pre-requisito: posicao deve ser um cliente e nao um carro
    public static ArrayList<Integer> encontraRota(int[] genotipo, int posicao)
    {
        int cmc = 0;
        int fim = genotipo.length-1;
        
        //Encontra o comeco da rota
        for(int i = posicao; i>=0; i--)
            if(genotipo[i] == -1){
                cmc = i+1;
                break;
            }
        
        //Encontra o final da rota
        for(int i = posicao+1; i < genotipo.length; i++)
            if(genotipo[i] == -1){
                fim = i-1;
                break;
            }
        
        //Adiciona esses a uma lista
        ArrayList<Integer> rota = new ArrayList();
        for(int i = cmc; i <= fim; i++)
            rota.add(genotipo[i]);
        
        //Retorna a lista
        return rota;
    }
    
    //Metodo que remove de um genotipo todos os clientes que estao numa lista de clientes
    //Pre-requisito: os clientes removidos nao podem estar repetidos - motivo: tamanho do array novoGenotipo
    public static int[] removeClientes(int[] genotipo, ArrayList<Integer> clientes)
    {
        int[] novoGenotipo = new int[genotipo.length - clientes.size()];
        int nAdicionados = 0;
        
        for(int i =0; i < genotipo.length; i++)
            if(!clientes.contains(genotipo[i]))
                novoGenotipo[i-nAdicionados] = genotipo[i];
            else
                nAdicionados++;
        
        return novoGenotipo;          
    }
    
    //Metodo que remove um cliente especifico de um genotipo
    //Pre-requisito: o cliente nao pode estar repetido - motivo: tamanho do array novoGenotipo
    public static int[] removeClientes(int[] genotipo, int cliente)
    {
        int[] novoGenotipo = new int[genotipo.length - 1];
        int nAdicionados = 0;
        
        for(int i =0; i < genotipo.length; i++)
            if(genotipo[i] == cliente)
                novoGenotipo[i-nAdicionados] = genotipo[i];
            else
                nAdicionados++;
        
        return novoGenotipo;          
    }
    
    //Procura o melhor lugar para inserir esse filho
    //Para cada espaco entre um cliente a e outro b de cada rota,
    //verifica se eh um bom lugar para inserir a subrota do pai2
    //O melhor lugar eh o com maior PayOff
    //PayOff = distancia entre a e b - a distancia entre a e o primeiro da subrota do pai2 - distancia entre b e o ultimo da subrota do pai2 
    public static int[] InserirMelhorLugar(int[] filho, ArrayList<Integer> subrota2, double[][] dist)
    {
    	//Inicializacao de variaveis
        int DEPOSITO = 0;
        double maiorPayOff = Double.MIN_VALUE;
        
        int ondeInserir = 0;
        
        for(int i =1; i < filho.length; i++)
        {
            double payOff = 0.0;
            
            // Insercao em uma rota vazia
            if(filho[i-1] == -1 && filho [i] ==-1)
            	payOff = dist[DEPOSITO][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)][DEPOSITO];
            
            // Insercao entre o deposito e um cliente
            else if(filho[i-1] == -1)
                payOff = dist[DEPOSITO][filho[i]-1] - dist[DEPOSITO][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)-1][filho[i]-1];
            
            // Insercao entre um cleinte e o deposito
            else if(filho[i] == -1)
                payOff = dist[filho[i-1]-1][DEPOSITO] - dist[filho[i-1]-1][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)-1][DEPOSITO];
            
            // Insercao entre dois filhos
            else
                payOff = dist[filho[i-1]-1][filho[i]-1] - dist[filho[i-1]-1][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)-1][filho[i]-1];

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
            
            return novoFilho;
    }
    
    //Mesmo esquema acima, so que inserindo um unico cliente
    public static int[] inserirMelhorLugar(int[] filho, int cliente, double[][] dist)
    {
    	//Iniciaizacao de variaveis
        int DEPOSITO = 0;
        double maiorPayOff = 0.0;
        int ondeInserir = 0;
        
        for(int i =1; i < filho.length; i++)
        {   
            double payOff = Double.MIN_VALUE;
            
            //Inserir numa rota vazia
            if(filho[i-1] == -1 && filho [i] ==-1)
            	payOff = dist[DEPOSITO][cliente-1] * 2;
            
            //Inserir entre o deposito e um filho
            else if(filho[i-1] == -1)
                 payOff = dist[DEPOSITO][filho[i]-1] - dist[DEPOSITO][cliente-1] - dist[cliente-1][filho[i]-1];
            
            //inserir entre um filho e o deposito
            else if(filho[i] == -1)
                payOff = dist[filho[i-1]-1][DEPOSITO] - dist[filho[i-1]-1][cliente-1] - dist[cliente-1][DEPOSITO];
            
            //Inserir entre dois filhos
            else
                payOff = dist[filho[i-1]-1][filho[i]-1] - dist[filho[i-1]-1][cliente-1] - dist[cliente-1][filho[i]-1];

            //Se o payOff for maior, substituir
            if(payOff > maiorPayOff)
            {
                maiorPayOff = payOff;
                ondeInserir = i-1;
            }
        }
            
            //Novo filho
            int[] novoFilho = new int[filho.length + 1];
            
            //As partes do filho sem os clientes da subrota do pai 2
            for(int i =0; i <= ondeInserir; i++)
                novoFilho[i] = filho[i];
            
            novoFilho[ondeInserir+1] = cliente;
        
            //O restante que compoe o filho
            for(int i = ondeInserir+2; i < novoFilho.length; i++)
                novoFilho[i] = filho[i-1];
            
            return novoFilho;
    }
    
    //Metodo que insere um cliente numa posicao p dentro do genotipo de um individuo
    //deslocando os individuos impactados para a frente
    public static int[] inserirAleatorio(int [] individuo, int cliente, int p)
    {
        int [] filho = new int[individuo.length+1];
        
        int i;
        for(i = 0; i < p; i ++)
            filho[i] = individuo[i];
        
        filho[i] = cliente;
        
        for(i = p+1; i < filho.length; i++)
            filho[i] = individuo[i-1];
        
        return filho;
    }
    
    //Metodo que obtem todas as rotas que existem num genotipo
    //Basicamente destrincha um genotipo numa lista de rotas
    //Nao insere rotas vazias
    public static ArrayList<ArrayList<Integer>> getRotas(int [] genotipo)
    {
        ArrayList<ArrayList<Integer>> rotas = new ArrayList();
        
        for(int i = 0; i < genotipo.length; i++)
        {
            ArrayList<Integer> aux = new ArrayList();
            
            //Se a rota for vazia, nao ocorre a insercao
            if(genotipo[i] != -1)
            {
                while(i < genotipo.length && genotipo[i] != -1)
                    if(!aux.contains(genotipo[i]))
                        aux.add(genotipo[i++]);
                    else
                        i++;
                
                rotas.add(aux);
            }   
        }
        
        return rotas;
    }
    
    //boundingBox -> Retorna o menor retangulo que contem todos os pontos da subrota passada
    //Fecho convexo retangular
    public static Rectangle boundingBox(ArrayList<Integer> subrota, ArrayList<Cliente> clientes)
    {
        int[] x = new int[subrota.size()+1];
        int[] y = new int[subrota.size()+1];
        x[0] = clientes.get(0).x;
        y[0] = clientes.get(0).y;
        for(int i = 1; i < x.length; i++)
        {
            x[i] = clientes.get(subrota.get(i-1)-1).x;
            y[i] = clientes.get(subrota.get(i-1)-1).y;
        }       
        return new Polygon(x, y, x.length).getBounds();
    }
    
    //Metodo que converte uma lista de listas de clientes em um genotipo
    public static int[] converteListasEmGenotipo(ArrayList<ArrayList<Integer>> rotas)
    {
        int tamanho = 0;
        
        //Calcula o tamanho da lista de rotas
        for(ArrayList<Integer> subrotas : rotas)
            tamanho+=subrotas.size();
        
        //Menos um porque um carro nao aparece na representacao
        tamanho+=rotas.size()-1;
        
        int[] genotipo = new int[tamanho];
        int i = 0;
        for(ArrayList<Integer> subrotas : rotas){
        	
        	//Inserir todos os clientes da subrota
            for(int gene : subrotas)
                genotipo[i++] = gene;
            
            //Inserir -1 ao fim da subrota
            if(i < genotipo.length)
                genotipo[i++] = -1;
        }
        
        return genotipo;
    }

}
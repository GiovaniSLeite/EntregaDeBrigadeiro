package algoritmoGenetico;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    
    public static int getCapacity(String arq){
        int resp = 0;
        try {
            BufferedReader ler = new BufferedReader(new FileReader(new File(arq)));
            String linha = "";
            
            while(!(linha = ler.readLine()).contains("CAPACITY")){}
            
            String[] comment = linha.split(":");
            resp = Integer.valueOf(comment[1].trim());

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
        
        int i = 0, j = 0;
        while(i < novoGenotipo.length)
            if(genotipo[j] != cliente)
                novoGenotipo[i++] = genotipo[j++];
            else
                j++;
        
        return novoGenotipo;          
    }
    
    
    /*AQUI DECIO*/
    /*
        Funcao para inserirMelhorLugar (Retirado de master thesis 2004)
        Recebe
            Um genotipo a (incompleto, sem os clientes da subrota s)
            Uma subrota s
            Uma matriz informando a distancia entre os clientes e o deposito, todos x todos
        Retorna
            O genotipo contendo a subrota s no melhor lugar possivel
            
    */
    public static int[] InserirMelhorLugar(int[] filho, ArrayList<Integer> subrota2, double[][] dist)
    {
        int DEPOSITO = 0;
        
        /*Faz uma copia do filho*/
    	int[] filhoAdaptado = Arrays.copyOf(filho, filho.length);
        
        /*Muda a representacao dos carros para 1*/
        for(int i = 0; i < filhoAdaptado.length; i++)
            if(filhoAdaptado[i] == -1) filhoAdaptado[i] = 1;
        
        /*PayOff = a distancia entre duas partes de a (deposito e cliente, cliente e cliente ou cliente deposito em alguma das subrotas de a)
             - a distancia entre a parte 1 e o primeiro da subrota s - a distancia entre o ultimo da subrota s e a segunda parte de a*/
        
        /*Calcula o payoff para adicionar a subrota no comeco do genotipo e no final*/
        double entreDepositoEPrimeiro = dist[DEPOSITO][filhoAdaptado[0]-1] - dist[DEPOSITO][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)-1][filhoAdaptado[0]-1];
        double entreUltimoEDeposito = dist[filhoAdaptado[filhoAdaptado.length-1]-1][DEPOSITO] - dist[filhoAdaptado[filhoAdaptado.length-1]-1][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)-1][DEPOSITO];
        
        double maiorPayOff;
        int ondeInserir;
        if(entreDepositoEPrimeiro > entreUltimoEDeposito)
        {
            maiorPayOff = entreDepositoEPrimeiro;
            ondeInserir = 0;
        }else
        {
            maiorPayOff = entreUltimoEDeposito;
            ondeInserir = filhoAdaptado.length;
        }
        
        /*Percorre o genotipo calculando os payOffs*/
        for(int i = 1; i < filhoAdaptado.length; i++)
        {
            double payOff = dist[filhoAdaptado[i-1]-1][filhoAdaptado[i]-1] - dist[filhoAdaptado[i-1]-1][subrota2.get(0)-1] - dist[subrota2.get(subrota2.size()-1)-1][filhoAdaptado[i]-1];
            
            if(payOff > maiorPayOff)
            {
                maiorPayOff = payOff;
                ondeInserir = i;
            }
        }
        
        /*Compoe o genotipo a ser retornado inserindo a subrota s no melhor lugar*/
        int[] novoFilho = new int[filho.length + subrota2.size()];
            
            for(int i =0; i < ondeInserir; i++)
                novoFilho[i] = filho[i];

            int indice = ondeInserir;
            
            for(int aux : subrota2)
                novoFilho[indice++] = aux;

            for(int i = indice; i < novoFilho.length; i++)
                novoFilho[i] = filho[i-subrota2.size()];
            
            return novoFilho;
    }
    
    /*Calcula o melhor lugar para um cliente ser inserido e retorna o genotipo, agora, com esse cliente*/
    public static int[] inserirMelhorLugar(int[] filho, int cliente, double[][] dist)
    {
    	//Iniciaizacao de variaveis
        int DEPOSITO = 0;
        int[] filhoAdaptado = Arrays.copyOf(filho, filho.length);
        for(int i = 0; i < filhoAdaptado.length; i++)
            if(filhoAdaptado[i] == -1) filhoAdaptado[i] = 1;
        
        //distancias
        double entreDepositoEPrimeiro = dist[DEPOSITO][filhoAdaptado[0]-1] - dist[DEPOSITO][cliente-1] - dist[cliente-1][filhoAdaptado[0]-1];
        double entreUltimoEDeposito = dist[filhoAdaptado[filhoAdaptado.length-1]-1][DEPOSITO] - dist[filhoAdaptado[filhoAdaptado.length-1]-1][cliente-1] - dist[cliente-1][DEPOSITO];
        
        double maiorPayOff;
        int ondeInserir;
        if(entreDepositoEPrimeiro > entreUltimoEDeposito)
        {
            maiorPayOff = entreDepositoEPrimeiro;
            ondeInserir = 0;
        }else
        {
            maiorPayOff = entreUltimoEDeposito;
            ondeInserir = filhoAdaptado.length;
        }
        
        for(int i = 1; i < filhoAdaptado.length; i++)
        {
            double payOff = dist[filhoAdaptado[i-1]-1][filhoAdaptado[i]-1] - dist[filhoAdaptado[i-1]-1][cliente-1] - dist[cliente-1][filhoAdaptado[i]-1];
            
            if(payOff > maiorPayOff)
            {
                maiorPayOff = payOff;
                ondeInserir = i;
            }
        }
            
            //Novo filho
            int[] novoFilho = new int[filho.length + 1];
            
            //As partes do filho sem os clientes da subrota do pai 2
            for(int i =0; i < ondeInserir; i++)
                novoFilho[i] = filho[i];
            
            novoFilho[ondeInserir] = cliente;
        
            //O restante que compoe o filho
            for(int i = ondeInserir+1; i < novoFilho.length; i++)
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
            }
            rotas.add(aux);
            
        }
        
        if(genotipo[genotipo.length-1] == -1) rotas.add(new ArrayList<Integer>());
        return rotas;
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
    
    public static int demandaTotal(List<Cliente> clientes)
    {
        int total = 0;
        
        for(Cliente cli : clientes)
            total += cli.demanda;
        
        return total;
    }

}

package funcoes;

import Operadores.*;
import algoritmoGenetico.*;
import java.util.ArrayList;

public class Routes extends AlgoritmosGeneticos {
    private static final int DEPOSITO = 0;
    
    public Routes(int qRotas, ArrayList<Cliente> aux)
    {
        this.qtdRotas = qRotas;
        this.clientes = new ArrayList();
        clientes.addAll(aux);
        this.qtdClientes = this.clientes.size();
        this.calcularDistancias();
        
    }
    public static void main(String[] args)
    {
       
        /*args
        0 - nome do arquivo teste
        1 - ID
        2 - Numero de individuos
        3 - criterio de parada
        4 - numero de geracoes
        5 - numero de crossovers
        6 - tipo do crossover
        7 - probabilidade do crossover
        8 - tipo da mutacao
        9 - probabilidade da mutacao
        10 - prob especial
        11 - criterio de troca
        12 - elitismo
        13 - prob correcao
        14 - prob reparação (caso de capacidade)
        15 - prob otimizacao
        16 - intervalo de impressao
        */
        //args = new String[]{"A-n32-k5.vrp","0","5000","0","100","1","3","1","2","0.05","0.5","1","false","0","1","0.5","5"};
        String arquivoTeste = "C:\\Users\\sousa\\OneDrive\\Documentos\\NetBeansProjects\\EP1IA_2\\A-VRP\\"+args[0];
        int qRotas = Utils.getQtdRotas(arquivoTeste);
        ArrayList<Cliente> aux = Utils.getClientes(arquivoTeste);
        Routes r = new Routes(qRotas, aux);
        r.ID = Integer.parseInt(args[1]);
        r.capacidade = Utils.getCapacity(arquivoTeste);
        r.numIndividuos = Integer.parseInt(args[2]);
        r.critParada = Integer.parseInt(args[3]);
        r.numGeracoes = Integer.parseInt(args[4]);
        r.numCross = (int)(Double.parseDouble(args[5])*r.numIndividuos);
        r.crossover = args[6].equals("0") ? new CrossoverUmPonto() : args[6].equals("1") ? new CrossoverDoisPontos() : args[6].equals("2") ? new CrossoverSimpleRandom(r.mapaDistancias) : new CrossoverOXadaptado();
        r.probCrossover = Double.parseDouble(args[7]);
        r.probEspecialMutacao = Double.parseDouble(args[10]);
        r.mutacao = args[8].equals("0") ? new MutacaoSimples() : args[8].equals("1") ? new MutacaoTroca() : new MutationSimpleRandom(r.mapaDistancias, r.probEspecialMutacao);
        r.probMutacao = Double.parseDouble(args[9]);
        r.critTroca = Integer.parseInt(args[11]);
        r.elitismo = args[12].equals("true");
        r.probEspecialCorrecao = Double.parseDouble(args[14]);
        r.corretor = new Correcao(r.mapaDistancias, r.probEspecialCorrecao);
        r.probCorrecao = Double.parseDouble(args[13]);
        r.reparador = new RepairingOperator();
        r.probReparacao = Double.parseDouble(args[15]);
        r.otimizador = new Otimizacao(r.mapaDistancias);
        r.probOtimizacao = Double.parseDouble(args[16]);
        r.intervaloImpressao = Integer.parseInt(args[17]);
        
        r.evolucao();
        
        
    }
    
    
    /*
        Funcao fitness
        Recebe um genotipo e retorna sua avaliacao
    */
    @Override
    protected double fitness(int[] genotipo)
    {
        /*A distancia percorrida inicial eh 0
        o ponto de partida (anterior) eh -1, o que simboliza o deposito
        o atual sera atribuido posteriormente*/
    	double distanciaPercorrida = 0;
    	int anterior = -1;
    	int atual;
    	
        /*Percorre o genotipo*/
    	for(int i = 0; i < genotipo.length; i++)
    	{       /*Se for -1, chegou ao final de uma rota/a volta ao deposito*/
    		if(genotipo[i] == -1)
    		{
                    /*Caso nao seja uma rota vazia*/
                    if(anterior!=-1)
                    {
                        /*eh somada a volta ao deposito
                        e o anterior eh sinalizado como deposito*/
                        distanciaPercorrida += this.mapaDistancias[anterior][DEPOSITO];
                        anterior = -1;
                    }
    		}
    		else
    		{
                    /*Caso seja um cliente, o atual recebe o numero equivalente a este na lista de clientes*/
                    atual = genotipo[i]-1;
                    /*Caso seja inicio da rota*/
                    if(anterior==-1)
                    {
                        /*Eh somada a distancia do deposito ate o atual*/
                        distanciaPercorrida += this.mapaDistancias[DEPOSITO][atual];
                        anterior = atual;
                    }/*Caso seja meio da rota*/
                    else
                    {
                        /*Eh somada a distancia do cliente anterior ate o atual*/
                        distanciaPercorrida += this.mapaDistancias[anterior][atual];
                        anterior = atual;       
                    }
    		}
    	}
    	/*Se a ultima rota nao for vazia: acrescenta a distancia do ultimo ate o deposito*/
    	if (genotipo[genotipo.length-1] != -1 && anterior != -1)
    		distanciaPercorrida += this.mapaDistancias[anterior][DEPOSITO];
    	
        /*Retorna a distância somada a penalizacao*/
    	return distanciaPercorrida+ ((double)this.indGeracao/this.IT)*this.alpha*penalizar(genotipo);
    }
    
    
    /*
        Funcao penalizar
        Recebe um genotipo
        Retorna a soma dos estouros de capacidade das subrotas
    */
    @Override
    public int penalizar(int [] genotipo)
    {
        /*Divide o genotipo em sub-rotas*/
        ArrayList<ArrayList<Integer>> rotas = Utils.getRotas(genotipo);
        
        /*Calcula o estouro de capacidade para cada um deles e acrescenta a penalizacao*/
        int penalizacao = 0;
        for(ArrayList<Integer> clientesAtendidos : rotas)
        {
            /*Inicia penalizacaoLocas com o oposto da capacidade*/
            int penalizacaoLocal = -capacidade;
            /*soma a demanda dessa subrota a penalizacaoLocal*/
            for(Integer cliAtendido : clientesAtendidos)
                penalizacaoLocal+= clientes.get(cliAtendido-1).demanda;
            /*Se ela for maior que 0, ocorrou um estouro da capacidade, entao soma a penalizacao*/
            if(penalizacaoLocal>0)
                penalizacao+=penalizacaoLocal;
        }
        
        return penalizacao;
        
    }
    
    /*
        Funcao alpha
        Essa funcao teve como base:
            Solving the Vehicle Routing Problem with Genetic Algorithms (Master thesis 2004)
            Aslaug Soley Bjarnadottir
        Calcula o peso dado ao estouro de capacidade  
    */
    public void calcAlpha()
    {
        double mnv = (double) Utils.demandaTotal(clientes)/capacidade;
        this.alpha = this.getBetter().fitness/((1.0/IT)*Math.pow(mnv/2 * capacidade, 2));
    }
}

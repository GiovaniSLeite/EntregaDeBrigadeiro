package algoritmoGenetico;


import Operadores.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*-----------------| Classe abstrata das funcoes |-----------------*/
public abstract class AlgoritmosGeneticos {
	
    /*-----------------| Espaco dos Atributos |-----------------*/
    //geracao -> Lista de elementos da geracao atual da populacao
    //Cada elemento devera mapear o valor x e o valor y do ponto em binario
    List<Individuo> geracao;
    List<Individuo> geracaoRoleta;
    
    //indGeracao -> Indicador da geracao em que a populacao esta
    protected int indGeracao;

    //qtdRotas = quantidade de carros
    //qtdClientes = Quantidade de clientes (incluindo o deposito)
    //capacidade = Capacidade que todos os carros t�m de carregar (em qtd de objetos)
    protected int qtdRotas;
    protected int qtdClientes;
    protected int capacidade;
    
    //Lista dos clientes com sua localizacao e demanda
    protected List<Cliente> clientes;

    //rand -> operador aleatorio
    Random rand;

    //fitnessTotal - Guarda o fitness total da geracao - para evitar recalculo
    double fitnessTotal;
    double fitnessTotalRoleta;
    
    /*-----------------| Atributos da Evolucao |-----------------*/
    //Criterio de Parada
    protected int critParada;
    protected int numGeracoes;
    private static final int CONVERGENCIA = 0;
    private static final int NUM_GERACOES = 1;

    //Numero de individuos por geracao
    protected int numIndividuos;

    //Operador de crossover
    protected Crossover crossover;

    //Numero de Crossovers que ocorrerao por ciclo evolutivo
    protected int numCross;

    //Probabilidade de ocorrer Crossover
    protected double probCrossover;

    //Operador mutacao
    protected Mutacao mutacao;

    //Probabilidade de mutacao
    protected double probMutacao;
    
    protected Correcao corretor;
    //Probabilidade de correcao
    protected double probCorrecao;
    
    protected RepairingOperator reparador;
    //Probabilidade de repairing
    protected double probReparacao;
   
    protected Otimizacao otimizador;
    //Probabilidade de otimizacao
    protected double probOtimizacao;
    //Criterio de Troca
    protected int critTroca;
    private static final int COM_SUBST = 0;
    private static final int SEM_SUBST = 1;

    //Elitismo?
    protected boolean elitismo;
    
    //Intervalo de impressao
    protected int intervaloImpressao;
    
    //Mapa de Distancias
    protected double[][] mapaDistancias;
    
    //Numero total de iteracoes
    protected static final int IT = 10000;
    protected double alpha = 10;
    
    //id da execucao
    public int ID;
    
    
    /*-----------------| Espaco dos Metodos |-----------------*/
    //Construtor -> Por enquanto so inicializa rand
    protected AlgoritmosGeneticos() {
        this.rand = new Random();
    }
    
    
    /*-----------------|
    Fitness e abstrato porque cada filho definira o seu fitness
    Recebera como entrada um genotipo e devolvera uma avaliacao
    O genotipo eh convertido em fenotipo dentro do metodo
    |-----------------*/
    protected abstract double fitness(int[] genotipo);
    protected abstract void calcAlpha();
    
    /*-----------------|
    Calcula o fitness total de toda a populacao
    |-----------------*/
    double fitnessTotal() {
        double total = 0;
        //Percorre todos os individuos da populacao
        for (Individuo ind : geracao) {
            total += ind.fitness; //Acrescenta a total o fitness do individuo vigente
        }
        return total; //Retorna o total de fitness
    }
    
    /*-----------------|
    geradorInicial -> Cria a primeira geracao baseado nos parametros
    |-----------------*/
    void geradorInicial() {
    	
        //1) Criar a primeira geracao
        this.geracao = new ArrayList(this.numIndividuos);

        //Laco que cria todos os individuos
        for (int i = 0; i < this.numIndividuos; i++) {
            //Cria um cromossomo aleatorio
            int[] gen_ind_atual = Individuo.cromossomoAleatorio(qtdRotas, qtdClientes);
            //Cria um individuo com aquele cromossomo e adiciona na geracao
            geracao.add(new Individuo(gen_ind_atual, fitness(gen_ind_atual)));
        }
    }
    
    /*-----------------|
    Cria uma copia da geracao atual adaptando o fitness dos individuos de forma que
    todos os valores sejam positivos e garantam o funcionamento da roleta
    ==> Adaptado para os problemas de roteamento, que sao apenas de minimizacao
    |-----------------*/
    void criaGeracaoRoleta()
    {
        geracaoRoleta = new ArrayList<Individuo>(); //Geracao adaptada
        double shift = 0; //O shift e para garantir que nenhum valor seja negativo
        fitnessTotalRoleta = 0; //Fitness total adaptado
        
            //Se o maior valor for positivo, como ele virara negativo ao multiplicar por -1, ele sera o shift
            if(geracao.get(geracao.size()-1).fitness > 0)
                shift = geracao.get(geracao.size()-1).fitness +1;
            
            for(Individuo ind : geracao)//Para cada individuo da geracao
            {
                //Invertendo os fitness de todos os individuos
                double novoFitness = shift+ind.fitness*-1; //Multiplicar o fitness por -1 e adicionar o shift
                geracaoRoleta.add(new Individuo(ind.getGenotipo(), novoFitness)); //Adicionar a geracaoRoleta o adaptado
                fitnessTotalRoleta += novoFitness;//Calculando o fitness total adaptado
            }

        if(fitnessTotalRoleta == 0) fitnessTotalRoleta =1; //Para nao acontecer divisao por 0
        Collections.shuffle(geracaoRoleta); //Desordena a geracao roleta para aumentar a aletoriedade
    }
    
    
    /*-----------------|
    Metodo de selecao: Roleta
    Sorteia dois  numeros i,j no intervalo [0, 100]
    Escolhe os dois individuos no qual o i e j esta em seu intervalo/fatia
    Retorna um vetor de duas posicoes, cada uma delas com um dos individuos escolhidos
    |-----------------*/
    Individuo[] roleta() {

        double g1 = (rand.nextDouble());//Numero da roleta para primeira escolha
        Individuo ind1 = null,ind2 = null;
        int escolhido = 0;
       
        for (int j = 0; g1 > 0; j++) {
        	//Enquanto o numero for maior que 0, se ele for menor ou igual, para pois esse eh o individuo escolhido
            ind1 = geracaoRoleta.get(j);
            double x =(ind1.fitness) / this.fitnessTotalRoleta; 
            g1 -= x;//Desconta a % que o fitness do individuo representa
            escolhido = j;
        }

        //Faz a mesma coisa para escolher o segundo individuo
        int escolhido2 = 0;
        do {
            g1 = (rand.nextDouble()); //Numero da roleta para segunda escolha
            for (int j = 0; g1 > 0 && j < geracao.size(); j++) {
                ind2 = geracaoRoleta.get(j);
                double x =(ind2.fitness) / this.fitnessTotalRoleta; 
                g1 -= x;
                escolhido2 = j;
            }
        } while (escolhido2 == escolhido);//Para garantir que nao escolhemos dois iguais
        
        return new Individuo[]{ind1, ind2};
    }
    
    /*-----------------|
    Criterio de parada: Convergiu
    Verifica se todos os individuos da geracao tem o mesmo fitness
    |-----------------*/
    boolean convergiu() {
        double firstFitness;
        firstFitness = this.geracao.get(0).fitness;

        //Se achou alguem com fitness diferente ao do primeiro, retorna false
        int i=0;
        for (i = 1; i < this.geracao.size(); i++) {
 
            if (this.geracao.get(i).fitness != firstFitness) {
                return false;
            }
        }
        //Se nao achou -> Convergiu - retorna true
        return true;
    }
    
    /*-----------------|
    Troca dos individuos
    Recebe a lista de filhos e retorna uma sublista dessa com os filhos melhores avaliados
    |-----------------*/
    List<Individuo> melhoresFilhos(List<Individuo> proxFilhos) {
        List<Individuo> melhoresFilhos;//Cria a lista de melhores filhos
        Collections.sort(proxFilhos); //Ordena o vetor de filhos (parametro)
            if(proxFilhos.size() < numIndividuos){
                melhoresFilhos = proxFilhos;
                melhoresFilhos.addAll(geracao.subList(0, numIndividuos - proxFilhos.size()+1));
            }
            else
                melhoresFilhos = proxFilhos.subList(0, numIndividuos);
        
        
        return melhoresFilhos;
    }
    
    /*-----------------|
    Metodo que retorna o individuo mais bem adaptado (com melhor fitness) da geracao atual
    |-----------------*/
    public Individuo getBetter() {
            return geracao.get(0);
    }
    
    
    /*-----------------|
    Esse metodo executa a evolucao baseado nos parametros fornecidos
    |-----------------*/
    protected void evolucao() {
        
        boolean convergiu = true; //Boolean para controle de convergencia
        String relatorio =""; //String a ser guardada em arquivo com o relatorio conforme especificado
        //Parametros da execucao
        relatorio = relatorio + "qtdClientes,qtdRotas,numIndividuos,critParada,numGeracoes,numCross,tipoCrossover,probCrossover,tipoMutacao,probMutacao,critTroca,elitismo\n";
        
        //Impressao dos parametros da execucao
        System.out.println("qtdClientes\tqtdRotas\tnumIndividuos\tcritParada\tnumGeracoes\tnumCross\ttipoCrossover\tprobCrossover\ttipoMutacao\tprobMutacao\tcritTroca\telitismo\n"
                +qtdClientes+"\t"+qtdRotas+"\t"+numIndividuos+"\t"+critParada+"\t"+numGeracoes+"\t"+numCross+"\t"+crossover+"\t"+probCrossover+"\t"+mutacao+"\t"+probMutacao+"\t"+critTroca+"\t"+elitismo+"\n");
        relatorio = relatorio+qtdClientes+","+qtdRotas+","+numIndividuos+","+critParada+","+numGeracoes+","+numCross+","+crossover+","
                +probCrossover+","+mutacao+","+probMutacao+","+critTroca+","+elitismo+"\n\n";
        
        //Colunas disponiveis no relatorio e na impressao -> Numero da geracao, fitness total da populacao, fitness medio, fitness maximo, fitness minimo
        relatorio = relatorio + "numGeracao,fitness da populacao: total,medio,maximo,minimo\n";
        System.out.println("numGeracao\tfitness da populacao: total\tmedio\tmaximo\tminimo");
        
        List<Individuo> proxFilhos = new ArrayList(); //Lista para guardar os proximos filhos
        
        this.geradorInicial(); //Gerar primeira populacao
        this.indGeracao = 1; //Numero da geracao
        Collections.sort(geracao);
        this.calcAlpha();
        
        
        
        // Iniciar a Evolucao
        // A Evolucao sera um while true, cujo criterio de parada definira quando sair do laco
        while (true) {
            //Ordena, em ordem crescente de fitness, os individuos
            Collections.sort(geracao);
            
            //Cria a copia adaptada da geracao que servira para a roleta
            criaGeracaoRoleta(); 
            
            //Armazenar o fitness total para evitar recalculo
            this.fitnessTotal = this.fitnessTotal(); 
            
            //Guarda no relatorio as informacoes da geracao atual/Imprime no console caso seja uma das impressoes que deva ir (baseado no parametro)
            relatorio = relatorio+ indGeracao+","+fitnessTotal+","+(fitnessTotal/geracao.size())+","+geracao.get(geracao.size()-1).fitness+","+geracao.get(0).fitness+"\n";
            if(indGeracao%intervaloImpressao==1){ System.out.println(indGeracao+"\t"+fitnessTotal+"\t"+(fitnessTotal/geracao.size())+"\t"+geracao.get(geracao.size()-1).fitness+"\t"+geracao.get(0).fitness);
            System.out.println(getBetter());}
            
            //A evolucao consiste em:
            //0) Geracao de crossovers e mutacoes: m (numCross) tentativas
            for (int contCross = 0; contCross < this.numCross; contCross++) {

                //1) Dada um probabilidade de ocorrer o crossOver
                if (this.rand.nextDouble() <= this.probCrossover) {
                    //a) Selecionar os pais
                    Individuo[] pais = roleta();

                    //b) Efetuar o cruzamento e gerar os filhos
                    int[][] filhos = crossover.executar(pais[0].getGenotipo(), pais[1].getGenotipo());

                    
                    //c) Adicionar os filhos a lista
                    for(int[] aux : filhos){
                        //Aplicacao do corretor
                        if(rand.nextDouble() < probCorrecao)
                            aux = corretor.executar(aux, qtdClientes, qtdRotas);
                        //Aplicacao do reparador
                        if(rand.nextDouble() < probReparacao)
                            aux = reparador.executar(aux, clientes, capacidade);
                        //Aplicacao do otimizador
                        if(rand.nextDouble() < probOtimizacao)
                            aux = otimizador.executar(aux);                        
                        proxFilhos.add(new Individuo(aux, fitness(aux)));
                    }
                }
            }

            //2) Dada uma probabilidade de ocorrer a mutacao
            if (this.rand.nextDouble() <= this.probMutacao && proxFilhos.size() > 0) {
               
                //Sortear um filho a ser mutado
                int indice = rand.nextInt(proxFilhos.size());
                //Efetuar a mutacao
                
                int[] mutante = mutacao.executar(proxFilhos.get(indice).getGenotipo());
                
                //Adicionar o filho mutado em seu lugar de origem
                proxFilhos.remove(indice);
                proxFilhos.add(indice, new Individuo(mutante, fitness(mutante)));
                
            }

            //3) Selecionar os melhores filhos para compor a proxima geracao
            //Aplicar o criterio de troca de populacao
            
            //Caso nao seja por substituicao, a geracao atual e acrescida aos possiveis integrantes da proxima
            if (this.critTroca == SEM_SUBST) {
                for(Individuo pai : geracao)
                    proxFilhos.add(new Individuo(pai.genotipo, fitness(pai.genotipo)));//Atualiza fitness
                
            } else //Se for aplicada troca com substituicao de populacao, verificar se vai ser aplicado elitismo
             if (this.elitismo) {
                    proxFilhos.add(this.getBetter());
                }
            
            //Nota: por uma questao simples, nao se aplica elitismo quando nao ha substituicao de populacao
            
            List<Individuo> copiaFilhos = new ArrayList<Individuo>();
            copiaFilhos.addAll(proxFilhos);
            
            //Seleciona os melhores para comporem a proxima geracao
            this.geracao = this.melhoresFilhos(copiaFilhos);
            
            //Incrementar a geracao
            this.indGeracao++;
            
            //Ordenando a geracao nova
            Collections.sort(geracao);
            
            //4) Parar a evolucao quando for a hora certa
            if (this.critParada == CONVERGENCIA) {
                //Teste de convergencia - Se a geracao convergiu, paramos a evolucao
                if (this.convergiu()) {
                    break;
                } 
                else if(indGeracao == 10000) //Se alcancar a 10000o. geracao, parar evolucao
                {
                    convergiu = false;
                    System.out.println("NAO CONVERGIU");
                    break;   
                }
            } else if (this.critParada == NUM_GERACOES) {
                //Se a geracao atual corresponder ao numero estabelecido, paramos a evolucao
                if (this.indGeracao == this.numGeracoes) {
                    break;
                }
            }
            

            //Limpar os objetos utilizados
            proxFilhos.clear();
        }
        
        //Imprimir as informacoes da ultima geracao/Guardar no relatorio
        System.out.println(indGeracao+"\t"+fitnessTotal+"\t"+(fitnessTotal/geracao.size())+"\t"+geracao.get(geracao.size()-1).fitness+"\t"+geracao.get(0).fitness);
        relatorio = relatorio+ indGeracao+","+fitnessTotal+","+(fitnessTotal/geracao.size())+","+geracao.get(geracao.size()-1).fitness+","+geracao.get(0).fitness+"\n";
        //Se nao convergiu, guardar no relatorio
        if(!convergiu) relatorio = relatorio+"NAO CONVERGIU";
        
        //Gerar o arquivo com o relatorio
        imprimirRelatorio(relatorio);
    }
    
    
    /*-----------------|
    Gera um arquivo .csv com o relatorio
    |-----------------*/
    public void imprimirRelatorio(String r)
    {
        String nome = "n"+qtdClientes+"k"+qtdRotas+"-"+ID;
        //Nome da funcao que esta sendo rodada
        
        //Cria o arquivo
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(nome+","+qtdClientes+","+qtdRotas+","+numIndividuos+","+critParada+","+numGeracoes+","+numCross+","+crossover+","
                    +probCrossover+","+mutacao+","+probMutacao+","+critTroca+","+elitismo+".csv"));
            
            w.append(r);
            w.close();
        } 
        catch (IOException ex) 
        {
        	//Escrever log de execoes
            Logger.getLogger(AlgoritmosGeneticos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Criar um mapa de distancias para evitar de ficar calculando as 
    //distancias toda hora
    public void calcularDistancias()
    {
    	int i, j;
    	this.mapaDistancias = new double[this.qtdClientes][this.qtdClientes];
    	
    	for(i = 0; i < mapaDistancias.length; i++)
    		for(j = 0; j < mapaDistancias.length; j++)
    			if(i==j)
    				this.mapaDistancias[i][j] = 0;
    			else
    				this.mapaDistancias[i][j] = this.clientes.get(i).distancia(this.clientes.get(j));
    }
       
}
package Operadores;

import java.util.ArrayList;
import java.util.List;

public class CrossoverOXadaptado extends Crossover{
	
	public CrossoverOXadaptado()
	{
		super();
	}
        
        @Override
        public String toString()
        {
            return "3";
        }

        
        /*
            Operador de cruzamento 4: Crossover OX Adaptado
                Retira os simbolos de carro (-1) do genotipo, atribui a cada carro um valor unico
                Faz o crossover OX tradicional
                Volta os carros ao padrao tradicional
        */
	public int[][] executar(int[] pai, int[] mae) {
		//1) Representar os carros de maneira diferente
		// Isso significa mudar a representacao de cada carro, de -1 pra outro valor
		// de modo que nenhum valor se repita
		int[] pai1, pai2;
		int[] filho1, filho2;
		int[][] retorno;
		int i, ind1, ind2;
		int p1, p2;
		List<Integer> inseridos1, inseridos2;
		int aux;
		
		pai1 = pai.clone();
		pai2 = mae.clone();
		
		ind1 = ind2 = -1;
		
		//A partir daqui cada carro sera unico
		for (i = 0; i < pai1.length; i++)
		{
			if(pai1[i]==-1)
				pai1[i] = ind1--;
			
			if(pai2[i]==-1)
				pai2[i] = ind2--;
		}
		//Todos os carros terao valores negativos, -1, -2, -3...
		
		//Agora faremos a implementacao classica do operador
		//Escolher os dois pontos de corte
		p1 = rand.nextInt(pai1.length-1) + 1;
		
		do{//p2 deve ser diferente de p1
			p2 = rand.nextInt(pai2.length-1) + 1;}
		while(p2 == p1);
		
		//Se p2 for menor que p1, inverter as posicoes
		if(p2 < p1)
		{
			aux = p1;
			p1 = p2;
			p2 = aux;
		}
		
		//Fazer os cortes e gerar os dois filhos
		filho1 = new int[pai1.length];
		inseridos1 = new ArrayList<Integer>();
		filho2 = new int[pai2.length];
		inseridos2 = new ArrayList<Integer>();
		
		//Copiar a parte central de cada pai para cada filho
		for(i = p1; i <= p2; i++)
		{
			filho1[i] = pai1[i];
			inseridos1.add(pai1[i]);
			filho2[i] = pai2[i];
			inseridos2.add(pai2[i]);
		}
		
		//Preencher o resto do espaco
		//Comecando da primeira posicao depois do corte
		ind1 = ind2 = p2+1;
		
		i = p2+1;
		while(true)
		{		
			//Quando terminar de passar nos pais, come�ar de novo
			if(i == pai1.length)
				i = 0;
			
			//Quando chegar ao fim, voltar para o come�o
			if(ind1==pai1.length)
				ind1 = 0;
			if(ind2==pai1.length)
				ind2 = 0;
			
			//Inserir no filho 2 os itens do pai 1 na ordem de aparecimento
			if(!inseridos2.contains(pai1[i]))
				filho2[ind2++] = pai1[i];
			
			//Inserir no filho 1 os itens do pai 2 na ordem de aparecimento
			if(!inseridos1.contains(pai2[i]))
				filho1[ind1++] = pai2[i];
			
			//Incrementar
			i++;
			
			//Verificar se comecei de novo e cheguei at� p2+1
			//Se sim, sair
			if(i==p2+1)
				break;
		}
		
		//Converter os carros novamente -> Pegar todos os carros
		//que est�o como -1, -2, -3... e transform�-los em -1 de novo
		for(i = 0; i < pai1.length; i++)
		{
			filho1[i] = filho1[i] < 0 ? -1 : filho1[i];
			filho2[i] = filho2[i] < 0 ? -1 : filho2[i];
		}
		
		//Montar o retorno dos filhos
		retorno = new int[2][pai1.length];
		retorno[0] = filho1;
		retorno[1] = filho2;
		
		return retorno;
	}
}

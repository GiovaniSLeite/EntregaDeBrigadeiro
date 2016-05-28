
package algoritmoGenetico;
public class Cliente {
    
	//Variaveis armazenadas - Posicao e demanda de cada cliente
    public int x, y, demanda;
    
    public Cliente(int cx, int cy, int demanda)
    {
        this.x = cx;
        this.y = cy;
        this.demanda = demanda;
    }
    
    //Metodo que calcula a distancia entre o cliente (instancia) e outro cliente
    public double distancia(Cliente o)
    {
        //return Math.sqrt(Math.pow((this.x - o.x), 2) + Math.pow((this.y - o.y), 2));
        return Math.round(Math.sqrt(Math.pow((this.x - o.x), 2) + Math.pow((this.y - o.y), 2)));
    }
    
    //Impressao
    public String toString()
    {
        return this.x +"\t"+this.y+"\t"+this.demanda;
    }
}

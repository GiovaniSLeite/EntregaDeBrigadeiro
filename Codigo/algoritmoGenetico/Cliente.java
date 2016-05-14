
package algoritmoGenetico;
public class Cliente {
    
    int x, y, demanda;
    
    public Cliente(int cx, int cy, int demanda)
    {
        this.x = cx;
        this.y = cy;
        this.demanda = demanda;
    }
    
    public double distancia(Cliente o)
    {
        return Math.sqrt(Math.pow((this.x - o.x), 2) + Math.pow((this.y - o.y), 2));
    }
}

package algoritmoGenetico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/*-----------------|Classe Utils|-----------------/

/-----------------|            |-----------------*/
public class Utils {

    public static ArrayList<Cliente> getClientes(String arq) {
        
        ArrayList<Cliente> clientes = new ArrayList();
        try {
            BufferedReader ler = new BufferedReader(new FileReader(new File(arq)));
            int i = 3;
            while (i > 0) {
                i--;
                ler.readLine();
            }
            int qtdClientes = Integer.valueOf(ler.readLine().split(":")[1].trim());
            i = 3;
            while (i > 0) {
                i--;
                ler.readLine();
            }
            ArrayList<Integer> x = new ArrayList();
            ArrayList<Integer> y = new ArrayList();
            for (i = 0; i < qtdClientes; i++) {
                String[] coord = ler.readLine().split("\\s+");
                x.add(Integer.valueOf(coord[1]));
                y.add(Integer.valueOf(coord[2]));
            }
            ler.readLine();

            ArrayList<Integer> demand = new ArrayList();

            for (i = 0; i < qtdClientes; i++) {
                String[] coord = ler.readLine().split("\\s+");
                demand.add(Integer.valueOf(coord[1]));
            }

            for (i = 0; i < x.size(); i++) {
                clientes.add(new Cliente(x.get(i), y.get(i), demand.get(i)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return clientes;
    }

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

}

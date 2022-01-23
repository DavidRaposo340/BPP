
import java.sql.SQLException;



public class Transfer extends Transaction {

    public long iban_quemRecebe;

	public Transfer(long iban_quemRecebe, float montante) {
		super(montante, "transfer_payment");
        this.iban_quemRecebe=iban_quemRecebe;

	}
    
    public int payment_by_transfer(long d, float am, Client quem_paga ) throws SQLException{
        
        
        //verificar se iban existem na base de dados
        Client c= new Client();
        Client quem_recebe=c.getClient_byIban(d);


        if(quem_recebe != null){
        //se exite (iban e cliente validos): 
            //chamar o transation_money
            super.transation_money(quem_paga, quem_recebe, am, this.type);
            return  1;
        }
        else{
            //se nao existir 
            //msg de erro
            return -1;
        }
           
    }
    public float getAmount() {
        return super.getAmount();
    }
    public long getIban_quemRecebe() {
        return iban_quemRecebe;
    }


}

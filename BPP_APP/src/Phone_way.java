import java.sql.SQLException;

public class Phone_way extends Transaction{

    public int phonenumber_quemRecebe;

    public Phone_way(int numeroTlm_quemRecebe, float montante) {
        super(montante, "phone_way");
        this.phonenumber_quemRecebe=numeroTlm_quemRecebe;
    }

    public int payment_by_phoneWay(int phonenumber_quemRecebe, float f, Client quem_paga) throws SQLException {
    
        //verificar se iban existem na base de dados
        Client c= new Client();
        Client quem_recebe=c.getClient_byPhonenNumber(phonenumber_quemRecebe);


        if(quem_recebe != null){
        //se exite (phonenumber e cliente validos): 
            //chamar o transation_money
            super.transation_money(quem_paga, quem_recebe, f, this.type);
            return 1;
        }
        else{
            //se nao existir 
            //client not found in the system
            return -1;
        }

    }

    public float getAmount() {
        return super.getAmount();
    }

    public int getPhoneNumber_quemRecebe() {
        return phonenumber_quemRecebe;
    }


    
}

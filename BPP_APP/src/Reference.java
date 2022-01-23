//import java.util.ArrayList;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.Database;


public class Reference extends Transaction{
    
    public int entity;
    public int reference;

    public Reference(int entity, int reference, float amount, String string) throws SQLException{ 
        //quando é criado este objeto, é inserido na db
        // isto so e premitido aos business clients!!!
        super(amount, "reference");
        this.entity=entity;
        this.reference=reference;
        generate_paymentDb(entity, reference, amount);

    }
    public Reference(int entity, int reference, float amount) {
        super(amount, "reference");
        this.entity=entity;
        this.reference=reference;
	}
	public int getEntity() {
        return entity;
    }
    public int getReference() {
        return reference;
    }
    
    public float getAmount() {
        return super.getAmount();
    }

    
    public void generate_paymentDb(int entity, int reference,float amount ) throws SQLException{
        //importar estes dados para a base
        Database d = new Database();
        Connection c = d.connectDB();

        String sql="INSERT INTO reference (entity, reference, ammount) VALUES (?,?,?)";
        PreparedStatement statement= c.prepareStatement(sql);

        statement.setInt(1, entity);   
        statement.setInt(2, reference);     
        statement.setFloat(3, amount);  

        statement.executeUpdate();

        d.disconnectDB(c);
    }

    public int payment_by_reference(int ent, int ref,float am, Client quem_paga ) throws SQLException{
        
        //verificar se entity reference amount existem na base de dados
        
        int entityFound= getEntityDB(ent, ref, am); //coloca as referencias todas da base de dados na minha lista list_ref

        if(entityFound>=0){
        //se exite (ref e am validos): 
            if(entityFound!=ent){    
               //entity incorrect
                return -1;
            }
            else{
                //procurar o client (business) que gerou a referencia (este client vai ter como parametro a entity)-> business client vai ter um metodo que me da o client a partir do int entity
                Client quem_cobra=Business_client.newClient_byEnt(ent);

                //chamar o transation_money
                super.transation_money(quem_paga, quem_cobra, am, this.type);
                return 1;
            }

        }//se nao existir 
        else{
            //entity reference amount not found in the system
            return -2;
        }

           
    }


    private int getEntityDB(int ent, int ref, float am) throws SQLException { //verifica se existe ref e ammount na base de dados e se existir ja devolve entidade

        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT entity FROM reference WHERE (reference='"+ref+"' AND ammount='"+am+"')";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            if (result.next()){
                    entity = result.getInt("entity");
                }
            else if (!result.next()) entity=-1;
        }
        catch (SQLException e){

            e.printStackTrace();

        }
        d.disconnectDB(c);
        
        return entity;
    }

   

}

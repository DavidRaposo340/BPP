import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.Database;

public class Business_client {
    
    public int entity;

     public static Client newClient_byEnt(int ent) throws SQLException{  

        Client client_id_client=new Client();
        int id_client= getId_client(ent); //vai buscar o id do cliente que vai receber

        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT * FROM client WHERE id='"+id_client+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            while (result.next()){ //so vai ser um cliente
                int id = result.getInt("id");
                long iban = result.getLong("iban");
                float total_balance = result.getFloat("total_balance");
                int card_number = result.getInt("card_number");
                int app_account = result.getInt("app_account");
                
                client_id_client = new Client(id, iban, total_balance, card_number, app_account);
                

            }
            
        }
        catch (SQLException e){

            System.out.println("error in connecting to PostgreSQL server ");
            e.printStackTrace();
            return null;

        }
        d.disconnectDB(c);
        
        return client_id_client;

    }

    public static int getId_client(int ent) throws SQLException{

        int id_client=-1;
        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT id_client FROM business_client WHERE entity='"+ent+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            if (result.next()){
                    id_client = result.getInt("id_client");
                }
            else if (!result.next()) id_client=-1;
        }
        catch (SQLException e){

            System.out.println("error in connecting to PostgreSQL server ");
            e.printStackTrace();

        }
        d.disconnectDB(c);
        
        return id_client;

    }
}

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.Database;
public class global_position {
    
    public float order_deposits;
    public float savings;
    public float savings_certificates;
    public float pprs;
    public float cryptocurrency;

    
    
    public global_position(){   

    }


    public global_position(float order_deposits, float savings, float savings_certificates, float pprs, float cryptocurrency){

        this.order_deposits=order_deposits;
        this.savings=savings;
        this.savings_certificates=savings_certificates;
        this.pprs=pprs;
        this.cryptocurrency=cryptocurrency;

    }



    public global_position getGlobalPosition_byIdClient(int id_client) throws SQLException {

        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT * FROM global_position WHERE id_client='"+id_client+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            while (result.next()){ //so vai ser um cliente 
                this.order_deposits=result.getFloat("order_deposits");
                this.savings = result.getFloat("savings");
                this.savings_certificates = result.getFloat("savings_certificates");
                this.pprs = result.getFloat("pprs");
                this.cryptocurrency = result.getFloat("cryptocurrency");

                
                global_position GP= new global_position(this.order_deposits, this.savings, this.savings_certificates, this.pprs, this.cryptocurrency);
                return GP;
            }
            
        }
        catch (SQLException e){

            System.out.println("error in connecting to PostgreSQL server ");
            e.printStackTrace();
            return null;
        }
        d.disconnectDB(c);
        return null;    
    }


    
    public float getOrder_deposits() {
        return order_deposits;
    }
    public void setOrder_deposits(float f) {
        this.order_deposits = f;
    }

    public float getSavings() {
        return savings;
    }
    public void setSavings(float f) {
        this.savings = f;
    }

    public float getSavings_certificates() {
        return savings_certificates;
    }
    public void setSavings_certificates(float f) {
        this.savings_certificates = f;
    }
    public float getPprs() {
        return pprs;
    }
    public void setPprs(float f) {
        this.pprs = f;
    }
    public float getCryptocurrency() {
        return cryptocurrency;
    }
    public void setCryptocurrency(float f) {
        this.cryptocurrency = f;
    }


    public void updateGlobal_positionDb(Client cliente, global_position gP) throws SQLException {

        Database d = new Database();
        Connection c = d.connectDB();

        String sql="UPDATE global_position SET order_deposits = ?, savings = ?, savings_certificates = ?, pprs = ?, cryptocurrency = ?  WHERE id_client = ?";
        PreparedStatement statement= c.prepareStatement(sql);

        statement.setFloat(1, gP.getOrder_deposits());   
        statement.setFloat(2, gP.getSavings());   
        statement.setFloat(3, gP.getSavings_certificates());   
        statement.setFloat(4, gP.getPprs());   
        statement.setFloat(5, gP.getCryptocurrency());   
        statement.setFloat(6, cliente.getId());   
        
        int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Dado atualizado");
            }

        d.disconnectDB(c);
        
        return;
    
    }

    
    
}

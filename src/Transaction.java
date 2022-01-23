import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.Database;

public class Transaction {
    
    
	public float amount;
    public String type;

    Client how_pay;
    Client how_charge;
    
    public Transaction(float montante, String tipo) {
        this.amount=montante;
        this.type=tipo;

    }
    public float getAmount() {
        return amount;
    }


    public void check_transaction_type(){}

    public void transation_money(Client how_pay, Client how_charge, float amount, String type2) throws SQLException{
        //client que paga fica sem dinheiro (balance-amount0)
        how_pay.setTotalBalance(how_pay.getTotalBalance()-amount);
        how_pay.getGb_ps().setOrder_deposits(how_pay.getGb_ps().getOrder_deposits()-amount);
        
        //user que recebe ganha dinheiro (balance+amount0)
        how_charge.setTotalBalance(how_charge.getTotalBalance()+amount);
        how_charge.getGb_ps().setOrder_deposits(how_charge.getGb_ps().getOrder_deposits()+amount);

        //atualizar estes valores na db
        updateBalnceDb(how_pay);
        updateDepositsDb(how_pay);

        updateBalnceDb(how_charge);
        updateDepositsDb(how_charge);
        
        setTransactionDb(how_pay, how_charge, amount, type2);
        
    }


    private void setTransactionDb(Client how_pay2, Client how_charge2, float amount2, String type2) throws SQLException {
        Database d = new Database();
        Connection c = d.connectDB();

        String sql="INSERT INTO transaction (amount, type, description) VALUES (?,?,?)";
        PreparedStatement statement= c.prepareStatement(sql);

        statement.setFloat(1, amount);   
        statement.setString(2, type2);   
        String descricao = "from cli. num.: "+how_pay2.getCardNumber()+" - to cli. num.: "+how_charge2.getCardNumber();
        statement.setString(3, descricao);  

        int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Nova transaçao inserida\n");
            }

        d.disconnectDB(c);
    }
    private void updateBalnceDb(Client cliente) throws SQLException {
        
        Database d = new Database();
        Connection c = d.connectDB();

        String sql="UPDATE client SET total_balance = ? WHERE id = ?";
        PreparedStatement statement= c.prepareStatement(sql);

        statement.setFloat(1, cliente.getTotalBalance());   
        statement.setInt(2, cliente.getId());     

        int rows = statement.executeUpdate();
            if (rows > 0){
                System.out.println("Dado atualizado");
            }

        d.disconnectDB(c);
        
        return;
    }

    private void updateDepositsDb(Client cliente) throws SQLException {

        Database d = new Database();
        Connection c = d.connectDB();

        String sql1="UPDATE global_position SET order_deposits = ? WHERE id_client = ?";
        PreparedStatement statement1= c.prepareStatement(sql1);

        statement1.setFloat(1, cliente.getGb_ps().getOrder_deposits());   
        statement1.setInt(2, cliente.getId());     

        int rows = statement1.executeUpdate();
            if (rows > 0){
                System.out.println("Dado atualizado");
            }

        d.disconnectDB(c);
        
        return;
    }

    

}

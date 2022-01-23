import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.Database;
public class Client extends User{

    
    public int id;
    public float TotalBalance;
    public long Iban;
    public int CardNumber;
    public int ClientInSystem;  //é uma flag para sabermos se este client ja tem conta ou nao nao APP
    public global_position gb_ps= new global_position();


    public Client(int id, long iban2, float total_balance, int card_number, int app_account) throws SQLException {
    
    this.id=id;
    this.Iban=iban2;
    this.TotalBalance=total_balance;
    this.CardNumber=card_number;
    this.ClientInSystem=app_account;
    this.gb_ps=gb_ps.getGlobalPosition_byIdClient(id);
    
    }
    public Client() {
    }

    public Client(int card_number) throws SQLException {

        this.CardNumber=card_number;

        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT * FROM client WHERE card_number='"+card_number+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            while (result.next()){ //so vai ser um cliente 
                this.id = result.getInt("id");
                this.Iban = result.getLong("iban");
                this.TotalBalance = result.getFloat("total_balance");
                this.ClientInSystem = result.getInt("app_account");
                this.gb_ps=gb_ps.getGlobalPosition_byIdClient(this.id);
            }
            
        }
        catch (SQLException e){

            e.printStackTrace();

        }
        d.disconnectDB(c);
    }

    public Client getClient_byIban(long iban) throws SQLException {
        

        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT * FROM client WHERE iban='"+iban+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            while (result.next()){ //so vai ser um cliente
                this.id = result.getInt("id");
                this.CardNumber=result.getInt("card_number");
                this.TotalBalance = result.getFloat("total_balance");
                this.ClientInSystem = result.getInt("app_account");

                Client cliente = new Client( this.id, iban, this.TotalBalance, this.CardNumber, this.ClientInSystem);
                return cliente;
            }
            
        }
        catch (SQLException e){

            e.printStackTrace();
            return null;
        }
        d.disconnectDB(c);
        return null;

    }

    public Client getClient_byPhonenNumber(int phonenumber_quemRecebe) throws SQLException {
        
        int idUser=getId_user_byPhonenNumber(phonenumber_quemRecebe);
        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT * FROM client WHERE id_user='"+idUser+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            while (result.next()){ //so vai ser um cliente 
                this.id = result.getInt("id");
                this.Iban = result.getLong("iban");
                this.CardNumber=result.getInt("card_number");
                this.TotalBalance = result.getFloat("total_balance");
                this.ClientInSystem = result.getInt("app_account");
                Client cliente = new Client( this.id, this.Iban , this.TotalBalance, this.CardNumber, this.ClientInSystem);
                return cliente;
            }
            
        }
        catch (SQLException e){

            e.printStackTrace();
            return null;
        }
        d.disconnectDB(c);
        return null;
    }

    public int getEntity_byClient(Client cliente) throws SQLException{
        Database d = new Database();
        Connection c = d.connectDB();
        int id_Cliente=getId();
        int entidade=0;
        try{
            String sql1="SELECT entity FROM business_client WHERE id_client='"+id_Cliente+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            result.next();
            entidade = result.getInt("entity");
            
        }
        catch (SQLException e){

            e.printStackTrace();
            return -1;
        }
        d.disconnectDB(c);
        return entidade;
    }



    private int getId_user_byPhonenNumber(int phonenumber_quemRecebe) throws SQLException {
        
        Database d = new Database();
        Connection c = d.connectDB();
        int idUser=0;
        try{
            String sql1="SELECT id FROM userprep WHERE phonenumber='"+phonenumber_quemRecebe+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            result.next();
            idUser = result.getInt("id");
            
        }
        catch (SQLException e){
            e.printStackTrace();
            return -1;
        }

        d.disconnectDB(c);
        return idUser;
    }


    public void setTotalBalance(float f) {
        TotalBalance = f;
    }
    public float getTotalBalance() {
        return TotalBalance;
    }
    public int getId() {
        return id;
    }
    public long getIban() {
        return Iban;
    }
    public int getCardNumber() {
        return CardNumber;
    }
    public global_position getGb_ps() {
        return this.gb_ps;
    }
    public int sendMoneyToDiferentPosition(Client cliente, float amount, String InicialPosition, String FinalPosition) throws SQLException {
    
        //verificar se cliente tem esse dinheiro (erro= -1)
        if ( checkSuficientMoney(cliente.getTotalBalance(), amount) == -1)
            return -1;
        
        
        global_position GP=cliente.getGb_ps();
        float initPos=0;
        float finPos=0;
        

        //descobrir quais as origens e destinos do dinheiro
        if( InicialPosition == "order_deposits" ){
            //verificar se cliente tem esse dinheiro na posiçao inicial (erro= -1)
            if ( checkSuficientMoney(GP.getOrder_deposits(), amount) == -1){
                return -1;
            }
            
            
            if( FinalPosition == "savings" ){
                //ver quanto existe no inicial position
                initPos=GP.getOrder_deposits();
                //subtrair ao anterior o amount
                GP.setOrder_deposits(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getSavings();
                //adicionar ao anterior o amount
                GP.setSavings(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "savings_certificates" ){
                //ver quanto existe no inicial position
                initPos=GP.getOrder_deposits();
                //subtrair ao anterior o amount
                GP.setOrder_deposits(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getSavings_certificates();
                //adicionar ao anterior o amount
                GP.setSavings_certificates(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "pprs" ){
                //ver quanto existe no inicial position
                initPos=GP.getOrder_deposits();
                //subtrair ao anterior o amount
                GP.setOrder_deposits(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getPprs();
                //adicionar ao anterior o amount
                GP.setPprs(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "cryptocurrency" ){
                //ver quanto existe no inicial position
                initPos=GP.getOrder_deposits();
                //subtrair ao anterior o amount
                GP.setOrder_deposits(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getCryptocurrency();
                //adicionar ao anterior o amount
                GP.setCryptocurrency(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else{
               // System.out.println("Operação Impossivel (de momento)");
                return -2;
            }
            
        }
        if( InicialPosition == "savings" ){
            //verificar se cliente tem esse dinheiro na posiçao inicial (erro= -1)
            if ( checkSuficientMoney(GP.getOrder_deposits(), amount) == -1){
                return -1;
            }
            
            
            if( FinalPosition == "order_deposits" ){
                //ver quanto existe no inicial position
                initPos=GP.getSavings();
                //subtrair ao anterior o amount
                GP.setSavings(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getOrder_deposits();
                //adicionar ao anterior o amount
                GP.setOrder_deposits(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "savings_certificates" ){
                //ver quanto existe no inicial position
                initPos=GP.getSavings();
                //subtrair ao anterior o amount
                GP.setSavings(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getSavings_certificates();
                //adicionar ao anterior o amount
                GP.setSavings_certificates(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "pprs" ){
                //ver quanto existe no inicial position
                initPos=GP.getSavings();
                //subtrair ao anterior o amount
                GP.setSavings(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getPprs();
                //adicionar ao anterior o amount
                GP.setPprs(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "cryptocurrency" ){
                //ver quanto existe no inicial position
                initPos=GP.getSavings();
                //subtrair ao anterior o amount
                GP.setSavings(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getCryptocurrency();
                //adicionar ao anterior o amount
                GP.setCryptocurrency(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else{
                //System.out.println("Operação Impossivel (de momento)");
                return -2;
            }
            
        }
        if( InicialPosition == "pprs" ){
            //verificar se cliente tem esse dinheiro na posiçao inicial (erro= -1)
            if ( checkSuficientMoney(GP.getOrder_deposits(), amount) == -1){
                return -1;
            }
            
            
            if( FinalPosition == "order_deposits" ){
                //ver quanto existe no inicial position
                initPos=GP.getPprs();
                //subtrair ao anterior o amount
                GP.setPprs(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getOrder_deposits();
                //adicionar ao anterior o amount
                GP.setOrder_deposits(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "savings_certificates" ){
                //ver quanto existe no inicial position
                initPos=GP.getPprs();
                //subtrair ao anterior o amount
                GP.setPprs(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getSavings_certificates();
                //adicionar ao anterior o amount
                GP.setSavings_certificates(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "savings" ){
                //ver quanto existe no inicial position
                initPos=GP.getPprs();
                //subtrair ao anterior o amount
                GP.setPprs(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getSavings();
                //adicionar ao anterior o amount
                GP.setSavings(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "cryptocurrency" ){
                //ver quanto existe no inicial position
                initPos=GP.getPprs();
                //subtrair ao anterior o amount
                GP.setPprs(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getCryptocurrency();
                //adicionar ao anterior o amount
                GP.setCryptocurrency(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else{
                //System.out.println("Operação Impossivel (de momento)");
                return -2;
            }
            
        }
        if( InicialPosition == "savings_certificates" ){
            //verificar se cliente tem esse dinheiro na posiçao inicial (erro= -1)
            if ( checkSuficientMoney(GP.getOrder_deposits(), amount) == -1){
                return -1;
            }
            
            
            if( FinalPosition == "order_deposits" ){
                //ver quanto existe no inicial position
                initPos=GP.getSavings_certificates();
                //subtrair ao anterior o amount
                GP.setSavings_certificates(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getOrder_deposits();
                //adicionar ao anterior o amount
                GP.setOrder_deposits(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "savings" ){
                //ver quanto existe no inicial position
                initPos=GP.getSavings_certificates();
                //subtrair ao anterior o amount
                GP.setSavings_certificates(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getSavings();
                //adicionar ao anterior o amount
                GP.setSavings(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "pprs" ){
                //ver quanto existe no inicial position
                initPos=GP.getSavings_certificates();
                //subtrair ao anterior o amount
                GP.setSavings_certificates(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getPprs();
                //adicionar ao anterior o amount
                GP.setPprs(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else if( FinalPosition == "cryptocurrency" ){
                //ver quanto existe no inicial position
                initPos=GP.getSavings_certificates();
                //subtrair ao anterior o amount
                GP.setSavings_certificates(initPos-amount);
                //ver quanto existe no final position
                finPos=GP.getCryptocurrency();
                //adicionar ao anterior o amount
                GP.setCryptocurrency(finPos+amount);
                //guardar tudo na base de dados
                GP.updateGlobal_positionDb(cliente, GP);
                return 1;
    
            }
            else{
                return -2;
            }
            
        }
        if( InicialPosition == "cryptocurrency" ){
            //verificar se cliente tem esse dinheiro na posiçao inicial (erro= -1)
            if ( checkSuficientMoney(GP.getOrder_deposits(), amount) == -1){
                return -1;
            }
            
            else{
              //Nao é possível transferir dinheiro "crypto"
                return -3;
            }
            
        }
        return 0;

    }


    private int checkSuficientMoney(float f, float amount) {
        if (f<amount){
            return -1;
        }
        return 0;
    }

    



}

import java.sql.Statement;
//import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.Database;


public class User {
    private String Name;
    private int PhoneNumber;
    private int Passcode;
    private int ContractNumber;
    private boolean Suspended;
    
    public int getContractNumber() {
        return ContractNumber;
    }
    public void setContractNumber(int contractNumber) {
        this.ContractNumber = contractNumber;
    }
    public String getName() {
        return Name;
    }
    public int getPhoneNumber() {
        return PhoneNumber;
    }

    public int getPasscode(int ContractNumber) throws SQLException {
        getPasscodeDB(ContractNumber);
        return Passcode;
    }

    private int getPasscodeDB(int ContractNumber) throws SQLException {
        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT passcode FROM userprep WHERE contractnumber='"+ContractNumber+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            if (result.next()){
                    Passcode = result.getInt("passcode");
                }
            else if (!result.next()) Passcode=-1;
        }
        catch (SQLException e){

            e.printStackTrace();

        }
        d.disconnectDB(c);
        
        return Passcode;
    }


    public void setPasscode(int passcode) {
        Passcode = passcode;
    }


	public int login(int ContractN, int Pass) throws SQLException {


        int PasscodeDB = getPasscode(ContractN);

        if(PasscodeDB==-1){
            //Invalid Contract Number
            return -1;
        }

        else if (PasscodeDB == Pass){

            boolean susp=check_susp(ContractN);

            if(susp==true) {
               //Account is suspended
                return -2;
            }

            else{
                atualizar_parametros(ContractN);
              //nlogin successfully
                return 1;
            }
        }
        
        return -3;
    }


    public boolean check_susp(int ContractNumber) throws SQLException {
        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT suspended FROM userprep WHERE contractnumber='"+ContractNumber+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            result.next();
            Suspended = result.getBoolean("suspended");
        }
        catch (SQLException e){

            e.printStackTrace();
        }
        d.disconnectDB(c);

        return Suspended;
    }


    private void atualizar_parametros(int ContractNumber) throws SQLException{
        Database d = new Database();
        Connection c = d.connectDB();
        try{
            String sql1="SELECT * FROM userprep WHERE contractnumber='"+ContractNumber+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

            while (result.next()){ 
                this.Name = result.getString("name");
                this.PhoneNumber = result.getInt("phonenumber");
                this.Passcode = result.getInt("passcode");
                this.ContractNumber = result.getInt("contractnumber");
                this.Suspended = result.getBoolean("suspended");
            }

            
        }
        catch (SQLException e){

            e.printStackTrace();

        }
        d.disconnectDB(c);
    }


    public int createAccount(int type,String Name,int PhoneNumber, int Passcode, int ConfirmPasscode, int ContractNumber) throws SQLException { //tipo de cliente=1 se business
        if (type == 0){
            int error=createAcc(0,Name,PhoneNumber,Passcode,ConfirmPasscode,ContractNumber); 
                if (error==-1){
                   // Phone Number not valid (Must have 9 digits and start with 9);
                    return error;
                } 
                else if (error==-2){
                 //   Phone Number not valid (Must have 9 digits and start with 9));
                    return error;
                } 
                else if (error==-3){
                  //Confirmation Passcode doens't correspond;
                    return error;
                }
                else if (error==-4){
                   // Bank client not found
                    return error;
                }
                else if (error==-5){
                  // Has already been created an account with this Contract Number
                    return error;
                }

        }
        else if (type == 1){
            int error=createAcc(1,Name,PhoneNumber,Passcode,ConfirmPasscode,ContractNumber); 
                if (error==-1){
                   //Phone Number not valid (Must have 9 digits and start with 9)
                    return error;
                } 
                else if (error==-2){
                  //Passcode not valid (Must have 4 numeric digits)
                    return error;
                } 
                else if (error==-3){
                 // Confirmation Passcode doens't correspond"
                    return error;
                }
                else if (error==-4){
                   //Bank client not found
                    return error;
                }
                else if (error==-5){
                   //Has already been created an account with this Contract Number
                    return error;
                }
        }
        return type; 
    }


    public int createAcc(int tipo_cliente,String Name, int PhoneNumber, int Passcode, int ConfirmPasscode, int ContractNumber) throws SQLException { //tipo de cliente=1 se business

        if ((PhoneNumber < 900000000) || (PhoneNumber > 999999999)){
            return -1;
        }

        String pass = Integer.toString(Passcode);
        Matcher condition = Pattern.compile("\\d{4}").matcher(pass);
            if(!condition.matches()){
                //ler.close();
                return -2;
            }
    
        if(!(Passcode==ConfirmPasscode)){
            return -3;
        }
        Suspended=false;

        int contractNumber=0,CardNumber=0;
        long iban_number=0;
        float balance_total=0;

        Database d = new Database();
        Connection c = d.connectDB();

        String sql1="SELECT * FROM clients_bank WHERE contractnumber='"+ ContractNumber+"' ";
        Statement statement1= c.createStatement();
        ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);

        if (!result.next()) {
           // sem data
            return -4;
        }
        else {
            do {
                contractNumber = result.getInt("contractnumber");
                CardNumber = result.getInt("card_number");
                iban_number = result.getLong("iban");
                balance_total = result.getFloat("total_balance");
            } while (result.next());
        } 
        
        if (check_acc_existence(ContractNumber)==-5) return -5; //Verificar se existe uma conta associada ao Contract Number

        String sql="INSERT INTO userprep (name, phonenumber, passcode, contractnumber, suspended, client_type) VALUES (?,?,?,?,?,?)";
        PreparedStatement statement= c.prepareStatement(sql);

        statement.setString(1, Name);   
        statement.setInt(2, PhoneNumber);     
        statement.setInt(3, Passcode);  
        statement.setInt(4, ContractNumber);
        statement.setBoolean(5, Suspended);
        statement.setInt(6, tipo_cliente+2);

        statement.executeUpdate();

        int user_id=0;    

        String sql3="SELECT id FROM userprep WHERE contractnumber='"+contractNumber+"' ";
        Statement statement3= c.createStatement();
        ResultSet result3 = ((java.sql.Statement) statement3).executeQuery(sql3);

        if (result3.next()){
                user_id = result3.getInt("id");
            }

        String sql2="INSERT INTO client (id_user, iban, total_balance, card_number) VALUES (?,?,?,?)";
        PreparedStatement statement2= c.prepareStatement(sql2);

        statement2.setInt(1, user_id);   
        statement2.setLong(2, iban_number);     
        statement2.setFloat(3, balance_total);  
        statement2.setInt(4, CardNumber);

        statement2.executeUpdate();
            
        
        if (tipo_cliente==1){

            int client_id=0;
            String sql4="SELECT id FROM client WHERE card_number='"+CardNumber+"' ";
            Statement statement4= c.createStatement();
            ResultSet result4 = ((java.sql.Statement) statement4).executeQuery(sql4);

            result4.next();
            client_id = result4.getInt("id");
            
            String sql5="INSERT INTO business_client (id_client) VALUES (?)";      
            PreparedStatement statement5= c.prepareStatement(sql5);

            statement5.setInt(1, client_id);   

            statement5.executeUpdate();

        }

        d.disconnectDB(c);

        create_globalPosition(balance_total,user_id);
        
        return 1;
	}


    public int check_acc_existence(int contractNumber2) throws SQLException {
        Database d = new Database();
        Connection c = d.connectDB();

        String sql3="SELECT id FROM userprep WHERE contractnumber='"+contractNumber2+"' ";
        Statement statement3= c.createStatement();
        ResultSet result3 = ((java.sql.Statement) statement3).executeQuery(sql3);

        if (result3.next()){
                return -5;
            }
        else return 1;
    }


    private void create_globalPosition(float balance_total, int user_id) throws SQLException {

        int client_id = getIdClient_from_IdUser(user_id);

        Database d = new Database();
        Connection c = d.connectDB();

        String sql="INSERT INTO global_position (id_client, order_deposits, savings, savings_certificates, pprs, cryptocurrency) VALUES (?,?,?,?,?,?)";
        PreparedStatement statement= c.prepareStatement(sql);

        statement.setInt(1, client_id);   
        statement.setFloat(2, balance_total);     
        statement.setFloat(3, 0);  
        statement.setFloat(4, 0);
        statement.setFloat(5, 0);
        statement.setFloat(6, 0);

        statement.executeUpdate();

        d.disconnectDB(c);
    }


    private int getIdClient_from_IdUser(int user_id) throws SQLException {
        
        Database d = new Database();
        Connection c = d.connectDB();
        
        int client_id=0;

        String sql="SELECT id FROM client WHERE id_user='"+user_id+"' ";
        Statement statement= c.createStatement();
        ResultSet result = ((java.sql.Statement) statement).executeQuery(sql);

        result.next();
        client_id = result.getInt("id");
        
        d.disconnectDB(c);

        return client_id;
    }


    public int getNumeroCartao(User user) throws SQLException {
        
        Database d = new Database();
        Connection c = d.connectDB();
        int cardNumber=0;

        try {
        String sql1="SELECT card_number FROM client WHERE id_user='"+getiduserDB(user)+"' ";
        Statement statement1= c.createStatement();
        ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);
    
        result.next();
        cardNumber = result.getInt("card_number");
        
        }
        catch (SQLException e){

            e.printStackTrace();   
        }
        d.disconnectDB(c);

        return cardNumber;
        
    }


    private int getiduserDB(User user) throws SQLException {
        Database d = new Database();
        Connection c = d.connectDB();
        int id_userprep=0;

        try {
            String sql1="SELECT id FROM userprep WHERE contractnumber='"+user.getContractNumber()+"' ";
            Statement statement1= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement1).executeQuery(sql1);
        
            result.next();
            id_userprep = result.getInt("id");

        }
        catch (SQLException e){

            e.printStackTrace();   
        }
        d.disconnectDB(c);

        return id_userprep;
    }

 
	public void logout(User utilizador) {
        utilizador=null; // tem de ser no main
	}


    public void suspend_acc(int contractnumber, boolean type) throws SQLException {
        Database d = new Database();
        Connection c = d.connectDB();

        String sql="UPDATE userprep SET suspended='"+type+"' WHERE contractnumber='"+contractnumber+"' ";
        PreparedStatement statement= c.prepareStatement(sql);

        statement.executeUpdate();
            

        d.disconnectDB(c);
    }


	public int delete_acc(int contractnumber) throws SQLException {

        int idClient=getIdClient_from_IdUser(getIdUser_from_contractNumber(contractnumber));
        boolean check_business=Check_if_business(contractnumber);

        Database d = new Database();
        Connection c = d.connectDB();

        try { 
            if(check_business){
                String sql6="DELETE FROM business_client WHERE id_client='"+idClient+"' ";
                PreparedStatement statement6= c.prepareStatement(sql6);
                statement6.executeUpdate();
            }

            String sql3="DELETE FROM global_position WHERE id_client='"+idClient+"' ";
            PreparedStatement statement3= c.prepareStatement(sql3);
            statement3.executeUpdate();

            String sql1="DELETE FROM client WHERE card_number='"+getCardNumber_contract(contractnumber)+"' ";
            PreparedStatement statement= c.prepareStatement(sql1);
            statement.executeUpdate();


            String sql2="DELETE FROM userprep WHERE contractnumber='"+contractnumber+"' ";
            PreparedStatement statement2= c.prepareStatement(sql2);
            statement2.executeUpdate();
        }
        catch (SQLException e){

            e.printStackTrace();   
        }
        d.disconnectDB(c);
        //Executado o delete;

        return -1;
	}


    public boolean Check_if_business(int contractnumber2) throws SQLException {
        
        Database d = new Database();
        Connection c = d.connectDB();

        String sql="SELECT client_type FROM userprep WHERE contractnumber='"+contractnumber2+"' ";
        Statement statement= c.createStatement();
        ResultSet result = ((java.sql.Statement) statement).executeQuery(sql);
    
        result.next();
        int client_type = result.getInt("client_type");

        if (client_type == 3) return true;
        else return false;
    }
    
    public int getCardNumber_contract(int contractnumber) throws SQLException {

        Database d = new Database();
        Connection c = d.connectDB();
        int cardNumber=0;
        int id_utilizador=0;

        try {

            String sql="SELECT id FROM userprep WHERE contractnumber='"+contractnumber+"' ";
            Statement statement= c.createStatement();
            ResultSet result = ((java.sql.Statement) statement).executeQuery(sql);
        
            result.next();
            id_utilizador = result.getInt("id");

            String sql1="SELECT card_number FROM client WHERE id_user='"+id_utilizador+"' ";
            Statement statement1= c.createStatement();
            ResultSet result1 = ((java.sql.Statement) statement1).executeQuery(sql1);
        
            result1.next();

            cardNumber = result1.getInt("card_number");
        
        }
        catch (SQLException e){

            e.printStackTrace();   
        }
        d.disconnectDB(c);

        return cardNumber;
    }
     

    private int getIdUser_from_contractNumber(int contracnumber) throws SQLException {
        
        Database d = new Database();
        Connection c = d.connectDB();
        
        int user_id=0;

        String sql="SELECT id FROM userprep WHERE contractnumber='"+contracnumber+"' ";
        Statement statement= c.createStatement();
        ResultSet result = ((java.sql.Statement) statement).executeQuery(sql);

        result.next();
        user_id = result.getInt("id");
        
        d.disconnectDB(c);

        return user_id;
    }
}
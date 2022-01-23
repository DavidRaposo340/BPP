import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;

public class UserTest {
    User user=new User();
    Client user_paga=new Client();

    @Test
    public void testDelete_acc() throws SQLException {
        user.createAccount(0,"Test_Delete",912345678,4321,4321,123456789);
        assertEquals(-1, user.delete_acc(123456789));
    }
    
    @Test
    public void testCreateAccountPositiveParticular() throws SQLException {
        assertEquals(0, user.createAccount(0,"Test_create1",912345678,4321,4321,123456789));
        user.delete_acc(123456789);
    }

    @Test
    public void testCreateAccountPositiveBusiness() throws SQLException {
        assertEquals(1, user.createAccount(1,"Test_create2",912345678,4321,4321,123456788));
        user.delete_acc(123456788);
    }

    @Test
    public void testCreateAccountNegativeParticular() throws SQLException {
        assertEquals(-1, user.createAccount(0,"Test_create3",91234567,4321,4321,123456789));  //Not having 9 numbers
        assertEquals(-1, user.createAccount(0,"Test_create4",812345678,4321,4321,123456789));  //Not strating with 9
        assertEquals(-2, user.createAccount(0,"Test_create5",912345678,43218,4321,123456789));  //Not having 4 difits
        assertEquals(-3, user.createAccount(0,"Test_create6",912345678,4321,4444,123456789));  //Error on comfirmation password
        assertEquals(-4, user.createAccount(0,"Test_create7",912345678,4321,4321,11100111));  //Client not found on Bank_Client tab in database(Isn't a bank client)
        assertEquals(-5, user.createAccount(0,"Test_create8",912345678,4321,4321,40250));  //Already created an acc with this Contract Number
    }

    @Test
    public void testCreateAccountNegativeBusiness() throws SQLException {
        assertEquals(-1, user.createAccount(1,"Test_create9",91234567,4321,4321,123456788));  //Not having 9 numbers
        assertEquals(-1, user.createAccount(1,"Test_create10",812345678,4321,4321,123456788));  //Not strating with 9
        assertEquals(-2, user.createAccount(1,"Test_create11",912345678,43218,4321,123456788));  //Not having 4 difits
        assertEquals(-3, user.createAccount(1,"Test_create12",912345678,4321,4444,123456788));  //Error on comfirmation password
        assertEquals(-4, user.createAccount(1,"Test_create13",912345678,4321,4321,11100111));  //Client not found on Bank_Client tab in database(Isn't a bank client)
        assertEquals(-5, user.createAccount(1,"Test_create14",912345678,4321,4321,4545456));  //Already created an acc with this Contract Number
    }

    @Test
    public void testLoginNegative() throws SQLException {
        assertEquals("Invalid Contract Number",-3, user.login(40250,3333)); 
        assertEquals("Account is suspended!",-1,user.login(90009,1234));
        assertEquals("Invalid passcode inserted",-2, user.login(1235754,3333));
    }

    @Test
    public void testLoginPositive() throws SQLException {
        assertEquals(1, user.login(40250,1234));
    }

    @Test
    public void testCheck_susp() throws SQLException {
        assertEquals(true, user.check_susp(1235754)); //Verify if an suspended client is suspended 
        assertEquals(false, user.check_susp(1212123));//verify if an unsuspended client is unsuspended
    }

    @Test
    public void testSuspend_acc() throws SQLException {
        assertEquals(true, user.check_susp(1235754));  //Check fisrt id suspended
        user.suspend_acc(1235754,false);                //Unsuspend the account
        assertEquals(false, user.check_susp(1235754));  // Check if it is unsuspended
        user.suspend_acc(1235754,true);                 //Suspend the account again
        assertEquals(true, user.check_susp(1235754));   //Check if it is suspended
    }

    @Test
    public void testPayment_by_ref_positive() throws SQLException {
        user.login(40250,1234);
        Reference Ref = new Reference(10500,111200300,100);
        assertEquals(1, Ref.payment_by_reference(10500,111200300,200, user_paga ));
    }

    @Test
    public void testPayment_by_ref_negative1() throws SQLException {//Ref nao existente
        user.login(40250,1234);
        Reference Ref = new Reference(10500,111200300,100);
        assertEquals(-2, Ref.payment_by_reference(10500,1,200, user_paga ));
    }

    @Test
    public void testPayment_by_ref_negative2() throws SQLException { //entity nao existente
        user.login(40250,1234);
        Reference Ref = new Reference(10500,111200300,100);
        assertEquals(-1, Ref.payment_by_reference(1,111200300,200, user_paga ));
    }

    @Test
    public void testtransfer_positive() throws SQLException {
        user.login(40250,1234);
        Transfer tras= new Transfer(120220320, 5);
        assertEquals(1, tras.payment_by_transfer(tras.getIban_quemRecebe(), tras.getAmount(), user_paga));
    }

    @Test
    public void testtransfer_negative() throws SQLException {
        user.login(40250,1234);
        Transfer tras= new Transfer(120220320, 5);
        assertEquals(-1, tras.payment_by_transfer(1, tras.getAmount(), user_paga));
    }

    @Test
    public void testPhoneway_positive() throws SQLException {
        user.login(40250,1234);
        Phone_way tlm_way= new Phone_way(979979979, 10);
        assertEquals(1, tlm_way.payment_by_phoneWay(tlm_way.getPhoneNumber_quemRecebe(), tlm_way.getAmount(), user_paga));
    }

    @Test
    public void testPhoneway_negative() throws SQLException {
        user.login(40250,1234);
        Phone_way tlm_way= new Phone_way(979979979, 10);
        assertEquals(-1, tlm_way.payment_by_phoneWay(900000001, tlm_way.getAmount(), user_paga));
    }

    @Test
    public void testSendMoneyDIferent_Positions() throws SQLException {
        User utilizador=new User();
        utilizador.login(99999,1234);
        Client troca_position = new Client(utilizador.getNumeroCartao(utilizador));

        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100, "order_deposits", "savings"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100,  "savings", "order_deposits"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100, "order_deposits", "savings_certificates"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100, "savings_certificates", "order_deposits"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100, "order_deposits", "pprs"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100, "pprs", "order_deposits"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 1, "order_deposits", "cryptocurrency"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100,  "savings", "savings_certificates"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100,  "savings_certificates", "savings"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100,  "savings", "pprs"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100,  "pprs", "savings"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 1,  "savings", "cryptocurrency"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100,  "pprs", "savings_certificates"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100,  "savings_certificates","pprs"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 100,  "pprs", "cryptocurrency"));
        assertEquals(1, troca_position.sendMoneyToDiferentPosition(troca_position, 1,  "savings_certificates", "cryptocurrency"));
    }
    @Test
    public void testSendMoneyDIferent_Positions_Nehgative() throws SQLException {
        User utilizador=new User();
        utilizador.login(99999,1234);
        Client troca_position = new Client(utilizador.getNumeroCartao(utilizador));

        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000, "order_deposits", "savings"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000,  "savings", "order_deposits"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000, "order_deposits", "savings_certificates"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000, "savings_certificates", "order_deposits"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000, "order_deposits", "pprs"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000, "pprs", "order_deposits"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000,  "savings", "savings_certificates"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000,  "savings_certificates", "savings"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000,  "savings", "pprs"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000,  "pprs", "savings"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000,  "pprs", "savings_certificates"));
        assertEquals(-1, troca_position.sendMoneyToDiferentPosition(troca_position, 10000000,  "savings_certificates","pprs"));
        assertEquals(-3, troca_position.sendMoneyToDiferentPosition(troca_position, 1,  "cryptocurrency", "pprs"));
        assertEquals(-3, troca_position.sendMoneyToDiferentPosition(troca_position, 1,  "cryptocurrency","savings_certificates"));
        assertEquals(-3, troca_position.sendMoneyToDiferentPosition(troca_position, 1,  "cryptocurrency","savings"));
        assertEquals(-3, troca_position.sendMoneyToDiferentPosition(troca_position, 1, "cryptocurrency","order_deposits"));
        assertEquals(-2, troca_position.sendMoneyToDiferentPosition(troca_position, 10, "order_deposits", "Erro"));
        assertEquals(-2, troca_position.sendMoneyToDiferentPosition(troca_position, 10,  "savings", "Erro"));
        assertEquals(-2, troca_position.sendMoneyToDiferentPosition(troca_position, 10, "pprs", "Erro"));
        assertEquals(-2, troca_position.sendMoneyToDiferentPosition(troca_position, 10,  "savings_certificates","Erro"));
        
    }

    
}
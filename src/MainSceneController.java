import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class MainSceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String contractnumber_i;
    public static String passcode_i;
    private int c_number;
    private int pass;
    
    @FXML
    private PasswordField pfPasscode;

    @FXML
    private TextField tfContractNumber;
    
    @FXML
    private Button okMenu;

    @FXML
    private Pane iban;

    @FXML
    private TextArea taInformationiban;

    @FXML
    private TextField tfAmount;

    @FXML
    private TextField tfEntity;

    @FXML
    private TextField tfReference;

    @FXML
    private TextField tfcardnumber;

    @FXML
    private TextField tfibanumberdestination;

    @FXML
    private TextField tfnameoncard;

    @FXML
    private PasswordField pfPassocode_c;

    @FXML
    private TextField tfContractNumber_c;

    @FXML
    private TextField tfcardnumber_c;

    @FXML
    private TextField tfibanumberdestination_c;

    @FXML
    private TextField tfnameoncard_c;

    @FXML
    private TextField tfAmountPhoneWay;

    @FXML
    private TextField tfPhoneNumberPhoneway;


    @FXML
    private PasswordField pfPasscodecreationaccount;

    @FXML
    private TextField tfFirstname;

    @FXML
    private TextField tfPhonenumbercreationaccount;

    @FXML
    private Label labeldone;

    @FXML
    private Label labelinformationiban;
    @FXML
    private Label label_type;
   

    @FXML
    void changepage(ActionEvent event, String nameofpage) throws IOException{
        root=FXMLLoader.load(getClass().getResource(nameofpage));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void nextClicked(ActionEvent event) throws IOException {
        String secondpagename="secondpage.fxml";
        changepage(event, secondpagename);
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
    }
    
    @FXML
    void createaccount(ActionEvent event) throws IOException {
        String pagename="createaccount.fxml";
        changepage(event, pagename);
    }

    @FXML
    void confirmcrreationaccount(ActionEvent event) {

    }

    @FXML
    void tocompanylogin(ActionEvent event) throws IOException {
        String pagename="logincompany.fxml";
        changepage(event, pagename);
    }
       
    @FXML
    void toindividualogin(ActionEvent event) throws IOException { 
        String pagename="loginindividual.fxml";
        changepage(event, pagename);      
    }
  
    @FXML
    void tomenuindividual(ActionEvent event) throws IOException, SQLException{
        contractnumber_i=tfContractNumber.getText();
        c_number=Integer.valueOf(contractnumber_i);
        passcode_i=pfPasscode.getText();
        pass=Integer.valueOf(passcode_i);
        User u = new User();
        if(u.login(c_number, pass)==-1){ //invalid contract number
            String pagename="errorcontractnumber.fxml";
            changepage(event, pagename);
            stage.setUserData(u);
        }
        else if(u.login(c_number, pass)==-2){ //acount suspended
            String pagename="errorsuspendedaccount.fxml";
            changepage(event, pagename);
            stage.setUserData(u);
        }
        else if(u.login(c_number, pass)==-3){ //invalid passcode
            String pagename="errorpass.fxml";
            changepage(event, pagename);
            stage.setUserData(u);
        }
       
       else if(u.Check_if_business(c_number)){ //company
        System.out.println("ola"+u.Check_if_business(c_number));
           if(u.login(c_number,pass)==1){ 
                label_type.setText("You are a company, please click below");           
            }
        }
        else{
            String pagename="menuindividual.fxml";
            changepage(event, pagename);
            stage.setUserData(u);
        }
                  
    }


    @FXML
    void tomenucompany(ActionEvent event) throws IOException, SQLException {
        String contractnumber_c = tfContractNumber_c.getText();
        c_number=Integer.valueOf(contractnumber_c);
        String passcode_c = pfPassocode_c.getText();
        pass=Integer.valueOf(passcode_c);
        User u = new User();
     
       if(u.login(c_number, pass)==-1){ //invalid contract number
            String pagename="errorcontractnumber.fxml";
            changepage(event, pagename);
        }
        else if(u.login(c_number, pass)==-2){ //acount suspended
            String pagename="errorsuspendedaccount.fxml";
            changepage(event, pagename);
        }
        else if(u.login(c_number, pass)==-3){ //invalid passcode
            String pagename="errorpass.fxml";
            changepage(event, pagename);
        }
       else if((u.Check_if_business(c_number))){ //company
            if(u.login(c_number,pass)==1){ 
                String pagename="menucompany.fxml";
                changepage(event, pagename);
                stage.setUserData(u);
            }
        }
        else{
            label_type.setText("You are an individual, please click below");
        }
    }

    @FXML
    void generatereference(ActionEvent event) throws IOException {
        String pagename="referencebusiness.fxml";
        changepage(event, pagename);
    }
    @FXML
    void checktotalbalance_c(ActionEvent event) {

    }

    @FXML
    void doIBANconsult_c(ActionEvent event) {

    }

    @FXML
    void doIBANtransference_c(ActionEvent event) throws IOException {
        String pagename="ibantransference_c.fxml";
        changepage(event, pagename);
    }

    @FXML
    void confirmibantransference_c(ActionEvent event) {

    }

    @FXML
    void gotomenu(ActionEvent event) throws IOException, SQLException { //quando ja esteve nas outras paginas 
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
    
        if(!(u.Check_if_business(u.getContractNumber()))){ //company
            String pagename="menuindividual.fxml";
            changepage(event, pagename);
        }
        else{
            String pagename="menucompany.fxml";
            changepage(event, pagename);
        }
    }
            
    @FXML
    void checktotalbalance_i(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("totalbalance.fxml"));
        Parent root = loader.load();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
        User u=new User();
        u=(User) stage.getUserData();
        Client clientelogado=new Client(u.getNumeroCartao(u));
        System.out.println("check the total balance/money in my account: "+clientelogado.getTotalBalance());
    }


    @FXML
    void doIBANconsult_i(ActionEvent event) throws IOException {
        String pagename="ibanconsult.fxml";
        changepage(event, pagename);
    }

    @FXML
    void showiban(ActionEvent event) throws SQLException { 
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
        
        Client clientelogado=new Client(u.getNumeroCartao(u));
        long iban=clientelogado.getIban();
        String iban_s=Long.toString(iban);
        labelinformationiban.setText(iban_s); 
    }

    @FXML
    void doIBANtransference_i(ActionEvent event) throws IOException {
        String pagename="ibantransference.fxml";
        changepage(event, pagename);
    }

    @FXML
    void payaservice_i(ActionEvent event) throws IOException {
        String pagename="payaservice.fxml";
        changepage(event, pagename);
    }

    @FXML
    void confirmpayservice_i(ActionEvent event) throws SQLException { //coloquei prints e le bem o que se escreve nas caixinhas
        int ret;
        int entity=0;
        int reference=0;
        int amount=0;
       
        if(tfEntity.getText().trim().isEmpty() || tfReference.getText().trim().isEmpty() || tfAmount.getText().trim().isEmpty()){
            labeldone.setText("Please fill all spaces.");
        }
        else{
            String entity_s=tfEntity.getText();
            String reference_s=tfReference.getText();
            String amount_s=tfAmount.getText();
            entity=Integer.valueOf(entity_s);    
            reference=Integer.valueOf(reference_s);           
            amount=Integer.valueOf(amount_s);
        }
      

        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
        Client o_que_paga = new Client(u.getNumeroCartao(u));
        Reference Ref=new Reference(entity, reference, amount);
        ret=Ref.payment_by_reference(Ref.getEntity(), Ref.getReference(), Ref.getAmount(), o_que_paga);
        if(ret==-1){
            labeldone.setText("Entity Incorrect");
        }
        //teste feito com entity, reference, amount=10500,111200300,200
        else if(ret==1){
            labeldone.setText("DONE!");
        }
        else{
            labeldone.setText("Entity/Reference/Amount not found in the system");
          
        }
    
    }
 
      @FXML
    void phoneway_i(ActionEvent event) throws IOException {
        String pagename="phoneway_i.fxml";
        changepage(event, pagename);
    }
    @FXML
    void confirmphoneway(ActionEvent event) throws SQLException {
        int r=0;
        int phonenumber=0;
        int amountpw=0;
        String amount_s="";
        String phone_s=tfPhoneNumberPhoneway.getText();
        if(tfPhoneNumberPhoneway.getText().trim().isEmpty() || tfAmountPhoneWay.getText().trim().isEmpty()){
            labeldone.setText("Please fill all spaces");
        }
        else{
            phonenumber=Integer.valueOf(phone_s);  
            amount_s=tfAmountPhoneWay.getText();
            amountpw=Integer.valueOf(amount_s);
        }
    
      

        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
        Client o_que_paga = new Client(u.getNumeroCartao(u));
         
        Phone_way tlm_way= new Phone_way(phonenumber, amountpw);
        r=tlm_way.payment_by_phoneWay(tlm_way.getPhoneNumber_quemRecebe(), tlm_way.getAmount(), o_que_paga);
        if(r==-1){
            labeldone.setText("Client not found in the system");
        }
        else{
            labeldone.setText("DONE!");
        }

    }

    
@FXML
    void globalpositionsoptions_i(ActionEvent event) throws IOException {
        String pagename="globalpositions_choice.fxml";
        changepage(event, pagename);
    }
     
    @FXML
    void gotoglobalpositionsoverview(ActionEvent event) throws IOException {
        String pagename="globalpositionsoverview.fxml";
        changepage(event, pagename);
    }

    @FXML
    void applogout(ActionEvent event) throws IOException {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u.logout(u);
        String pagename="logoutpage.fxml";
        changepage(event, pagename);
        
    }
    
    @FXML
    void suspendaccount(ActionEvent event) throws SQLException, IOException {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
        u.suspend_acc(u.getContractNumber(), true);
        String pagename="errorsuspendedaccount.fxml";
        changepage(event, pagename);
    }


}

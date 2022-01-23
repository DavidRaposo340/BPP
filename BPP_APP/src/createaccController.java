import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class createaccController {
    @FXML
    private Stage stage;
    private Parent root;
    private Scene scene;
    @FXML
    private ChoiceBox<String> cbBusinessorprivate;
    private String[] choices={"private", "business"};

    @FXML
    private PasswordField pfPasscodecreationaccount;

    @FXML
    private PasswordField pfPasscodecreationaccount_conf;

    @FXML
    private TextField tfFirstname;

    @FXML
    private TextField tfPhonenumbercreationaccount;

    @FXML
    private TextField tfcontractnum_creat;

    @FXML
    private Label lsucesso;

 

    @FXML
    void changetoerrorpage(ActionEvent event, String nameofpage) throws IOException{
          
        root=FXMLLoader.load(getClass().getResource(nameofpage));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    void confirmcrreationaccount(ActionEvent event) throws SQLException, IOException {
        int type_cli=3;
        String name="";
        int PhoneNumber=0;
        String passcode;
        int Passcode=0;
        String passcode_confirm;
        int ConfirmPasscode=0;
        String contractnum;
        int ContractNumber=0;
    
        if(getchoice(event).equals("private")){
            type_cli=0;
        }
        else if(getchoice(event).equals("business")){
            type_cli=1;
        }
        String phonenumber=tfPhonenumbercreationaccount.getText();
        if(tfFirstname.getText().trim().isEmpty() || tfPhonenumbercreationaccount.getText().trim().isEmpty() || pfPasscodecreationaccount.getText().trim().isEmpty() || pfPasscodecreationaccount_conf.getText().trim().isEmpty() || tfcontractnum_creat.getText().trim().isEmpty() ){
            lsucesso.setText("Please fill all the spaces");
        }
      
        else{
            name=tfFirstname.getText();
            PhoneNumber=Integer.valueOf(phonenumber);
            passcode=pfPasscodecreationaccount.getText();
            Passcode=Integer.valueOf(passcode);
            passcode_confirm=pfPasscodecreationaccount_conf.getText();
            ConfirmPasscode=Integer.valueOf(passcode_confirm);
            contractnum=tfcontractnum_creat.getText();
            ContractNumber=Integer.valueOf(contractnum);
        }
        
  
        User u=new User();
        int ret=u.createAcc(type_cli, name, PhoneNumber, Passcode, ConfirmPasscode, ContractNumber);
        if(ret==-1){
           changetoerrorpage(event, "errorcreationacc.fxml");
        }
        else if(ret==-2){
            changetoerrorpage(event, "errorpasscodecreate.fxml");
           
        }
        else if(ret==-3){
            changetoerrorpage(event, "errorconfirmpasscode.fxml");
        }
        else if(ret==-4){
            changetoerrorpage(event, "bankclientnotfound.fxml");
          
        }
        else if(ret==-5){
            changetoerrorpage(event, "repeatedcn_creation.fxml");
        }
        else{
            lsucesso.setText("Your account is created!");
            root=FXMLLoader.load(getClass().getResource("loginindividual.fxml"));
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }
    
    public String getchoice(ActionEvent event){
        String typeacc=cbBusinessorprivate.getValue();
        return typeacc;
    }

    public void initialize() {
        cbBusinessorprivate.getItems().addAll(choices);
        cbBusinessorprivate.setOnAction(this::getchoice);

    }

}
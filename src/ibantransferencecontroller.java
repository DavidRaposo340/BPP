import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class ibantransferencecontroller {
    @FXML
    private Label labelcardnumber;

    @FXML
    private Label labelnameoncard;

    @FXML
    private TextField tfamountibantransf;

    @FXML
    private TextField tfibanumberdestination;
  

    private Stage stage;
    private Parent root;
    private Scene scene;
    @FXML
    private Label labeldone;

    @FXML
    void confirmibantransference(ActionEvent event) throws SQLException {
        long iban_d=0;
        String iban_s;
        String amount_s="";
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();

        
        Client o_que_paga=new Client(u.getNumeroCartao(u));
        if(tfibanumberdestination.getText().trim().isEmpty() || tfamountibantransf.getText().trim().isEmpty() ){
            labeldone.setText("Please fill all the spaces");
        }
        else{
            iban_s=tfibanumberdestination.getText();
            iban_d=Long.valueOf(iban_s);
            amount_s=tfamountibantransf.getText();
            float montante=Float.valueOf(amount_s);        
            Transfer tras= new Transfer(iban_d, montante);
            if(tras.payment_by_transfer(tras.getIban_quemRecebe(), tras.getAmount(), o_que_paga)==-1){
                labeldone.setText("Client not found in the system!");
            }
            else{
                labeldone.setText("DONE!");
            }
        }     
    }

    @FXML
    void gotomenu(ActionEvent event) throws IOException, SQLException {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
    
        if(!(u.Check_if_business(u.getContractNumber()))){ //company
            String pagename="menuindividual.fxml";
            root=FXMLLoader.load(getClass().getResource(pagename));
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            String pagename="menucompany.fxml";
            root=FXMLLoader.load(getClass().getResource(pagename));
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void shownameandcard(ActionEvent event) throws IOException, SQLException {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
      
        
        Client clientelogado=new Client(u.getNumeroCartao(u));
        String name=u.getName();
        int cardnumber=clientelogado.getCardNumber();
        String cardnumber_s=Integer.toString(cardnumber);
        labelnameoncard.setText(name);
        labelcardnumber.setText(cardnumber_s);
        
    }

 

}

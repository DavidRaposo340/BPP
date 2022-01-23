import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class choicegbcontroller {
  
    @FXML
    private ChoiceBox<String> choicebox;
    private String[] choices={"order_deposits", "savings", "savings_certificates", "pprs", "cryptocurrency"};

    @FXML
    private ChoiceBox<String> choicebox2;

    private Stage stage;
    private Parent root;
    private Scene scene;

    @FXML
    private TextField tfAmountgp;

    @FXML
    private Label labeldone;

    @FXML
    void changepositions(ActionEvent event) throws SQLException {
        String amount_gp=tfAmountgp.getText();
        if(tfAmountgp.getText().trim().isEmpty()){
            labeldone.setText("Please fill the amount");
        }
        int amount_gp_int=Integer.valueOf(amount_gp);
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
        Client o_que_paga = new Client(u.getNumeroCartao(u));
        
        int r=o_que_paga.sendMoneyToDiferentPosition(o_que_paga, amount_gp_int, getchoice(event), getchoice2(event));
        if(r==-1){
            labeldone.setText("You don't have that amount of money.");
        }
        else if(r==-2){
            labeldone.setText("Impossible operation at the moment");
        }
        else{
            labeldone.setText("DONE!");
        }

    }
 

    @FXML
    void gotoglobalpositionsoverview(ActionEvent event) throws IOException {
        root=FXMLLoader.load(getClass().getResource("globalpositionsoverview.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    public String getchoice(ActionEvent event){
        String choice=choicebox.getValue();
        return choice;
    }

    public String getchoice2(ActionEvent event){
        String choice2=choicebox2.getValue();
        return choice2;
    }

    public void initialize() {
        choicebox.getItems().addAll(choices);
        choicebox.setOnAction(this::getchoice);

        choicebox2.getItems().addAll(choices);
        choicebox2.setOnAction(this::getchoice2);
    }

  
}

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class reference_controller {
    private Stage stage;
    private Parent root;
    private Scene scene;
    @FXML
    private Label labeldone;
 
    @FXML
    private Label identifier_ref;

    @FXML
    private Label ref_gen;

    @FXML
    private TextField tfamount_ref;
    @FXML
    private Label entity_ref;


    @FXML
    private Label labelamount_ref;

    @FXML
    private Label labelref;



    @FXML
    private TextField tfreference_ref;

  
    
    @FXML
    void gotomenu(ActionEvent event) throws SQLException, IOException {
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
    void confirmamount_ref(ActionEvent event) throws SQLException {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();  
        Client clientelogado=new Client(u.getNumeroCartao(u));
        String amount_s=tfamount_ref.getText();
        String reference_s=tfreference_ref.getText();
        int entity=clientelogado.getEntity_byClient(clientelogado);
        String entity_s=Integer.toString(entity);
        entity_ref.setText(entity_s);
        labelref.setText(reference_s);
        labelamount_ref.setText(amount_s);
      
    }

}


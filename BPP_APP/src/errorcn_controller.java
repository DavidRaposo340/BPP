import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

public class errorcn_controller {

  

    @FXML
    private Label labelerrocn;

    private Stage stage;
    private Parent root;
 

    @FXML
    void tocompanylogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("logincompany.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void toindividualogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("loginindividual.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(){
        labelerrocn.setText("You inserted an invalid contract number!");
    }

}
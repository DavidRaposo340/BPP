import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;


import javafx.stage.Stage;
public class balanceController {

  
    private Stage stage;
    private Parent root;
    private Scene scene;
 

    @FXML
    private Label label_Informationbalance;

    @FXML
    public void gotomenu(ActionEvent event) throws IOException, SQLException {
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
    public void showagaintotalbalance(ActionEvent event) throws SQLException, IOException {
        root=FXMLLoader.load(getClass().getResource("totalbalance.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
      
       System.out.println("total balance show"+u.getContractNumber());
        
        Client clientelogado=new Client(u.getNumeroCartao(u));
        float totalbalance=clientelogado.getTotalBalance();
        String totalbalance_s=Float.toString(totalbalance);
        
        label_Informationbalance.setText(totalbalance_s); //aqui em vez de termos o numero, vamos coloca
    }

  

}

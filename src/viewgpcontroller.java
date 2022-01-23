import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class viewgpcontroller {
    @FXML
    private PieChart globalpie;
    
    @FXML
    private TextField tfcryptocurrency;

    @FXML
    private TextField tforderdeposits;

    @FXML
    private TextField tfpprs;

    @FXML
    private TextField tfsavingcertificates;

    @FXML
    private TextField tfsavings;
      
    private Stage stage;
    private Parent root;
    private Scene scene;

  

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
    void showglobalpositions(ActionEvent event) throws SQLException {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
        Client o_que_paga = new Client(u.getNumeroCartao(u));

        float od=o_que_paga.getGb_ps().getOrder_deposits();
        String od_s=Float.toString(od);
        tforderdeposits.appendText(od_s);

        float sav=o_que_paga.getGb_ps().getSavings();
        String sav_s=Float.toString(sav);
        tfsavings.appendText(sav_s);

        float pprs=o_que_paga.getGb_ps().getPprs();
        String pprs_s=Float.toString(pprs);
        tfpprs.appendText(pprs_s);

        float savcert=o_que_paga.getGb_ps().getSavings_certificates();
        String savcert_s=Float.toString(savcert);
        tfsavingcertificates.appendText(savcert_s);

        float crypto=o_que_paga.getGb_ps().getCryptocurrency();
        String crypto_s=Float.toString(crypto);
        tfcryptocurrency.appendText(crypto_s);

    }
    
    @FXML
    void showpiechart(ActionEvent event) throws SQLException {
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        User u=new User();
        u=(User) stage.getUserData();
        Client o_que_paga = new Client(u.getNumeroCartao(u));
        float od=o_que_paga.getGb_ps().getOrder_deposits();
        float sav=o_que_paga.getGb_ps().getSavings();
        float pprs=o_que_paga.getGb_ps().getPprs();
        float savcert=o_que_paga.getGb_ps().getSavings_certificates();
        float crypto=o_que_paga.getGb_ps().getCryptocurrency();
        ObservableList<PieChart.Data> pcd =
        FXCollections.observableArrayList(
            new PieChart.Data("orderdeposits", od),
            new PieChart.Data("savings", sav),
            new PieChart.Data("pprs", pprs),
            new PieChart.Data("certificates", savcert),
            new PieChart.Data("crypto", crypto)
            );
              globalpie.setData(pcd);
      
        
    }

  

    
   
}

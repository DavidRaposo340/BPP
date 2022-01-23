import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
         
            Parent root = FXMLLoader.load(getClass().getResource("firstpage.fxml"));
            Scene scene = new Scene(root);
            Image imagem = new Image("/imagens/BPP.PNG");
            stage.getIcons().add(imagem);
            stage.setTitle("BPP");
            stage.setScene(scene);
            stage.show();
         
           } catch(Exception e) {
            e.printStackTrace();
           }
        
     
        
    }
    public static void main(String[] args) {
        launch(args);
    }

   
    
}

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NewFXMainClass extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainFXML.fxml"));
            Parent root = loader.load();

            AplikasitokoController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            Scene scene = new Scene(root);

            primaryStage.setTitle("Aplikasi Admin Toko ONLINE by Adryan ft. Ridho");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

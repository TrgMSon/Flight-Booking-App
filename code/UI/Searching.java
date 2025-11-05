package UI;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.util.Objects;

public class Searching extends Application {
    private static Stage searchingStage;

    public static Stage getSearchingStage() {
        return searchingStage;
    }

    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/views/flight-search-view.fxml"));
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Image icon = new Image(
                Objects.requireNonNull(getClass().getResource("/UI/resources/search_icon.png")).toExternalForm());

        scene.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
        stage.getIcons().add(icon);

        stage.setTitle("Tìm chuyến bay");
        stage.setScene(scene);
        stage.setResizable(false);
        searchingStage = stage;
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

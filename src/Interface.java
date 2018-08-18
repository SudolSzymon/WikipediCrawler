import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Interface extends Application {
    private Engine engine;
    public static void main(String[] args) {
        launch(args);
    }
    @FXML
    private VBox linkList;
    @FXML
    private TextField input;
    @FXML
    private Button find;
    @FXML
    private Button checkButton;
    @FXML
    private TextField status;
    @FXML
    private Button openButton;
    @FXML
    public void initialize(){
        engine=new Engine(this);
        System.out.println(find);
        find.setOnAction(e->engine.check(input.getText()));
        checkButton.setOnAction(e->engine.displayResults());
        openButton.setOnAction(e->engine.openAll());
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();

    }
    void addLink(String Url){
        Hyperlink link=new Hyperlink(Url);
        link.setOnAction(e->{try {
            Desktop.getDesktop().browse(new URL(Url).toURI());
        } catch (IOException | URISyntaxException exeption) {
            exeption.printStackTrace();
        }
        });
        linkList.getChildren().add(link);
    }
    void statusUpdate(boolean isFinished, int nResultsFound){
        status.setText(isFinished?"Downloading finished found "+nResultsFound:"Still downloading found "+nResultsFound);
    }

    void clearOutput() {
        linkList.getChildren().clear();
    }
}
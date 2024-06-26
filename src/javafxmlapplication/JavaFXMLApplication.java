/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;
import static org.sqlite.SQLiteJDBCLoader.initialize;


public class JavaFXMLApplication extends Application {
    public static Stage stage;
    private static Acount acount;
    private static User user;
    
    @Override
    public void start(Stage stage) throws Exception {
        //======================================================================
        // 1- creación del grafo de escena a partir del fichero FXML
        this.stage=stage;
        
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        //======================================================================
        // 2- creación de la escena con el nodo raiz del grafo de escena
        Scene scene = new Scene(root);
        //======================================================================
        // 3- asiganación de la escena al Stage que recibe el metodo 
        //     - configuracion del stage
        //     - se muestra el stage de manera no modal mediante el metodo show()
        stage.setScene(scene);
        stage.setTitle("start PROJECT - IPC:");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws AcountDAOException, IOException, Exception {
        initialize();
        launch(args);
        
    }
    
     public static void setScene(Scene scene){

        stage.setScene(scene);
        stage.show();

    }
    
    public static void setRoot(Parent root){

        stage.getScene().setRoot(root);
        stage.show();

    }
    
    public static void initialize() throws AcountDAOException, IOException {
        System.out.println("JavaFXMLApplication.initialize()");
        acount = Acount.getInstance();
    }
    
    public static Stage getStage(){
        return stage;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        JavaFXMLApplication.user = user;

    }

    public static Acount getAcount() {
        return acount;
    }


    
}

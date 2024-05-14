/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import inicio.VistaInicioController;
import java.io.IOException;
import static java.lang.Boolean.TRUE;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Acount;
import model.AcountDAO;
import model.User;
import registro.VistaRegistroController;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {
    private Label labelMessage;
    @FXML
    private TextField UserField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Button Inicio;
    @FXML
    private Button Registro;
    private BooleanProperty validUser;
    @FXML
    private Label errorInicio;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        //Si el campo de usario != "", al presionar "ENTER", pasamos al campo de PASSWORD
        UserField.setOnKeyPressed(e -> {
            if(!UserField.getText().equals("") && e.getCode() == KeyCode.ENTER) {
               //fieldPassword.requestFocus();
                PasswordField.requestFocus();
            }
        });
        
        //Al pusar ENTER en el campo de password, invocámos al método de inicio de sesión
        PasswordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                InicioOnAction(new ActionEvent());
            }
        });
    }
    
  
    @FXML
    private void goToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/registro/VistaRegistro.fxml"));
        Stage stage = new Stage();
        Parent root = loader.load();
        VistaRegistroController register = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Registro");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    @FXML
    public void InicioOnAction(ActionEvent actionEvent)  {
        String nickname = UserField.getText().trim();
        String password = PasswordField.getText();
   
        try {

            boolean isOK = Acount.getInstance().logInUserByCredentials(nickname, password);
            
            if (isOK) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/inicio/VistaInicio.fxml"));
                Stage stage = new Stage();
                Parent root = loader.load();
                VistaInicioController init = loader.getController();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Inicio");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } else {
                if (!errorInicio.isVisible()) {
                    aparecerError();
                } else {
                    remarcarError();
                }

            }

        }catch (Exception e){

            if (!errorInicio.isVisible()) {
                aparecerError();
            } else {
                remarcarError();
            }

        }
    }
        
            //Remarca el mensaje de error
    public void remarcarError(){
        FadeTransition ft = new FadeTransition(Duration.millis(200), errorInicio);
        ft.setFromValue(1.0);
        ft.setToValue(0.7);
        ft.play();
        FadeTransition fd = new FadeTransition(Duration.millis(400), errorInicio);
        fd.setFromValue(0.7);
        fd.setToValue(1.0);
        fd.play();


    }

    //Muestra el mensaje de error
    public void aparecerError(){
        errorInicio.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(600), errorInicio);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }


    }
      
    
    


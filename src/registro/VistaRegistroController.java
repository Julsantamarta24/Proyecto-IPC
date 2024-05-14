/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro;

import inicio.VistaInicioController;
import java.io.File;
import java.io.IOException;
import static java.lang.Boolean.TRUE;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxmlapplication.FXMLDocumentController;
import javafxmlapplication.JavaFXMLApplication;
import javafxmlapplication.Utils;
import model.Acount;
import model.AcountDAOException;
import model.User;
import registro.VistaRegistroController;

/**
 *
 * @author sandr
 */
public class VistaRegistroController implements Initializable{
    private final int EQUALS = 0;
    @FXML
    private Button añadirImagen;
    @FXML
    private Button registro;
    
    @FXML
    ImageView profileImage = null;
    
    @FXML
    private TextField name;
    @FXML
    private TextField nickname;
    @FXML
    private PasswordField password;
    @FXML
    private TextField celectronico;
    @FXML
    private TextField surname;
    
    private BooleanProperty correoValido = new SimpleBooleanProperty();
    private BooleanProperty usuarioValido = new SimpleBooleanProperty();
    private BooleanProperty contraseñaValida = new SimpleBooleanProperty();
    
    //private BooleanProperty equalContraseñas = new SimpleBooleanProperty();
    private BooleanProperty equalPasswords;
    @FXML
    private Label userLabel;
    @FXML
    private Label contraseñaLabel;
    @FXML
    private Label repetirLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private PasswordField repetirContraseña;
    
            
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        usuarioValido.setValue(Boolean.FALSE);
        contraseñaValida.setValue(Boolean.FALSE);
        correoValido.setValue(Boolean.FALSE);
        
        equalPasswords = new SimpleBooleanProperty();
        equalPasswords.setValue(Boolean.FALSE);

        
        añadirImagen.setOnAction(e -> seleccionarFoto());
        
        celectronico.focusedProperty().addListener((observable,oldValue,newValue)->{
            if(!newValue){
                checkEditEmail();
            }
        });
        
        password.focusedProperty().addListener((observable,oldValue,newValue)->{
            if(!newValue){
                checkEditPassword();
            }
        });
        
        repetirContraseña.focusedProperty().addListener((observable,oldValue,newValue)->{
            if(!newValue){
                checkEquals();
            }
        });
        
        BooleanBinding validFields = Bindings.and(usuarioValido, contraseñaValida).and(equalPasswords);
        
        validarUsuario();
        
    }
        

    
    @FXML
    private void registroUser(ActionEvent event) throws AcountDAOException, IOException{
        String nombre = name.getText();
        String apellido = surname.getText();
        String correo = celectronico.getText();
        String usuario = nickname.getText();
        String contraseña = password.getText();
        Image foto = getFoto();
        
        LocalDate fechaRegistro = LocalDate.now();
        
        boolean user = false;
        User userr = null;
        try{
 
        user = Acount.getInstance().registerUser(nombre, apellido, correo, usuario, contraseña, foto, fechaRegistro);
        }catch(AcountDAOException ignored){}
        if(user){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario creado correctamente");
            alert.setHeaderText(null);
            alert.setOnHidden(actionEvent -> {
                try {
                    returnToLogin();
                } catch (IOException ex) {
                    Logger.getLogger(VistaRegistroController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            alert.showAndWait();
        }
    }
    
    private void goToInicio() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/inicio/VistaInicio.fxml"));
        Stage stage = new Stage();
        Parent root = loader.load();
        VistaInicioController inicio = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Inicio");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    private void returnToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxmlapplication/FXMLDocument.fxml"));
        Stage stage = new Stage();
        Parent root = loader.load();
        FXMLDocumentController login = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Inicio");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    private Image fotoPerfil = new Image("/avatars/default.png");
    
    private void seleccionarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione una imagen de perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        // Mostrar el diálogo para seleccionar el archivo
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Actualizar la imagen en el ImageView con la nueva imagen
            fotoPerfil = new Image(selectedFile.toURI().toString());
            profileImage.setImage(fotoPerfil);
        }
    }
    
    private Image getFoto(){
        return fotoPerfil;
    }
    
    private void checkEditEmail() {
        if(!Utils.checkEmail(celectronico.textProperty().getValueSafe())){
            manageError(emailLabel,celectronico,correoValido);
        }
        else{manageCorrect(emailLabel,celectronico,correoValido);}
        
    }
    private void manageError(Label errorLabel,TextField textField, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.FALSE);
        showErrorMessage(errorLabel,textField);
        textField.requestFocus();
 
    }
   
    private void manageCorrect(Label errorLabel,TextField textField, BooleanProperty boolProp ){
        boolProp.setValue(Boolean.TRUE);
        hideErrorMessage(errorLabel,textField);
        
    }
    
    private void showErrorMessage(Label errorLabel,TextField textField)
    {
        errorLabel.visibleProperty().set(true);
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");    
    }
    /**
     * Changes the background of the edit to the default value
     * and makes the error label invisible.
     * @param errorLabel
     * @param textField 
     */
    private void hideErrorMessage(Label errorLabel,TextField textField)
    {
        errorLabel.visibleProperty().set(false);
        textField.styleProperty().setValue("");
    }

    private void checkEditPassword() {
         if(!Utils.checkPassword(password.textProperty().getValueSafe())){
            manageError(contraseñaLabel,password,contraseñaValida);
        }
        else{manageCorrect(contraseñaLabel,password,contraseñaValida);}
        
    }
    
   private void checkEquals(){
        if(password.getText().compareTo(repetirContraseña.getText()) != EQUALS){
            showErrorMessage(repetirLabel,repetirContraseña);
            equalPasswords.setValue(Boolean.FALSE);
            password.requestFocus();
        }else 
            manageCorrect(contraseñaLabel,repetirContraseña,equalPasswords);
 }
   
   private void validarUsuario() {
    nickname.textProperty().addListener((observableValue, oldValue, newValue) -> {
        if (newValue == null || newValue.isEmpty()) {
            usuarioValido.set(false);
            userLabel.setText("El usuario no puede ser nulo");
            userLabel.visibleProperty().set(true);
        } else if (newValue.contains(" ")) {
            usuarioValido.set(false);
            userLabel.setText("El usuario no puede contener espacios en blanco");
            userLabel.visibleProperty().set(true);
        } else {
            try {
                if (Acount.getInstance().existsLogin(newValue)) {
                    usuarioValido.set(false);
                    userLabel.setText("El usuario ya existe");
                    userLabel.visibleProperty().set(true);
                } else {
                    usuarioValido.set(true);
                    userLabel.visibleProperty().set(false);
                }
            } catch (AcountDAOException | IOException e) {
                e.printStackTrace();
            }
        }
    });
    usuarioValido.addListener((observable, oldValue, newValue) -> actualizarEstadoBotonAceptar());
}
   
   private void actualizarEstadoBotonAceptar() {
        registro.setDisable(!(usuarioValido.get() && correoValido.get() && contraseñaValida.get() && equalPasswords.get()));
    }

}

   
   
    
    


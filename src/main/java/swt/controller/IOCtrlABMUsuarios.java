/* 
 * Copyright (C) 2018 Agustina y Nicolas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package swt.controller;

import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import swt.DAO.*;

/**
 * FXML Controller class
 *
 * @author tinar
 */
public class IOCtrlABMUsuarios implements Initializable {

    private IOCtrlLogin login;
    private IOCtrlMenu menu;
    private int access;
    private UsersDB udb;

    @FXML    private TextField txtNombre;
    @FXML    private TextField txtContras;
    @FXML    private TextField txtUsuario;
    @FXML    private ComboBox<String> cmbUsuario;
    @FXML    private Button btnCancel;
    @FXML    private AnchorPane window;
    @FXML    private RadioButton rdb3;
    @FXML    private ToggleGroup grpAccess;
    @FXML    private RadioButton rdb2;
    @FXML    private RadioButton rdb1;
    @FXML    private RadioButton rdb0;
    @FXML    private Button btnModificar;
    @FXML    private CheckBox chkNom;
    @FXML    private CheckBox chkContr;

    public IOCtrlABMUsuarios() {
        this.udb = new UsersDB();
    }

    @FXML
    private void crearUsuario(ActionEvent event) {

        boolean nombreOK = (txtNombre.getText()!=null && !txtNombre.getText().isEmpty());
        boolean userOK = (txtUsuario.getText()!=null && !txtUsuario.getText().isEmpty());
        boolean constraOK = (txtContras.getText()!=null && !txtContras.getText().isEmpty());
        if(!nombreOK || !userOK || !constraOK)
            PopUp.popUpError("Por favor, complete los campos que dejó vacíos.", "Campos Vacíos", null);
        else{
            String nombre = txtNombre.getText();
            if(nombre.matches("[A-Za-z]+( +[A-Za-z]+)*")){
                nombre = nombre.replace("  ", " ");
                String user = txtUsuario.getText();
                if(user.matches("\\w+")){
                    String contr = txtContras.getText();
                    if(contr.matches(".{8,20}")){
                        if (grpAccess.getSelectedToggle() != null) {
                            RadioButton selectedRadioButton = (RadioButton) grpAccess.getSelectedToggle();
                            String toggleGroupValue = selectedRadioButton.getId();
                            int newAccess = 0;
                            if(toggleGroupValue.contains("0")){
                                newAccess=0;
                            }else if(toggleGroupValue.contains("1")){
                                newAccess=1;
                            }else if(toggleGroupValue.contains("2")){
                                newAccess=2;
                            }else if(toggleGroupValue.contains("3")){
                                newAccess=3;
                            }
                            udb.write(nombre, user, contr, newAccess);

                            Stage x = (Stage) window.getScene().getWindow();
                            x.close();
                        }else{PopUp.popUpError("Debe seleccionar el tipo "
                            + "de acceso que tendrá el usuario.",
                            "Tipo de acceso", null);}
                    }else{PopUp.popUpError("La contraseña debe ser "
                        + "de 8 a 20 caracteres de longitud.",
                        "Contraseña", null);}
                }else{PopUp.popUpError("El nombre de usuario no "
                    + "debe contener espacios. "
                    + "Caracteres válidos: letras, números y '_'.",
                    "Nombre de usuario", null);}
            }else{PopUp.popUpError("Sólo son válidos caracteres alfabéticos.",
                "Nombre", null);}
        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage x = (Stage) window.getScene().getWindow();
        x.close();
    }

    @FXML
    private void modifUsuario(ActionEvent event) {

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Ingrese sus datos");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Nombre de usuario");
        username.setPrefWidth(200);
        PasswordField password = new PasswordField();
        password.setPromptText("Contraseña");
        password.setPrefWidth(200);

        grid.add(new Label("Usuario:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            access = IOCtrlLogin.getUserdb().validarIngreso(usernamePassword.getKey(), usernamePassword.getValue());
        });

        if(access==3){
            String nombre = "";
            String user = cmbUsuario.getSelectionModel().getSelectedItem();
            String pass = "";
            if(chkNom.isSelected())
                nombre = txtNombre.getText();

            if(chkContr.isSelected())
                pass = txtContras.getText();

            if(!nombre.isEmpty())
                udb.modifyName(user, nombre);
            if(!pass.isEmpty())
                udb.modifyPassword(user, pass);
            RadioButton selectedRadioButton = (RadioButton) grpAccess.getSelectedToggle();
            if(selectedRadioButton!=null){
                String toggleGroupValue = selectedRadioButton.getId();
                int newAccess = 0;
                if(toggleGroupValue.contains("0")){
                    newAccess=0;
                }else if(toggleGroupValue.contains("1")){
                    newAccess=1;
                }else if(toggleGroupValue.contains("2")){
                    newAccess=2;
                }else if(toggleGroupValue.contains("3")){
                    newAccess=3;
                }
                udb.modifyAccess(user, newAccess);

                Stage x = (Stage) window.getScene().getWindow();
                x.close();
            }
        } else{PopUp.popUpError("Tu usuario no tiene acceso de borrado de usuarios.", "No tenés acceso", null);}

    }
    @FXML
    private void eliminarUsuario(ActionEvent event) {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Ingrese sus datos");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Nombre de usuario");
        username.setPrefWidth(100);
        PasswordField password = new PasswordField();
        password.setPromptText("Contraseña");
        password.setPrefWidth(100);

        grid.add(new Label("Usuario:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            access = IOCtrlLogin.getUserdb().validarIngreso(usernamePassword.getKey(), usernamePassword.getValue());
        });

        if(access==3) {
                IOCtrlLogin.getUserdb().delete(cmbUsuario.getSelectionModel().getSelectedItem());
                cmbUsuario.getItems().setAll(udb.read());
                cmbUsuario.getSelectionModel().selectFirst();
        }else{PopUp.popUpError("Tu usuario no tiene acceso de borrado de usuarios.", "No tenés acceso", null);}

    }

    @FXML
    private void comboUserSelect(ActionEvent event) {
        int access = udb.getUserAccess(cmbUsuario.getSelectionModel().getSelectedItem());
        RadioButton t = null;
        switch (access) {
            case 0:
                grpAccess.selectToggle(rdb0);
                break;
            case 1:
                grpAccess.selectToggle(rdb1);
                break;
            case 2:
                grpAccess.selectToggle(rdb2);
                break;
            case 3:
                grpAccess.selectToggle(rdb3);
                break;
            default:
                break;
        }
    }

    /*** Initializes the controller class. ************************************/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (url.getPath().contains("ModifUsuario")){
            grpAccess.selectToggle(null);
            cmbUsuario.getItems().setAll(udb.read());
            new AutoCompleteComboBoxListener<>(cmbUsuario);

            chkNom.setOnAction((event)->{
                txtNombre.setEditable(chkNom.isSelected());
            });
            chkContr.setOnAction((event)->{
                txtContras.setEditable(chkContr.isSelected());
            });

        }else if(url.getPath().contains("BajaUsuario")){
            cmbUsuario.getItems().setAll(udb.read());
            cmbUsuario.getSelectionModel().selectFirst();
            new AutoCompleteComboBoxListener<>(cmbUsuario);
        }
    }

    /** GETTERS AND SETTERS ***************************************************/

    public IOCtrlLogin getLogin() { return login; }
    public void setLogin(IOCtrlLogin login) {  this.login = login; }
    public IOCtrlMenu getMenu() {  return menu; }
    public void setMenu(IOCtrlMenu menu) {  this.menu = menu; }

}

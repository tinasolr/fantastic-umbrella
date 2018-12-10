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

import java.io.*;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import swt.DAO.*;

/**
 * FXML Controller class
 *
 * @author tinar
 */
public class IOCtrlLogin implements Initializable {

    private static IOCtrlMenu controlmenu;
    private IOCtrlConsMasivaMedios consMasivaMedios;
    private IOCtrlConsMasivaSw consMasivaSw;
    private static UsersDB userdb = new UsersDB();
    private int access;

    @FXML    private TextField txtUsuario;
    @FXML    private PasswordField txtPass;
    @FXML    private Button btnIngresar;
    @FXML    private Button btnCancelar;
    @FXML    private AnchorPane window;

    @FXML
    private void login() {

        access = userdb.validarIngreso(txtUsuario.getText(), txtPass.getText());
        if(access>-1){

            Stage x = (Stage) window.getScene().getWindow();
            x.close();

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsultaMedios.fxml"));
                Node y = loader.load();

                consMasivaMedios = loader.getController();
                consMasivaMedios.setControlMenu(controlmenu);
                controlmenu.getMainWindow().setCenter(y);
                controlmenu.setConsmasivamed(consMasivaMedios);
                controlmenu.disableEverything(access);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }else {
            PopUp.popUpError("Por favor ingrese un usuario y/o contraseña correctos", null, "Acceso denegado");
            ((Stage)window.getScene().getWindow()).setAlwaysOnTop(true);
        }
        System.out.println("swt.controller.IOCtrlLogin.login()");

    }

    @FXML
    private void salir(ActionEvent event) {
        System.exit(0);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUsuario.requestFocus();
//        window.setOnKeyPressed((event) -> { });
//        window.setOnKeyReleased((event)->{
//            if(event.getEventType().equals(KeyEvent.KEY_RELEASED) && event.getCode().equals(KeyCode.ENTER)){
//            login();
//        }
//        });
    }

    public IOCtrlMenu getControlmenu() {
        return controlmenu;
    }

    public void setControlmenu(IOCtrlMenu controlmenu) {
        this.controlmenu = controlmenu;
    }

    public IOCtrlConsMasivaMedios getConsMasivaMedios() {
        return consMasivaMedios;
    }

    public void setConsMasivaMedios(IOCtrlConsMasivaMedios consMasivaMedios) {
        this.consMasivaMedios = consMasivaMedios;
    }

    public IOCtrlConsMasivaSw getConsMasivaSw() {
        return consMasivaSw;
    }

    public void setConsMasivaSw(IOCtrlConsMasivaSw consMasivaSw) {
        this.consMasivaSw = consMasivaSw;
    }

    public static UsersDB getUserdb() {
        return userdb;
    }

    public static void setUserdb(UsersDB udb) {
        IOCtrlLogin.userdb = udb;
    }

}

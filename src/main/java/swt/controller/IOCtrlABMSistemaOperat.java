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
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import swt.DAO.*;

/**
 * FXML Controller class
 *
 * @author tinar
 */
public class IOCtrlABMSistemaOperat implements Initializable, EventHandler<KeyEvent>  {

    private List<String> soDB = new ArrayList<>();
    @FXML    private ListView<String> lstSistemaOperativo;
    @FXML    private Button btnQuitar;
    @FXML    private TextField txtSistOperativo;
    @FXML    private Button btnAgregar;
    @FXML    private Button btnAceptar;
    @FXML    private AnchorPane abmSistOperat;

    /****Initializes the controller class.**********************************/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadList();
        txtSistOperativo.setOnKeyPressed((KeyEvent t) -> {
            if(txtSistOperativo.getText().isEmpty())
                btnAgregar.setDisable(true);
            else
                btnAgregar.setDisable(false);
        });
        txtSistOperativo.setOnKeyReleased(this);
        lstSistemaOperativo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /****************************JAVAFX FUNCTIONS******************************/

    @FXML
    private void quitar(ActionEvent event) {
        if(lstSistemaOperativo.getSelectionModel().getSelectedItem()!=null){

            TextInputDialog dialog = new TextInputDialog(lstSistemaOperativo.getSelectionModel().getSelectedItem());
            dialog.setTitle("Eliminar sistema operativo");
            dialog.setHeaderText("El sistema operativo debe ser reemplazado donde era utilizado.");
            dialog.setContentText("Ingrese el valor por el cual lo quiera "
                    + "reemplazar: ");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                SistOpDB s = new SistOpDB();
                s.connect();
                s.setNombre(lstSistemaOperativo.getSelectionModel().getSelectedItem());
                s.setNewNombre(name);
                /*
                    Update en bd se fija si existe uno con este nuevo nombre,
                    si existe, modifica las tablas que lo usan al id del que
                    corresponde con este nombre nuevo y borra el otro.
                    De no existir, cambia el nombre de este al nuevo nombre.
                */
                s.update();
                soDB.set(lstSistemaOperativo.getSelectionModel().getSelectedIndex(), name);
                loadList();
                txtSistOperativo.clear();
            });

        }else{PopUp.popUpError("Por favor, seleccione un sistema operativo a eliminar.", null, null);}
    }

    @FXML
    private void agregar(ActionEvent event) {
        String sistemaOperativo = txtSistOperativo.getText();
        if(sistemaOperativo.matches("[a-zA-Z][0-9a-zA-Z\\s]*")){
            boolean dup = false;
            if(soDB.isEmpty())
                getSOFromDB();
            for (String so : soDB)
                if(so.equalsIgnoreCase(sistemaOperativo)){
                    dup=true;
                    break;
                }
            if(!dup){
                SistOpDB s = new SistOpDB();
                s.connect();
                s.setNombre(sistemaOperativo);
                s.write();
                soDB.add(sistemaOperativo);
                loadList();
                txtSistOperativo.clear();
                btnAgregar.setDisable(true);
            }else{PopUp.popUpError("Ese sistema operativo ya se encuenta ingresado. Por favor, ingrese otro.", null, null);}
        }else{PopUp.popUpError("Por favor, ingrese un sistema operativo válido.", null, null);}
    }

    @FXML
    private void aceptar(ActionEvent event) {
        Stage x = (Stage) abmSistOperat.getScene().getWindow();
        x.close();
    }

    /*********************OTHER FUNCTIONS*************************************/

    public void getSOFromDB(){
        SistOpDB so = new SistOpDB();
        so.connect();
        List<SistOpDB> sos = so.read("SistOperativos");
        for(SistOpDB s : sos)
            soDB.add(s.getNombre());
    }

    public void loadList(){
        if(soDB.isEmpty())
            getSOFromDB();
        if(!lstSistemaOperativo.getItems().isEmpty())
            lstSistemaOperativo.getItems().clear();
        lstSistemaOperativo.getItems().setAll(soDB);
        if(lstSistemaOperativo.getItems().size()>0)
            btnQuitar.setDisable(false);
    }

    private void loadList(String x){
        ObservableList<String> list = FXCollections.observableArrayList();
        List<String> data = soDB;

        for (String f : data)
            if(f.toLowerCase().contains(x.toLowerCase()))
                list.add(f);

        lstSistemaOperativo.getItems().setAll(list);
        lstSistemaOperativo.getSelectionModel().selectFirst();
        if(lstSistemaOperativo.getItems().size()>0)
            btnQuitar.setDisable(false);
        else
           btnQuitar.setDisable(true);
    }

    /**************EVENT HANDLERS*********************************************/

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_RELEASED) && event.getSource().equals(txtSistOperativo)){
            switch (event.getCode()) {
                case ENTER:
                    agregar(new ActionEvent());
                    break;
                default:
                    if(txtSistOperativo.getText().isEmpty()){
                        loadList();
                        btnAgregar.setDisable(true);
                    }else{
                        loadList(txtSistOperativo.getText());
                        btnAgregar.setDisable(false);
                    }
                    break;
            }
        }
    }
}

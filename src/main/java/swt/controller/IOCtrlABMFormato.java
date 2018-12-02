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
public class IOCtrlABMFormato implements Initializable, EventHandler<KeyEvent> {

    private List<String> formatos = new ArrayList<>();
    private final FormatoDB f;
    private IOCtrlConsMasivaMedios consmasivamed;
    @FXML    private ListView<String> lstFormato;
    @FXML    private Button btnQuitar;
    @FXML    private Button btnAgregar;
    @FXML    private Button btnAceptar;
    @FXML    private TextField txtFormato;
    @FXML    private AnchorPane abmFormato;

    public IOCtrlABMFormato() {
        this.f = new FormatoDB();
    }

    /*******Initializes the controller class.**********************************/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadList();

        txtFormato.setOnKeyPressed((KeyEvent t) -> {
            if(txtFormato.getText().isEmpty())
                btnAgregar.setDisable(true);
            else
                btnAgregar.setDisable(false);});
        txtFormato.setOnKeyReleased(this);
        lstFormato.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /****************************JAVAFX FUNCTIONS******************************/

        @FXML
    private void quitar(ActionEvent event) {
        if(lstFormato.getSelectionModel().getSelectedItem()!=null){

            TextInputDialog dialog = new TextInputDialog(lstFormato.getSelectionModel().getSelectedItem());
            dialog.setTitle("Eliminar formato");
            dialog.setHeaderText("El formato debe ser reemplazado donde era utilizado.");
            dialog.setContentText("Ingrese el valor por el cual lo quiera "
                    + "reemplazar: ");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                /*
                    Update en bd se fija si existe uno con este nuevo nombre,
                    si existe, modifica medios y copias al id de formato del que
                    tiene este nombre y borra el otro formato. De no existir,
                    cambia el nombre de este formato al nuevo.
                */
                f.update(lstFormato.getSelectionModel().getSelectedItem(), name);
                formatos.set(lstFormato.getSelectionModel().getSelectedIndex(), name);
                loadList();
                new MediosCtrl().cargarMedios();
                consmasivamed.loadTable();
                txtFormato.clear();
            });

        }else{
            PopUp.popUpError("Por favor, seleccione un formato a eliminar.", null, null);
        }
    }

    @FXML
    private void agregar(ActionEvent event) {
        if(txtFormato.getText().matches("[a-zA-Z][0-9a-zA-Z\\s-]*")){
            boolean dup = false;
            for (String fo : formatos) {
                if(fo.equalsIgnoreCase(txtFormato.getText())){
                    dup=true;
                    break;
                }
            }
            if(!dup){
                f.write(txtFormato.getText());
                formatos.add(txtFormato.getText());
                txtFormato.clear();
                btnAgregar.setDisable(true);
                loadList();
            }else{PopUp.popUpError("Ese formato ya se encuenta ingresado. Por favor, ingrese otro.", null, null);}
        }else{PopUp.popUpError("Por favor, ingrese un formato válido.", null, null);}
    }

    @FXML
    private void aceptar(ActionEvent event) {
        Stage x = (Stage) abmFormato.getScene().getWindow();
        x.close();
    }

    /*********************OTHER FUNCTIONS*************************************/

    public void getFormatosFromDB(){
        formatos = f.read();
    }

    public void loadList(){
        if(formatos.isEmpty())
            getFormatosFromDB();

        if(!lstFormato.getItems().isEmpty())
            lstFormato.getItems().clear();

        lstFormato.getItems().setAll(formatos);

        if(lstFormato.getItems().size()>0)
            btnQuitar.setDisable(false);
    }

    public void loadList(String x){
        ObservableList<String> list = FXCollections.observableArrayList();
        List<String> data = formatos;

        for (String f : data)
            if(f.toLowerCase().contains(x.toLowerCase()))
                list.add(f);

        lstFormato.getItems().setAll(list);
        lstFormato.getSelectionModel().selectFirst();
        if(lstFormato.getItems().size()>0)
            btnQuitar.setDisable(false);
        else
           btnQuitar.setDisable(true);
    }

    /**************EVENT HANDLERS*********************************************/

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_RELEASED) && event.getSource().equals(txtFormato)){
            if(null == event.getCode()) {
                if(txtFormato.getText().isEmpty()){
                    loadList();
                    btnAgregar.setDisable(true);
                }else{
                    loadList(txtFormato.getText());
                    btnAgregar.setDisable(false);
                }
            } else switch (event.getCode()) {
                case ENTER:
                    agregar(new ActionEvent());
                    break;
                case BACK_SPACE:
                case DELETE:
                    if(txtFormato.getText().isEmpty()){
                        loadList();
                        btnAgregar.setDisable(true);
                    }else{
                        loadList(txtFormato.getText());
                        btnAgregar.setDisable(false);
                    }   break;
                default:
                    if(txtFormato.getText().isEmpty()){
                        loadList();
                        btnAgregar.setDisable(true);
                    }else{
                        loadList(txtFormato.getText());
                        btnAgregar.setDisable(false);
                    }   break;
            }
        }
    }

    void setConsMasivaMedios(IOCtrlConsMasivaMedios consmasivamed) {
        this.consmasivamed = consmasivamed;
    }

}


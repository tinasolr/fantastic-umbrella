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
import java.sql.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import swt.DAO.*;
import swt.model.*;

/**
 *
 * @author tinar
 */
public class IOCtrlABMUbicaciones implements Initializable, EventHandler<KeyEvent> {

    private IOCtrlConsMasivaMedios consMasivaMedios;
    private UbicacionesCtrl ubictrl = new UbicacionesCtrl();
    private UbicacionesDB udb = new UbicacionesDB();
    @FXML    private Button btnQuitar;
    @FXML    private TextField txtCodigo;
    @FXML    private Button btnAgregar;
    @FXML    private Button btnAceptar;
    @FXML    private TextArea txtDescripcion;
    @FXML    private TableView<Ubicaciones> tblUbicaciones;
    @FXML    private TableColumn<Ubicaciones, String> colCodigo;
    @FXML    private TableColumn<Ubicaciones, String> colDescripcion;
    @FXML    private Label lblCodigo;
    @FXML    private AnchorPane abmUbicaciones;

    /*******Initializes the controller class.**********************************/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTable();
        txtCodigo.setOnKeyPressed((KeyEvent event) -> {

        });
        txtCodigo.setOnKeyReleased(this);
        txtDescripcion.setOnKeyPressed((KeyEvent k) -> {});
        txtDescripcion.setOnKeyReleased(this);
        tblUbicaciones.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /****************************JAVAFX FUNCTIONS******************************/

    @FXML
    private void quitar(ActionEvent event) {
        if(tblUbicaciones.getSelectionModel().getSelectedItem() != null){

            String ub = tblUbicaciones.getSelectionModel().getSelectedItem().getId();

            if(!tblUbicaciones.getSelectionModel().getSelectedItem().getId().equalsIgnoreCase("NOASIG")){

                char answer = popUpWarning("Está seguro de que desea eliminar la ubicación " + ub + "? \n "
                        + "Hay " + udb.cantidadMediosEnUbicacion(ub) +
                        " medios y " + udb.cantidadCopiasEnUbicacion(ub) + " copias en esa ubicación.");
                if(answer=='y'){

                    udb.delete(tblUbicaciones.getSelectionModel().getSelectedItem().getId(), "NOASIG");

                    ubictrl.getUbis().remove(tblUbicaciones.getSelectionModel().getSelectedItem());
                    loadTable();
                    txtCodigo.clear();
                    txtDescripcion.clear();

                }else if(answer=='n'){

                    List<String> choices = new ArrayList<>();
                    for(Ubicaciones x : tblUbicaciones.getItems())
                        choices.add(x.getId());

                    ChoiceDialog<String> dialog = new ChoiceDialog(
                            choices.get(tblUbicaciones.getSelectionModel().getSelectedIndex()), choices);
                    dialog.setTitle("Nueva ubicacion");
                    dialog.setHeaderText("Elija dónde reubicar las piezas.");
                    dialog.setContentText("Codigo: ");

                    // Traditional way to get the response value.
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()){
                        udb.delete(tblUbicaciones.getSelectionModel().getSelectedItem().getId(), result.get());
                        ubictrl.getUbis().remove(tblUbicaciones.getSelectionModel().getSelectedItem());
                        loadTable();
                    }
                    txtCodigo.clear();
                    txtDescripcion.clear();

                }
            }else{PopUp.popUpError("No puede eliminar este valor.", null, null);}
        }else{PopUp.popUpError("Por favor, seleccione una ubicación a eliminar.", null, null);}
    }

    @FXML
    private void agregar(ActionEvent event) throws SQLException {
        String id = txtCodigo.getText();
        String descripcion = txtDescripcion.getText();

        if(id.matches("[A-Z]+[0-9]+(\\-[0-9]+)?")){

            if(!udb.isDuplicate(id)){

                udb.write(id, descripcion);
                ubictrl.getUbis().add(new Ubicaciones(id, descripcion));
                loadTable();
                txtCodigo.clear();
                txtDescripcion.clear();
                btnAgregar.setDisable(true);

            }else{PopUp.popUpError("El código ya existe. Por favor, ingrese uno diferente", null, null); }
        }else{
            String error = "Algo salió mal.";
            if(id.isEmpty()) error = "Por favor, ingrese un código.";
            else if(!id.matches("[A-Z]+[0-9]+")) error = "Por favor, ingrese un código válido.";
            PopUp.popUpError(error, null, null);

        }
    }

    @FXML
    private void aceptar(ActionEvent event) {
        Stage x = (Stage) abmUbicaciones.getScene().getWindow();
        x.close();
    }

    /*********************OTHER FUNCTIONS*************************************/

    public void loadTable(){
        UbicacionesCtrl ubictrl = new UbicacionesCtrl();
        if(ubictrl.getUbis().isEmpty())
            ubictrl.cargarUbicaciones();

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        if(ubictrl.getUbis()!=null)
            tblUbicaciones.getItems().setAll(ubictrl.getUbis());

        if(tblUbicaciones.getItems().isEmpty())
            btnQuitar.setDisable(true);
        else
            btnQuitar.setDisable(false);
    }

    public void loadTable(String partId, String partDescr){
        UbicacionesCtrl u = new UbicacionesCtrl();
        if(u.getUbis().isEmpty())
            u.cargarUbicaciones();
        List<Ubicaciones> ubi = new ArrayList<>();

        if(!partId.isEmpty() && !partDescr.isEmpty())
            for(Ubicaciones x : u.getUbis()){
                if(x.getDescripcion() != null)
                    if(x.getId().toLowerCase().contains(partId.toLowerCase()) && x.getDescripcion().toLowerCase().contains(partDescr.toLowerCase()))
                        ubi.add(x);
            }
        if(partId.isEmpty() && !partDescr.isEmpty())
            for(Ubicaciones x : u.getUbis()){
                if(x.getDescripcion() != null)
                    if(x.getDescripcion().toLowerCase().contains(partDescr.toLowerCase()))
                        ubi.add(x);
            }
        if(!partId.isEmpty() && partDescr.isEmpty())
            for(Ubicaciones x : u.getUbis()){
                if(x.getId().toLowerCase().contains(partId.toLowerCase()))
                    ubi.add(x);
            }
        tblUbicaciones.getItems().setAll(ubi);

        if(tblUbicaciones.getItems().isEmpty())
            btnQuitar.setDisable(true);
        else
            btnQuitar.setDisable(false);
    }

    public char popUpWarning(String texto){
        ButtonType buttonTypeOne = new ButtonType("Reubicar");
        Alert alert = new Alert(Alert.AlertType.WARNING, texto, ButtonType.YES, buttonTypeOne, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                alert.close();
                return 'y';
            } else if(alert.getResult() == buttonTypeOne){
                alert.close();
                return 'n';
            } else {
                alert.close();
                return 'c';
            }
    }

    /**************EVENT HANDLERS*********************************************/
    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_RELEASED) && event.getSource().equals(txtCodigo)){
            if(txtCodigo.getText().isEmpty() || txtCodigo.getText() == null){
                loadTable();
                btnAgregar.setDisable(true);
            }else{
                loadTable(txtCodigo.getText(), txtDescripcion.getText());
                btnAgregar.setDisable(false);
            }
        }else if(event.getEventType().equals(KeyEvent.KEY_RELEASED) && event.getSource().equals(txtDescripcion)){
            if(txtDescripcion.getText().isEmpty() || txtDescripcion.getText() == null)
                loadTable();
            else
                loadTable(txtCodigo.getText(), txtDescripcion.getText());
        }
    }

    public IOCtrlConsMasivaMedios getConsMasivaMedios() {
        return consMasivaMedios;
    }

    public void setConsMasivaMedios(IOCtrlConsMasivaMedios consMasivaMedios) {
        this.consMasivaMedios = consMasivaMedios;
    }
}

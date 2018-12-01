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
import swt.view.*;


/**
 *
 * @author Nico
 */
public class IOCtrlABMCopias implements Initializable, EventHandler<Event>{

    @FXML private AnchorPane AltaCopia;
    @FXML private Label lblBuscarCopia;
    @FXML private TextField txtBuscarCopia;
    @FXML private Label lblMedio;
    @FXML private Label lblFormato;
    @FXML private Label lblUbicaciones;
    @FXML private TextField txtMedio;
    @FXML private TextArea txtDescripcion;
    @FXML private Button btnFinalizar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnCancelar;
    @FXML private ComboBox<String> cmbFormato;
    @FXML private ComboBox<String> cmbUbicaciones;
    @FXML private CheckBox chkDeposito;
    @FXML private ScrollPane ScrollPaneTabla;
    @FXML private TableView<CopiasTableFormat> TablaCopias;
    @FXML private TableColumn colIDCopia;
    @FXML private TableColumn colFormato;
    @FXML private TableColumn colUbicacion;
    @FXML private TableColumn colEnDeposito;
    @FXML private TableColumn colDescripcion;

    private String medioID;
    private String nomMedio;
    private IOCtrlConsMasivaMedios controlMenu;
    private int access;
    private final CopiasDB cpdb;
    private final List<CopiasTableFormat> CopTabla;
    private final CopiasCtrl coCtrl;
    private final FormatoDB foDB;
    private final UbicacionesDB ubDB;
    private final CopiasCtrl copctrl;
    private final UbicacionesCtrl ubCtrl;


    public IOCtrlABMCopias() {
        this.ubCtrl = new UbicacionesCtrl();
        this.copctrl = new CopiasCtrl();
        this.ubDB = new UbicacionesDB();
        this.foDB = new FormatoDB();
        this.coCtrl = new CopiasCtrl();
        this.CopTabla = new ArrayList();
        this.cpdb = new CopiasDB();
    }

    /*******Initializes the controller class.**********************************/

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        access = IOCtrlLogin.getUserdb().getUserAccess();
        if(access>-1){
            txtBuscarCopia.setOnKeyPressed((KeyEvent event) -> {  });
            txtBuscarCopia.setOnKeyReleased(this);
            txtMedio.setText(nomMedio);
            cargarTabla();
            List<String> form = foDB.read();
            if(!cmbFormato.getItems().isEmpty())
                cmbFormato.getItems().clear();

            form.forEach((x)-> {cmbFormato.getItems().add(x);});
            new AutoCompleteComboBoxListener<>(cmbFormato);

            if(!cmbUbicaciones.getItems().isEmpty())
                cmbUbicaciones.getItems().clear();
            ubCtrl.getUbis().forEach((y) -> { cmbUbicaciones.getItems().add(y.getId()); });
            new AutoCompleteComboBoxListener<>(cmbUbicaciones);
        }else{
            disableEverything(true);
        }
    }

    /** FXML FUNCTIONS *********************************************************/

    @FXML
    private void agregarCopia(ActionEvent event) throws SQLException {
        String descripcion = txtDescripcion.getText();

        Copias copia;
        Ubicaciones ubicacion;

        if(cmbFormato.getSelectionModel().getSelectedItem() != null){
            if(cmbUbicaciones.getSelectionModel().getSelectedItem() != null){

                //Buscar ID Formato
                int formId=foDB.search(cmbFormato.getSelectionModel().getSelectedItem());
                //Creación de Copia en BD
                coCtrl.CrearCopia(medioID,formId ,descripcion);

                //Buscar ID de la copia Creada
                int id = coCtrl.buscarUltimoID();

                //Buscar ID de Ubicaciones
                ubicacion = ubCtrl.findUbicacion(cmbUbicaciones.getSelectionModel().getSelectedItem());

                boolean enDepo = chkDeposito.isSelected();
                //Crea copia en la lista de copias
                copia = new Copias(id,cmbFormato.getSelectionModel().getSelectedItem(),descripcion,ubicacion, enDepo);
                coCtrl.getCopias().add(copia);


                //Asocia la Copia con la Ubicacion en la BD
                coCtrl.asociarUbicacionACopia(id, cmbUbicaciones.getSelectionModel().getSelectedItem(), chkDeposito.isSelected());

                PopUp.popUpInfo("Copia creada con éxito.", null, "Exito!");
                clearFields();
                cargarTabla();
            }else{PopUp.popUpError("Por favor, seleccione una ubicacion para la copia.", null, null);}
        }else{PopUp.popUpError("Por favor, seleccione un formato para la copia.", null, null);}
    }

    @FXML
    private void modificarCopia(ActionEvent event) throws SQLException{

        if (TablaCopias.getSelectionModel().getSelectedItem()!=null){
            if(cmbFormato.getSelectionModel().getSelectedItem() != null){
                if(cmbUbicaciones.getSelectionModel().getSelectedItem() != null){
                    if(PopUp.popUpWarning("¿Está seguro de que desea modificar la copia " + TablaCopias.getSelectionModel().getSelectedItem().getId() + "?", null, "Confirmar"))
                    {
                        //Buscar ID Formato
                        int formId=foDB.search(cmbFormato.getSelectionModel().getSelectedItem());

                        int copiaID = TablaCopias.getSelectionModel().getSelectedItem().getId();
                        String obs = txtDescripcion.getText();

                        copctrl.ModificarCopia(copiaID, medioID, formId, obs);

                        Ubicaciones ubicacion = ubCtrl.findUbicacion(cmbUbicaciones.getSelectionModel().getSelectedItem());

                        for (Copias co: copctrl.getCopias()){
                            if(co.getId()==TablaCopias.getSelectionModel().getSelectedItem().getId()){
                                co.setFormato(cmbFormato.getSelectionModel().getSelectedItem());
                                co.setObserv(txtDescripcion.getText());
                                co.setUbiDepo(ubicacion);
                            }
                        }

                        copctrl.modificarUbicacionACopia(
                            TablaCopias.getSelectionModel().getSelectedItem().getId(),
                            ubicacion.getId(), chkDeposito.isSelected());

                        cargarTabla();
                        txtBuscarCopia.setText("");
                    }else{PopUp.popUpError("No se pudo modidifcar este valor.", null, null);}
                }else{PopUp.popUpError("Por favor, seleccione una ubicacion para la copia.", null, null);}
            }else{PopUp.popUpError("Por favor, seleccione un formato para la copia.", null, null);}
        }else{PopUp.popUpError("Seleccione una copia de la tabla para modificar", null, null);}
    }

    @FXML
    private void eliminarCopia(ActionEvent event) {

     if(TablaCopias.getSelectionModel().getSelectedItem()!=null)
        if(PopUp.popUpWarning("¿Está seguro de que desea eliminar la copia " + TablaCopias.getSelectionModel().getSelectedItem().getId() + "?", null, "Confirmar"))
        {
            copctrl.EliminarCopia(TablaCopias.getSelectionModel().getSelectedItem().getId());
            copctrl.desasociarUbicacionACopia(TablaCopias.getSelectionModel().getSelectedItem().getId());
            for (Copias co: copctrl.getCopias()){
                if(co.getId()==TablaCopias.getSelectionModel().getSelectedItem().getId()){
                    copctrl.getCopias().remove(co);
                }
            }
            cargarTabla();
            txtBuscarCopia.setText("");

        }else{PopUp.popUpError("No se pudo eliminar este valor.", null, null);}
     else{PopUp.popUpError("Seleccione una copia de la tabla para eliminar", null, null);}

    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage x = (Stage) AltaCopia.getScene().getWindow();
        x.close();
    }

    /****************** OTHER FUNCTIONS *************************************/

    public void disableEverything(boolean x){
        txtBuscarCopia.setDisable(x);
        txtDescripcion.setDisable(x);
        txtMedio.setDisable(x);
        btnCancelar.setDisable(x);
        btnEliminar.setDisable(x);
        btnFinalizar.setDisable(x);
        btnModificar.setDisable(x);
        cmbFormato.setDisable(x);
        cmbUbicaciones.setDisable(x);
        chkDeposito.setDisable(x);
        TablaCopias.setDisable(x);
    }

    public void cargarTabla(){

        CopiasCtrl copctrl = new CopiasCtrl();
        UbicacionesDB ubidb = new UbicacionesDB();

//        String enDepo="";
        if(copctrl.getCopias().isEmpty())
            copctrl.cargarCopias(medioID);

        if(!CopTabla.isEmpty())
            CopTabla.clear();

        colIDCopia.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFormato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("codUbi"));
        colEnDeposito.setCellValueFactory(new PropertyValueFactory<>("enDepo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        for(Copias c : copctrl.getCopias()){
//            ubidb.setCodUbi(c.getUbiDepo().getId());
//
//            int ubi= ubidb.find_EnDepo();
//            if (ubi==1)enDepo="Si";
//            else enDepo="No";
            CopTabla.add(new CopiasTableFormat(c.getId(),c.getFormato(),c.getUbiDepo().getId(),c.isEnDepo(),c.getObserv()));
        }

        if(copctrl.getCopias()!=null)
            TablaCopias.getItems().setAll(CopTabla);
    }

    public void visualizarAltaCopia(){
        lblBuscarCopia.setVisible(false);
        txtBuscarCopia.setVisible(false);
        btnModificar.setVisible(false);
        btnEliminar.setVisible(false);
        btnFinalizar.setVisible(true);
        cmbFormato.setDisable(false);
        cmbUbicaciones.setDisable(false);
        chkDeposito.setVisible(true);
        txtDescripcion.setEditable(true);
        //TablaCopias.setEditable(false);
    }
    public void visualizarVerCopia(){
        lblBuscarCopia.setVisible(false);
        txtBuscarCopia.setVisible(false);
        btnModificar.setVisible(false);
        btnEliminar.setVisible(false);
        btnFinalizar.setVisible(false);
        cmbFormato.setDisable(true);
        cmbUbicaciones.setDisable(true);
        chkDeposito.setVisible(false);
        txtDescripcion.setEditable(false);
    }

    public void visualizarModificarCopia(){
        lblBuscarCopia.setVisible(true);
        txtBuscarCopia.setVisible(true);
        btnModificar.setVisible(true);
        btnEliminar.setVisible(false);
        btnFinalizar.setVisible(false);
        cmbFormato.setDisable(false);
        cmbUbicaciones.setDisable(false);
        chkDeposito.setVisible(true);
        txtDescripcion.setEditable(true);
    }

    public void visualizarEliminarCopia(){
        lblBuscarCopia.setVisible(true);
        txtBuscarCopia.setVisible(true);
        btnModificar.setVisible(false);
        btnEliminar.setVisible(true);
        btnFinalizar.setVisible(false);
        cmbFormato.setDisable(true);
        cmbUbicaciones.setDisable(true);
        chkDeposito.setVisible(true);
        txtDescripcion.setEditable(true);
    }

    @Override
    public void handle(Event event) {
        if(event.getEventType().equals(KeyEvent.KEY_RELEASED) && event.getSource().equals(txtBuscarCopia))
            if(txtBuscarCopia.getText().isEmpty() || txtBuscarCopia.getText() == null)
                cargarTodo();
            else
                loadTable(txtBuscarCopia.getText());
    }

    public void loadTable(String id){
        if(CopTabla.isEmpty())
            cargarTabla();

        String idtabla;
        id = id.toLowerCase(Locale.ROOT);

        List<CopiasTableFormat> aux = new ArrayList();

        for(CopiasTableFormat c: CopTabla){
            idtabla = String.valueOf(c.getId()).toLowerCase(Locale.ROOT);
            if(idtabla.contains(id)) aux.add(c);
        }


        TablaCopias.getItems().setAll(aux);

    }

    public void cargarTodo(){

        colIDCopia.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFormato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        colEnDeposito.setCellValueFactory(new PropertyValueFactory<>("enDepo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        if(!CopTabla.isEmpty()) TablaCopias.getItems().setAll(CopTabla);
    }

    public void clearFields(){
        txtBuscarCopia.setText("");
        txtDescripcion.setText("");
        txtMedio.setText("");
        cmbFormato.getSelectionModel().selectFirst();
        cmbUbicaciones.getSelectionModel().selectFirst();
        chkDeposito.setSelected(false);
    }

    /***************** GETTERS AND SETTERS ***********************************/

    public Label getLblMedio() {
        return lblMedio;
    }

    public void setLblMedio(Label lblMedio) {
        this.lblMedio = lblMedio;
    }

    public Label getLblFormato() {
        return lblFormato;
    }

    public void setLblFormato(Label lblFormato) {
        this.lblFormato = lblFormato;
    }

    public Label getLblUbicaciones() {
        return lblUbicaciones;
    }

    public void setLblUbicaciones(Label lblUbicaciones) {
        this.lblUbicaciones = lblUbicaciones;
    }

    public TextField getTxtMedio() {
        return txtMedio;
    }

    public void setTxtMedio(TextField txtMedio) {
        this.txtMedio = txtMedio;
    }

    public TextArea getTxtDescripcion() {
        return txtDescripcion;
    }

    public void setTxtDescripcion(TextArea txtDescripcion) {
        this.txtDescripcion = txtDescripcion;
    }

    public Button getBtnFinalizar() {
        return btnFinalizar;
    }

    public void setBtnFinalizar(Button btnFinalizar) {
        this.btnFinalizar = btnFinalizar;
    }

    public Button getBtnModificar() {
        return btnModificar;
    }

    public void setBtnModificar(Button btnModificar) {
        this.btnModificar = btnModificar;
    }

    public Button getBtnEliminar() {
        return btnEliminar;
    }

    public void setBtnEliminar(Button btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    public Button getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(Button btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public ComboBox getCmbFormato() {
        return cmbFormato;
    }

    public void setCmbFormato(ComboBox cmbFormato) {
        this.cmbFormato = cmbFormato;
    }

    public ComboBox getCmbUbicaciones() {
        return cmbUbicaciones;
    }

    public void setCmbUbicaciones(ComboBox cmbUbicaciones) {
        this.cmbUbicaciones = cmbUbicaciones;
    }

    public CheckBox getChkDeposito() {
        return chkDeposito;
    }

    public void setChkDeposito(CheckBox chkDeposito) {
        this.chkDeposito = chkDeposito;
    }

    public AnchorPane getPane() {
        return AltaCopia;
    }

    public void setPane(AnchorPane Pane) {
        this.AltaCopia = Pane;
    }

    public String getMedioID() {
        return medioID;
    }

    public void setMedioID(String medioID) {
        this.medioID = medioID;
    }

    public String getNomMedio() {
        return nomMedio;
    }

    public void setNomMedio(String nomMedio) {
        this.nomMedio = nomMedio;
    }

    public IOCtrlConsMasivaMedios getControlMenu() {
        return controlMenu;
    }

    public void setControlMenu(IOCtrlConsMasivaMedios controlMenu) {
        this.controlMenu = controlMenu;
    }






}

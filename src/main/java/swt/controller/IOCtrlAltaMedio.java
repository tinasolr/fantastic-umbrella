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
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import swt.DAO.*;
import swt.model.*;


/**
 * FXML Controller class
 *
 * @author tinar
 */
public class IOCtrlAltaMedio implements Initializable, EventHandler<KeyEvent> {

    private BorderPane mainWindow;
    private MediosCtrl meCtrl = new MediosCtrl();
    private UbicacionesCtrl ubCtrl = new UbicacionesCtrl();
    private MediosDB meDB = new MediosDB();
    private FormatoDB foDB = new FormatoDB();
    private File archImagen;
    private IOCtrlConsMasivaSw consmasivasw;
    private IOCtrlConsMasivaMedios consmasivamed;
    private IOCtrlMenu controlMenu;
    private FormatoDB f = new FormatoDB();


    @FXML    private AnchorPane window;
    @FXML    private TitledPane tpaneDatosMedio;
    @FXML    private TextField txtCodigo;
    @FXML    private TextField txtNombre;
    @FXML    private Label lblCodigo;
    @FXML    private Label lblNombre;
    @FXML    private Label lblImagen;
    @FXML    private ImageView image;
    @FXML    private Label lblFormato;
    @FXML    private Label lblUbicacion;
    @FXML    private Label lblObservaciones;
    @FXML    private Label lblPartes;
    @FXML    private ComboBox<String> cmbFormato;
    @FXML    private Label lblEmpaque;
    @FXML    private TitledPane tpaneInfoFisica;
    @FXML    private ComboBox<String> cmbUbicacion;
    @FXML    private TextField txtPartes;
    @FXML    private TextArea txtObservaciones;
    @FXML    private TitledPane tPaneSwMedio;
    @FXML    private TableView<Software> tblSoftwareDisp;
    @FXML    private TableColumn<Software, String> colNombre;
    @FXML    private TableColumn<Software, String> colVersion;
    @FXML    private ListView<String> lstSwContenido;
    @FXML    private Button btnAgregar;
    @FXML    private Button btnQuitar;
    @FXML    private Button btnIngresar;
    @FXML    private Button btnCancelar;
    @FXML    private TextField txtBusqueda;
    @FXML    private RadioButton rbOriginal;
    @FXML    private RadioButton rbMixto;
    @FXML    private RadioButton rbOtros;
    @FXML    private CheckBox chkCaja;
    @FXML    private CheckBox chkManual;
    @FXML    private CheckBox chkEnDeposito;
    @FXML    private ToggleGroup grpOrigen;
    @FXML    private Button btnReload;

    /********Initializes the controller class.*********************************/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        chkCaja = new CheckBox();
//        chkEnDeposito = new CheckBox();
//        chkManual = new CheckBox();

        //Carga de Combos
        if(ubCtrl.getUbis().isEmpty())
            ubCtrl.cargarUbicaciones();
        if(!cmbUbicacion.getItems().isEmpty())
            cmbUbicacion.getItems().clear();

        ubCtrl.getUbis().forEach((x) -> { cmbUbicacion.getItems().add(x.getId());});
        new AutoCompleteComboBoxListener<>(cmbUbicacion);

        List<String> fo = f.read();
        if(!cmbFormato.getItems().isEmpty())
            cmbFormato.getItems().clear();

        fo.forEach((x) -> { cmbFormato.getItems().add(x); });
        new AutoCompleteComboBoxListener<>(cmbFormato);

        image.setImage(new Image(getClass().getResource("/images/no-image-available.png").toExternalForm()));

        loadTable();
        txtBusqueda.setOnKeyPressed((KeyEvent event) -> {});
        txtBusqueda.setOnKeyReleased(this);
        tblSoftwareDisp.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**********************JAVAFX FUNCTIONS**********************************/

        @FXML
    private void agregarImagen(MouseEvent event) {

        if(Images.getPath().isEmpty()){
            PopUp.popUpInfo("Por favor, seleccione un directorio donde almacenar las imagenes", null, "Seleccionar directorio");
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            configuringDirectoryChooser(directoryChooser);
            File dir = directoryChooser.showDialog(new Stage());
            Images.setPath(dir.getAbsolutePath());
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        archImagen = fileChooser.showOpenDialog(new Stage());
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        if(archImagen != null){
            try {
                Path from = Paths.get(archImagen.toURI());
                Path to = Paths.get(Images.getPath());
                to = to.resolve(Paths.get(archImagen.getName()));
                System.out.println(to);
                File exists = to.toFile();
                if(exists.length()<=0){
                    CopyOption[] options = new CopyOption[]{
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES
                    };
                    Files.copy(from, to, options);
                }
//                File fi = new File("./src/images/" + archImagen.getName());
//                image.setImage(new Image("file:///" + fi.getAbsolutePath()));
                image.setImage(new Image("file:///" + Images.getPath() + File.separator + archImagen.getName()));
                System.out.println(image);
                archImagen = new File(Images.getPath() + File.separator + archImagen.getName());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void agregar(MouseEvent event) {
        if(tblSoftwareDisp.getSelectionModel().getSelectedItem()!=null){
            Software s = tblSoftwareDisp.getSelectionModel().getSelectedItem();
            boolean add = true;
            for(String x : lstSwContenido.getItems())
                if(x.equalsIgnoreCase(s.getCodigo() + " - " + s.getNombre()))
                    add = false;
            if(add)
                lstSwContenido.getItems().add(s.getCodigo() + " - " + s.getNombre());
            else
                PopUp.popUpError("Ese software ya se agregó.", null, null);
        }else{PopUp.popUpError("Por favor, seleccione un software a agregar", null, null);}
    }

    @FXML
    private void quitar(MouseEvent event) {
        if(lstSwContenido.getSelectionModel().getSelectedItem()!=null){
            String s = lstSwContenido.getSelectionModel().getSelectedItem();
            lstSwContenido.getItems().remove(s);
        }else{PopUp.popUpError("Por favor, seleccione un software a quitar", null, null);}
    }

    @FXML
    private void ingresarMedio(ActionEvent event) {

        String id = txtCodigo.getText();
        String nombre = txtNombre.getText();
        String formato = cmbFormato.getSelectionModel().getSelectedItem();
        String ubicacion = cmbUbicacion.getSelectionModel().getSelectedItem();
        String imagen=null;
        String observ = txtObservaciones.getText();

        int origen = 0;
        boolean manual = false;
        boolean caja = false;
        boolean endepo = false;
        Ubicaciones ubaux = new Ubicaciones();
        Medios medio = null;

        try{
            int partes = Integer.parseInt(txtPartes.getText());
            if(id.matches(".+")){
                if(nombre.matches(".{3,}")){
                    if(formato!= null && ubicacion!=null){
                        if(rbOriginal.isSelected() || rbMixto.isSelected() || rbOtros.isSelected()){

                            //EMPAQUE
                            if(chkCaja.isSelected()) caja = true;
                            if(chkManual.isSelected()) manual = true;

                            //ORIGINAL-MIXTO-NO ORIGINAL
                            if(rbOriginal.isSelected()) origen = 1;
                            if(rbMixto.isSelected()) origen = 2;
                            if(rbOtros.isSelected()) origen = 3;

                            //EN DEPOSITO
                            if(chkEnDeposito.isSelected()) endepo = true;

                            //FORMATO
                            int formid = f.search(cmbFormato.getSelectionModel().getSelectedItem());
                            String formatName = "INVALIDO";
                            if(formid<0)
                                formid = f.search(formatName);

                            //IMAGEN
                            if(archImagen!=null)
                                imagen=archImagen.getName();

                            //UBICACION
                            if(ubCtrl.getUbis().isEmpty())
                                ubCtrl.cargarUbicaciones();
                            for(Ubicaciones u: ubCtrl.getUbis())
                                if(u.getId().equals(ubicacion)){
                                    ubaux=u;
                                    break;
                                }

                            //SOFTWARE CONTENIDO
                            SoftwareCtrl sctr = new SoftwareCtrl();
                            if(sctr.getSws().isEmpty())
                                sctr.cargarSoftware();
                            List<Software> contenido = new ArrayList<>();
                            for(String x : lstSwContenido.getItems()){
                                String[] soft = x.split(" - ");
                                Software s = sctr.findSoftware(Integer.parseInt(soft[0]));
                                contenido.add(s);
                            }

                            medio = new Medios(id, nombre, formato, caja, manual,origen,ubaux,endepo,imagen, observ,partes);

                            //ADD MEDIO TO EVERY SOFTWARE
                            for(Software s : contenido)
                                s.getMedios().add(medio);

                            //Se guarda en BD
                            boolean todoOK = meCtrl.altaMedio(id, nombre, formid, caja, manual, origen, ubaux, endepo, imagen, observ, origen, contenido);
                            meCtrl.getMedios().add(medio);

                            //RELOAD CONS MASIVA MEDIOS
                            if(todoOK){
                                boolean cont = PopUp.popUpWarning("Medio ingresado con éxito. ¿Cargar otro?", null, "Cargar otro medio");

                                Stage x = (Stage) window.getScene().getWindow();
                                x.close();
                                if(cont)
                                    controlMenu.altaMedio(new ActionEvent());
                                else
                                    consmasivamed.loadTable();
                            }else{PopUp.popUpError("Algo no se cargó correctamente a la base.", null, null);}
                      }else{ PopUp.popUpError("Selecione el si el medio es original, mixto u otro.", null, null);}
                    }else{ PopUp.popUpError("Selecione el formato y una ubicacion.", null, null);}
                }else{ PopUp.popUpError("Por favor, ingrese un nombre de al menos 3 caracteres", null, null);}
            }else{ PopUp.popUpError("Por favor, ingrese un identificador del medio. ", null, null);}

        }catch(NumberFormatException e){PopUp.popUpError("Por favor, ingrese un numero de partes.", null, null);}
    }

    @FXML
    private void cancelar(MouseEvent event) {
        Stage x = (Stage) window.getScene().getWindow();
        x.close();
    }

    @FXML
    public void reloadTable(){
        SoftwareCtrl swCtrl = new SoftwareCtrl();
        swCtrl.cargarSoftware();
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colVersion.setCellValueFactory(new PropertyValueFactory<>("version"));

        if(swCtrl.getSws()!=null)
            tblSoftwareDisp.getItems().setAll(swCtrl.getSws());
    }

    /******************LISTENERS**********************************************/

        @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_RELEASED) && event.getSource().equals(txtBusqueda)){
            if(txtBusqueda.getText().isEmpty() || txtBusqueda.getText() == null){
                loadTable();
                btnAgregar.setDisable(true);
            }else{
                loadTable(txtBusqueda.getText());
                btnAgregar.setDisable(false);
            }
        }
    }

    /*********************OTHER FUNCTIONS*************************************/

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Seleccionar donde guardar las imagenes");

        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    public void changeBackToConsultaSw(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Vista/ConsultaSoftware.fxml"));
            Node x = loader.load();
            mainWindow.setCenter(x);
        } catch (IOException ex) {
            Logger.getLogger(IOCtrlAltaSwConExtras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadTable(){
        SoftwareCtrl swCtrl = new SoftwareCtrl();
        if(swCtrl.getSws().isEmpty())
            swCtrl.cargarSoftware();
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colVersion.setCellValueFactory(new PropertyValueFactory<>("version"));

        if(swCtrl.getSws()!=null)
            tblSoftwareDisp.getItems().setAll(swCtrl.getSws());
    }

    public void loadTable(String buscar){
        SoftwareCtrl s = new SoftwareCtrl();
        if(s.getSws().isEmpty())
            s.cargarSoftware();
        List<Software> sof = new ArrayList<>();

        if(!buscar.isEmpty())
            for(Software x : s.getSws()){
                if(x.getNombre().toLowerCase().contains(buscar.toLowerCase()))
                    sof.add(x);
            }
        tblSoftwareDisp.getItems().setAll(sof);

        if(tblSoftwareDisp.getItems().isEmpty()){
            boolean newSw = PopUp.popUpWarning("El software que está buscando parece no existir. ¿Desea crearlo?", null, "Crear nuevo software");
            if(newSw){
                controlMenu.altaSoftware(new ActionEvent());
            }
            btnQuitar.setDisable(true);
        }else
            btnQuitar.setDisable(false);
    }

    /********************GETTERS & SETTE**************************************/

    public BorderPane getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(BorderPane mainWindow) {
        this.mainWindow = mainWindow;
    }

    public IOCtrlConsMasivaSw getConsmasivasw() {
        return consmasivasw;
    }

    public void setConsmasivasw(IOCtrlConsMasivaSw consmasivasw) {
        this.consmasivasw = consmasivasw;
    }

    public IOCtrlMenu getControlMenu() {
        return controlMenu;
    }

    public void setControlMenu(IOCtrlMenu controlMenu) {
        this.controlMenu = controlMenu;
    }

    public IOCtrlConsMasivaMedios getConsmasivamed() {
        return consmasivamed;
    }

    public void setConsmasivamed(IOCtrlConsMasivaMedios consmasivamed) {
        this.consmasivamed = consmasivamed;
    }


}

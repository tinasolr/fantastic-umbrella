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
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import swt.DAO.*;
import swt.model.*;
import swt.view.*;

/**
 * FXML Controller class
 *
 * @author tinar
 */
public class IOCtrlConsultaIndividualMedio implements Initializable {

    private MediosCtrl meCtrl;
    private UbicacionesCtrl ubCtrl;
    private MediosDB meDB = new MediosDB();
    private FormatoDB foDB = new FormatoDB();
    private File archImagen;
    private IOCtrlConsMasivaMedios consmasiva;
    private IOCtrlMenu controlMenu;
    private String codigoMedio;
    private Medios m;

    @FXML    private ImageView imagen;
    @FXML    private Label lblCodigo;
    @FXML    private Label lblNombre;
    @FXML    private Label lblOriginal;
    @FXML    private Label lblFormato;
    @FXML    private Label lblPartes;
    @FXML    private Label lblCaja;
    @FXML    private Label lblManual;
    @FXML    private Label lblUbicacion;
    @FXML    private Label lblGuardado;
    @FXML    private Label lblObserv;
    @FXML    private Label fillCodigo;
    @FXML    private Label fillNombre;
    @FXML    private Label fillOriginal;
    @FXML    private Label fillFormato;
    @FXML    private Label fillPartes;
    @FXML    private Label fillCaja;
    @FXML    private Label fillManual;
    @FXML    private Label fillUbicacion;
    @FXML    private Label fillGuardado;
    @FXML    private TextArea fillObserv;
    @FXML    private Button btnExportar;
    @FXML    private Button btnCancelar;
    @FXML    private AnchorPane window;
    @FXML    private TableView<Software> tblSoftware;
    @FXML    private TableColumn<Software, String> colCodigoSw;
    @FXML    private TableColumn<Software, String> colNombreSw;
    @FXML    private TableColumn<Software, String> colVersionSw;
    @FXML    private TableView<CopiasTableFormat> tblCopias;
    @FXML    private TableColumn<?, ?> colCodCop;
    @FXML    private TableColumn<?, ?> colFormCop;
    @FXML    private TableColumn<?, ?> colUbiCop;
    @FXML    private TableColumn<?, ?> colGuardCop;
    @FXML    private TableColumn<?, ?> colObservCop;

    @FXML
    private void exportar(ActionEvent event) {
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage x = (Stage) window.getScene().getWindow();
        x.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        meCtrl = new MediosCtrl();
        if(meCtrl.getMedios().isEmpty())
            meCtrl.cargarMedios();
        m = meCtrl.findMedio(codigoMedio);

        if(m!=null){

//            File fi = new File("./src/images/" + m.getImagen());
//            image.setImage(new Image("file:///" + fi.getAbsolutePath()));
            imagen.setImage(new Image("file:///" + Images.getPath() + File.separator + m.getImagen()));
            fillCodigo.setText(m.getCodigo());
            fillNombre.setText(m.getNombre());
            fillObserv.setText(m.getObserv());
            fillPartes.setText(String.valueOf(m.getPartes()));
            fillCaja.setText(m.isCaja()?"Sí":"No");
            fillGuardado.setText(m.isEnDepo()?"Sí":"No");
            fillManual.setText(m.isManual()?"Sí":"No");
            fillOriginal.setText(m.getOrigen()==1?"Sí":m.getOrigen()==2?"Mixto":"No");
            fillFormato.setText(m.getFormato());
            fillUbicacion.setText(m.getUbiDepo().getId());

            SoftwareCtrl sctrl = new SoftwareCtrl();
            sctrl.softwareDeMedio(m.getCodigo());
            colCodigoSw.setCellValueFactory(new PropertyValueFactory<>("codigo"));
            colVersionSw.setCellValueFactory(new PropertyValueFactory<>("version"));
            colNombreSw.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        if(sctrl.getSwDeMed()!=null)
            tblSoftware.getItems().setAll(sctrl.getSwDeMed());

            CopiasCtrl copctrl = new CopiasCtrl();
            copctrl.cargarCopias(m.getCodigo());

            colCodCop.setCellValueFactory(new PropertyValueFactory<>("id"));
            colFormCop.setCellValueFactory(new PropertyValueFactory<>("formato"));
            colUbiCop.setCellValueFactory(new PropertyValueFactory<>("codUbi"));
            colGuardCop.setCellValueFactory(new PropertyValueFactory<>("enDepo"));
            colObservCop.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

            ArrayList<CopiasTableFormat> copTabla = new ArrayList();
            for(Copias c:copctrl.getCopias())
            {
                copTabla.add(new CopiasTableFormat(c.getId(),c.getFormato(),c.getUbiDepo().getId(),c.isEnDepo(),c.getObserv()));
            }

            if(!copTabla.isEmpty()) tblCopias.getItems().setAll(copTabla);

        }else{PopUp.popUpError("Algo falló. No reconoce el código.", null, null);}

    }

    public MediosCtrl getMeCtrl() {
        return meCtrl;
    }

    public void setMeCtrl(MediosCtrl meCtrl) {
        this.meCtrl = meCtrl;
    }

    public UbicacionesCtrl getUbCtrl() {
        return ubCtrl;
    }

    public void setUbCtrl(UbicacionesCtrl ubCtrl) {
        this.ubCtrl = ubCtrl;
    }

    public MediosDB getMeDB() {
        return meDB;
    }

    public void setMeDB(MediosDB meDB) {
        this.meDB = meDB;
    }

    public FormatoDB getFoDB() {
        return foDB;
    }

    public void setFoDB(FormatoDB foDB) {
        this.foDB = foDB;
    }

    public File getArchImagen() {
        return archImagen;
    }

    public void setArchImagen(File archImagen) {
        this.archImagen = archImagen;
    }

    public IOCtrlConsMasivaMedios getConsmasiva() {
        return consmasiva;
    }

    public void setConsmasiva(IOCtrlConsMasivaMedios consmasiva) {
        this.consmasiva = consmasiva;
    }

    public IOCtrlMenu getControlMenu() {
        return controlMenu;
    }

    public void setControlMenu(IOCtrlMenu controlMenu) {
        this.controlMenu = controlMenu;
    }

    public String getCodigoMedio() {
        return codigoMedio;
    }

    public void setCodigoMedio(String codigoMedio) {
        this.codigoMedio = codigoMedio;
    }

    public Medios getMedio() {
        return m;
    }

    public void setMedio(Medios m) {
        this.m = m;
    }

}

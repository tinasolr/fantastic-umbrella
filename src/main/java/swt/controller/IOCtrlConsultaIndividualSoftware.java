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

import swt.DAO.SistOpDB;
import swt.model.Software;
import swt.model.Extras;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author tinar
 */
public class IOCtrlConsultaIndividualSoftware implements Initializable {

    private int codigoSW;
    private IOCtrlConsMasivaSw consMasivaSw;
    private SoftwareCtrl swCtrl;
    private Software s;
    @FXML    private ScrollPane scrollPane;
    @FXML    private VBox vboxSw;
    @FXML    private TitledPane swPane;
    @FXML    private BorderPane bpSoftware;
    @FXML    private Pane panelSuperior;
    @FXML    private ListView<String> lstSistemasOp = new ListView<>();
    @FXML    private Label lblNombreSw;
    @FXML    private Label lblVersionSw;
    @FXML    private TitledPane extrasPane;
    @FXML    private BorderPane bpExtras;
    @FXML    private AnchorPane panelInferior;
    @FXML    private TableView<Extras> tblExtras;
    @FXML    private TableColumn<Extras, String> colNombre;
    @FXML    private TableColumn<Extras, String> colVersion;
    @FXML    private TableColumn<Extras, String> colPartes;
    @FXML    private TableColumn<Extras, String> colDescrip;
    @FXML    private Button btnExportar;
    @FXML    private Button btnCancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        swCtrl = new SoftwareCtrl();
        if(swCtrl.getSws().isEmpty())
            swCtrl.cargarSoftware();

        s = swCtrl.findSoftware(codigoSW);
        lblNombreSw.setText(s.getNombre());
        lblVersionSw.setText(s.getVersion());
        SistOpDB so = new SistOpDB();
        List<SistOpDB> sos1 = so.sistopDeSoftware(codigoSW);
        sos1.forEach((x) -> { lstSistemasOp.getItems().add(x.getNombre());});
        loadTable();

    }

    public void loadTable(){
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
        colPartes.setCellValueFactory(new PropertyValueFactory<>("partes"));
        colDescrip.setCellValueFactory(new PropertyValueFactory<>("descrip"));
        if(s!=null && !s.getExtras().isEmpty())
            tblExtras.getItems().setAll(s.getExtras());
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage x = (Stage) vboxSw.getScene().getWindow();
        x.close();
    }

    @FXML
    private void exportar(ActionEvent event) {
    }
    
    public int getCodigoSW() {
        return codigoSW;
    }

    public void setCodigoSW(int codigoSW) {
        this.codigoSW = codigoSW;
    }

    public IOCtrlConsMasivaSw getConsMasivaSw() {
        return consMasivaSw;
    }

    public void setConsMasivaSw(IOCtrlConsMasivaSw consMasivaSw) {
        this.consMasivaSw = consMasivaSw;
    }


}

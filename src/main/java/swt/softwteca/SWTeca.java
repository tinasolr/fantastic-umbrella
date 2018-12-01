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
package swt.softwteca;

import java.io.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import org.apache.log4j.*;
import swt.controller.*;


public class SWTeca extends Application {

    public static Stage primaryStage;
    public static Parent root;
    private static Logger log = Logger.getLogger(Logger.class);

    @Override
    public void start(Stage primaryStage) throws Exception {

        SWTeca.primaryStage = primaryStage;
        SWTeca.primaryStage.setTitle("SoftwareTeca");
        initRoot();
        log.trace("start");
    }

       public void initRoot(){
        try{

            FXMLLoader loader = new FXMLLoader();

            root = loader.load(getClass().getResource("/fxml/Menu.fxml"));

            Scene scene = new Scene(root, 1056, 824);
            scene.getStylesheets().add("/styles/Styles.css");
            primaryStage.setScene(scene);
            primaryStage.show();
//                    FileChooser fileChooser = new FileChooser();
//        File arch = fileChooser.showOpenDialog(new Stage());
//        if(arch!=null)
//            CSVCtrl.readDataLineByLine(arch.getAbsolutePath());
        }catch(IOException e){
            e.printStackTrace();
        }
        log.trace("initializing");
    }
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SoftwareCtrl().cargarSoftware();
        log.trace("MAIN // LOADING SOFTWARE");
        new UbicacionesCtrl().cargarUbicaciones();
        log.trace("MAIN // LOADING UBICACIONES");
        new MediosCtrl().cargarMedios();
        log.trace("MAIN // LOADING MEDIOS");
        Images.getPathFromDB();
        log.trace("MAIN // GETTING IMAGE FOLDER PATH: " + Images.getPath());
        launch(args);
        log.trace("LAUNCH");

    }

}

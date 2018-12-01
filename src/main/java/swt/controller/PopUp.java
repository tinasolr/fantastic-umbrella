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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * @author tinar
 */
public class PopUp {

    /**
     *
     * @param text
     * @param header
     * @param title
     */
    public static void popUpError(String text, String header, String title){
        Alert alert = new Alert(Alert.AlertType.ERROR, text);
        alert.setHeaderText(header);
        if (title == null || title.isEmpty())
            alert.setTitle("Error");
        else
            alert.setTitle(title);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    /**
     *
     * @param text
     * @param header
     * @param title
     */
    public static void popUpInfo(String text, String header, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.setHeaderText(header);
        if (title == null || title.isEmpty())
            alert.setTitle("Informacion");
        else
            alert.setTitle(title);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }

    /**
     *
     * @param texto
     * @param header
     * @param title
     * @return
     */
    public static boolean popUpWarning(String texto, String header, String title){
        Alert alert = new Alert(Alert.AlertType.WARNING, texto, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(header);
        if (title == null || title.isEmpty())
            alert.setTitle("Advertencia");
        else
            alert.setTitle(title);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            alert.close();
            return true;
        }
        alert.close();
        return false;
    }

    /**
     *
     * @param text
     * @param header
     * @param title
     * @return
     */
    public static boolean popUpConfirm(String text, String header, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text);
        alert.setHeaderText(header);
        if (title == null || title.isEmpty())
            alert.setTitle("Confirmar");
        else
            alert.setTitle(title);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.OK){
            alert.close();
            return true;
        }else{
            alert.close();
            return false;
        }
    }

    public static void popUpException(Exception ex){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setContentText(ex.getMessage());

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}

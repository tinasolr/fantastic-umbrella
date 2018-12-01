/*
 * Copyright (C) 2018 Agustina y Nicolas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package swt.view;

/**
 *
 * @author tinar
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;

public class DynamicTable extends Application {

    @Override
    public void start(Stage primaryStage) {
        final BorderPane root = new BorderPane();
        final TableView<ObservableList<StringProperty>> table = new TableView<>();

        final TextField urlTextEntry = new TextField();
        final CheckBox headerCheckBox = new CheckBox("Tiene encabezado");
        final Button btnPopulate = new Button("Ver");
        final Button btnImportar = new Button("Importar");
        urlTextEntry.setEditable(false);
        FileChooser fileChooser = new FileChooser();
        File arch = fileChooser.showOpenDialog(new Stage());
        if(arch!=null){
            urlTextEntry.setText(arch.getAbsolutePath());
        }
        btnPopulate.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
              populateTable(table, urlTextEntry.getText(),
                headerCheckBox.isSelected());
        }
      });
        btnImportar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
      HBox controls = new HBox();
      controls.getChildren().addAll(urlTextEntry, headerCheckBox, btnPopulate);
      HBox.setHgrow(urlTextEntry, Priority.ALWAYS);
      HBox.setHgrow(headerCheckBox, Priority.NEVER);
      HBox.setHgrow(btnPopulate, Priority.NEVER);
      root.setTop(controls);
      root.setCenter(table);
      root.setBottom(btnImportar);
      Scene scene = new Scene(root, 600, 400);
      primaryStage.setScene(scene);
      primaryStage.show();
    }

    private void populateTable(final TableView<ObservableList<StringProperty>> table,
          final String urlSpec, final boolean hasHeader) {
      table.getItems().clear();
      table.getColumns().clear();
      table.setPlaceholder(new Label("Loading..."));
      Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          BufferedReader in = getReaderFromUrl("file:///"+urlSpec);
          // Header line
          if (hasHeader) {
            final String headerLine = in.readLine();
            final String[] headerValues = headerLine.split(",");
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                for (int column = 0; column < headerValues.length; column++) {
                  table.getColumns().add(
                      createColumn(column, headerValues[column]));
                }
              }
            });
          }

          // Data:

          String dataLine;
          while ((dataLine = in.readLine()) != null) {
            Pattern pattern = Pattern.compile("[^\"]*\"|[^,]+");
            List<String> dataValues = new ArrayList<String>();
            Matcher m = pattern.matcher(dataLine);
            while (m.find()) {
                dataValues.add(m.group());
            }
//            final String[] dataValues = dataLine.split("[^\"]*\"|[^,]+");
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                // Add additional columns if necessary:
                for (int columnIndex = table.getColumns().size(); columnIndex < dataValues.size(); columnIndex++) {
                  table.getColumns().add(createColumn(columnIndex, ""));
                }
                // Add data to table:
                ObservableList<StringProperty> data = FXCollections
                    .observableArrayList();
                for (String value : dataValues) {
                  data.add(new SimpleStringProperty(value));
                }
                table.getItems().add(data);
              }
            });
          }
          return null;
        }
      };
      Thread thread = new Thread(task);
      thread.setDaemon(true);
      thread.start();
  }

  private TableColumn<ObservableList<StringProperty>, String> createColumn(
      final int columnIndex, String columnTitle) {
    TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
    String title;
    if (columnTitle == null || columnTitle.trim().length() == 0) {
      title = "Column " + (columnIndex + 1);
    } else {
      title = columnTitle;
    }
    column.setText(title);
    column
        .setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
            ObservableList<StringProperty> values = cellDataFeatures.getValue();
            if (columnIndex >= values.size()) {
              return new SimpleStringProperty("");
            } else {
              return cellDataFeatures.getValue().get(columnIndex);
            }
          }
        });
    return column;
  }

  private BufferedReader getReaderFromUrl(String urlSpec) throws Exception {
    URL url = new URL(urlSpec);
    URLConnection connection = url.openConnection();
    InputStream in = connection.getInputStream();
    return new BufferedReader(new InputStreamReader(in));
  }

}
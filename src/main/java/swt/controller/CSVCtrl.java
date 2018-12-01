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

import com.opencsv.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import swt.DAO.*;
import swt.view.*;

/**
 * @author tinar
 */
public class CSVCtrl {

    private SoftwareCtrl sctrl = new SoftwareCtrl();

    public void writeSoftwareDataLineByLine(String filePath){
        // first create file object for file placed at location
        // specified by filepath
        String fileSeparator = System.getProperty("file.separator");

        File file = new File(filePath + fileSeparator + "SWTsoftware-" +
            LocalDateTime.now().toLocalDate()
            .format(DateTimeFormatter.BASIC_ISO_DATE) +".csv");
        try {
            // create FileWriter object with file as parameter
//            FileWriter outputfile = new FileWriter(file);
            OutputStreamWriter outputfile = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            //s.sw_id, sw_nom, sw_vers, so_nom, extra_nom, extra_vers, extra_descr, extra_partes
            String[] header = { "Codigo", "Nombre", "Version","Sistema Operativo", "Utilidades/Drivers"};
            writer.writeNext(header);
            String[] header2 = { "", "", "","", "Nombre", "Version", "Descripcion", "Partes" };
            writer.writeNext(header2);

            for(SoftwarePOJO s : new ReportesDB().readSoftwareReport()){
                String[] dataRow = { String.valueOf(s.getCodigo()), s.getNombre(),
                    s.getVersion(),s.getSistOp(), s.getExNomb(), s.getExVers(),
                    s.getExDescr(), String.valueOf(s.getExPartes())};
                writer.writeNext(dataRow);
            }
            // closing writer connection
            writer.close();
            PopUp.popUpInfo("El archivo se encuentra en " + file.getPath(), null, "Éxito!");
        }
        catch (IOException e) {
            PopUp.popUpException(e);
        }
    }

    public void writeMediosDataLineByLine(String filePath){
        // first create file object for file placed at location
        // specified by filepath
        String fileSeparator = System.getProperty("file.separator");

        File file = new File(filePath + fileSeparator + "SWTmedios-" +
            LocalDateTime.now().toLocalDate()
            .format(DateTimeFormatter.BASIC_ISO_DATE) +".csv");

        try {
            // create FileWriter object with file as parameter
            OutputStreamWriter outputfile = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = { "Codigo", "Nombre", "Software", "", "Partes", "Manual", "Caja", "Imagen", "Observaciones", "Formato", "Original", "Ubicacion", ""};
            writer.writeNext(header);
            String[] header2 = { "", "", "Nombre", "Version", "", "", "", "", "", "", "", "Codigo", "Observaciones"};
            writer.writeNext(header2);

            //m.medio_id, medio_nom, sw_nom, sw_vers, medio_partes, medio_manual, medio_caja, medio_imagen, medio_obs, form_nom, origen_id, u.ubi_id, ubi_obs
            for(MediosPOJO m : new ReportesDB().readMediosReport()){
                String[] dataRow = { m.getCodigo(), m.getNombre(), m.getSwNomb(),
                m.getSwVers(), m.getPartes(), m.isManual()?"Si":"No",
                m.isCaja()?"Si":"No", m.getImagen().contains("no-image")?"No":m.getImagen(),
                m.getObserv(), m.getFormato(),
                m.getOrigen().contains("1")?"Si":m.getOrigen().contains("2")?"Mixto":"No",
                m.getUbicacion(), m.getUbiObserv()};
                writer.writeNext(dataRow);
            }

            // closing writer connection
            writer.close();
            PopUp.popUpInfo("El archivo se encuentra en " + file.getPath(), null, "Éxito!");
        }
        catch (IOException e) {
            PopUp.popUpException(e);
        }
    }

    public void writeCopiasDataLineByLine(String filePath){
        // first create file object for file placed at location
        // specified by filepath
        String fileSeparator = System.getProperty("file.separator");

        File file = new File(filePath + fileSeparator + "SWTcopias-" +
            LocalDateTime.now().toLocalDate()
            .format(DateTimeFormatter.BASIC_ISO_DATE) +".csv");
        try {
            // create FileWriter object with file as parameter
            OutputStreamWriter outputfile = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
//copia_id, m.medio_id, medio_nom, cp_obs, form_nom, u.ubi_id, ubi_obs, medio_manual, medio_caja, medio_imagen
            String[] header = { "Codigo", "Formato", "Observaciones",
                "Ubicacion","", "Medio", "", "", "", ""};
            writer.writeNext(header);
            String[] header2 = { "", "", "", "Codigo","Observaciones",
                "Codigo", "Nombre", "Manual", "Caja", "Imagen" };
            writer.writeNext(header2);

            for(CopiasPOJO s : new ReportesDB().readCopiasReport()){
                String[] dataRow = { s.getCodigo(), s.getFormato(), s.getObserv(),
                    s.getUbicacion(),s.getUbiObserv(), s.getMedCodigo(),
                    s.getMedNomb(), s.isManual()?"Si":"No", s.isCaja()?"Si":"No",
                    s.getImagen() };
                writer.writeNext(dataRow);
            }
            // closing writer connection
            writer.close();
            PopUp.popUpInfo("El archivo se encuentra en " + file.getPath(), null, "Éxito!");
        }
        catch (IOException e) {
            PopUp.popUpException(e);
        }
    }

    public static void readDataLineByLine(String file){

        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            //s.sw_id, sw_nom, sw_vers, so_nom, extra_nom, extra_vers, extra_descr, extra_partes

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readMediosToBean(String file){

        String fileName = file;
        Path myPath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<Car> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Car.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(Car.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Car> cars = csvToBean.parse();

            cars.fo
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}

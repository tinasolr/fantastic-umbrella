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
package swt.DAO;

import swt.view.SoftwarePOJO;
import swt.view.MediosPOJO;
import swt.view.CopiasPOJO;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author tinar
 */
public class ReportesDB extends DBObject{

    public List<SoftwarePOJO> readSoftwareReport(){

        List<SoftwarePOJO> softrep = new ArrayList<>();

        try {
            if(conn==null || conn.isClosed())
                connect();

            //s.sw_id, sw_nom, sw_vers, so_nom, extra_nom, extra_vers, extra_descr, extra_partes
            CallableStatement sp = conn.prepareCall("CALL report_software()");
            try (ResultSet res = sp.executeQuery()) {
                while(res.next()){
                    int codigo = res.getInt(1);
                    String nombre = res.getString(2);
                    String version = res.getString(3);
                    String sistOp = res.getString(4);
                    String exNomb = res.getString(5);
                    String exVers = res.getString(6);
                    String exDescr = res.getString(7);
                    int exPartes = res.getInt(8);
                    softrep.add(new SoftwarePOJO(codigo, nombre, sistOp, version, exNomb, exVers, exDescr, exPartes));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportesDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return softrep;
    }

     public List<MediosPOJO> readMediosReport(){

        List<MediosPOJO> medrep = new ArrayList<>();

        try {
            if(conn==null || conn.isClosed())
                connect();

    //m.medio_id, medio_nom, sw_nom, sw_vers, medio_partes, medio_manual, medio_caja, medio_imagen, medio_obs, form_nom, origen_id, u.ubi_id, ubi_obs
            CallableStatement sp = conn.prepareCall("CALL report_medios()");
            try (ResultSet res = sp.executeQuery()) {
                while(res.next()){
                    String codigo = res.getString(1);
                    String nombre = res.getString(2);
                    String swNomb = res.getString(3);
                    String swVers = res.getString(4);
                    int partes = res.getInt(5);
                    boolean manual = res.getBoolean(6);
                    boolean caja = res.getBoolean(7);
                    String imagen = res.getString(8);
                    String observ = res.getString(9);
                    String formato = res.getString(10);
                    int origen = res.getInt(11);
                    String ubicacion = res.getString(12);
                    String ubiObserv = res.getString(13);

                    medrep.add(new MediosPOJO(codigo, nombre, swNomb, swVers,
                        String.valueOf(partes), manual, caja, imagen, observ, formato,
                        String.valueOf(origen), ubicacion, ubiObserv));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportesDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return medrep;
    }

       public List<CopiasPOJO> readCopiasReport(){

        List<CopiasPOJO> coprep = new ArrayList<>();

        try {
            if(conn==null || conn.isClosed())
                connect();

            //copia_id, m.medio_id, medio_nom, cp_obs, form_nom, u.ubi_id, ubi_obs, medio_manual, medio_caja, medio_imagen
            CallableStatement sp = conn.prepareCall("CALL report_copias()");
            try (ResultSet res = sp.executeQuery()) {
                while(res.next()){
                    int codigo = res.getInt(1);
                    String medCodigo = res.getString(2);
                    String medNomb = res.getString(3);
                    String observ = res.getString(4);
                    String formato = res.getString(5);
                    String ubicacion = res.getString(6);
                    String ubiObserv = res.getString(7);
                    boolean manual = res.getBoolean(8);
                    boolean caja = res.getBoolean(9);
                    String imagen = res.getString(10);

                    coprep.add(new CopiasPOJO(String.valueOf(codigo), medCodigo,
                        medNomb, observ, formato, ubicacion, ubiObserv,
                        manual, caja, imagen));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportesDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ReportesDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return coprep;
    }

    @Override
    public <E> E readResultSet(ResultSet res) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void executeWrite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void executeUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void executeDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String executeSearch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

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

import java.sql.*;
import java.util.*;
import org.apache.log4j.*;

/**
 *
 * @author tinar
 */
public class MediosDB {

    private static Logger log = Logger.getLogger(Logger.class);
    private static final String TABLE = "medios";
    private String id;
    private String nuevoId;
    private int formid;
    private boolean caja;
    private String imagen;
    private boolean manual;
    private String nombre;
    private String observ;
    private int partes;
    private int origen;
    private int swid;
    private String ubic;
    private boolean enDepo;

    public MediosDB() { }

    public MediosDB(String id, int formid, boolean caja, String imagen, boolean manual, String nombre, String observ, int partes, int origen) {
        this.id = id;
        this.formid = formid;
        this.caja = caja;
        this.imagen = imagen;
        this.manual = manual;
        this.nombre = nombre;
        this.observ = observ;
        this.partes = partes;
        this.origen = origen;
    }

    /**
     * Searched in the database for all medios that contain the software.
     * @param sw_id codigo del software
     * @return listado de medios que lo contienen
     */
    public List<MediosDB> mediosDeSoftware(int sw_id){

        ResultSet res = null;
        List<MediosDB> objDB = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        String SQL = "Select medio_id from Medios_Software AS s WHERE s.sw_id = ?";
        String SQL2 = "Select * from `medios` AS s WHERE s.medio_id LIKE ?";

        log.trace("Searching for medios containing software " + sw_id);

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL); ) {

            p.setInt(1, sw_id);
            res = p.executeQuery();

            while(res.next()){
                ids.add(res.getString(1));
            }

            ResultSet res2 = null;
            PreparedStatement p2 = conn.prepareStatement(SQL2);

            for(String x : ids){
                p2.setString(1, x);
                res2 = p2.executeQuery();
                if(res2.first()){
                    objDB.add(readResultSet(res2));
                }
            }
            log.trace("Done.");

        } catch (SQLException e) {log.error(e.getMessage()); e.printStackTrace(); }

        return objDB;
    }

    /**
     * Creates relation medio-software.
     * @param medioID codigo del medio
     * @param swID codigo del software
     * @return verdadero si lo asocio, falso si no.
     */
    public boolean asociarMedioASoftware(String medioID, int swID){

        log.trace("Associating medio and software...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL asociar_medio_software(?, ?)}");) {

            sp.setString(1, medioID);
            sp.setInt(2, swID);

            sp.executeUpdate();
            log.trace("Done.");
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Eliminates relation medio-software.
     * @param medioID codigo del medio
     * @param swID codigo del software
     * @return verdadero si lo desasocio, falso si no.
     */
    public boolean desasociarMedioASoftware(String medioID, int swID){

        log.trace("Eliminating association medio - software...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL desasociar_medio_software(?, ?)}");) {

            sp.setString(1, medioID);
            sp.setInt(2, swID);

            sp.executeUpdate();
            log.trace("Done.");

        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean asociarUbicacionAMedio(String medioID, String ubic, boolean enDepo){

        log.trace("Associating medio and ubicacion...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL asociar_medio_ubicacion(?, ?, ?)}");) {

            sp.setString(1, medioID);
            sp.setString(2, ubic);
            sp.setBoolean(3, enDepo);

            sp.executeUpdate();
            log.trace("Done.");

        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean desasociarUbicacionAMedio(String medioID, String ubicID){

        log.trace("Eliminating association medio and ubicacion...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL desasociar_medio_ubicacion(?, ?)}");) {

            sp.setString(1, medioID);
            sp.setString(2, ubicID);

            sp.executeUpdate();
              log.trace("Done.");

        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
          return false;
        }
        return true;
    }

    /**
     * Read all medios from database.
     * @return List of MediosDB.
     */
    public List<MediosDB> read() {

        log.trace("Reading medios...");

        List<MediosDB> objetos = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE;

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet rs = p.executeQuery();) {

            log.trace("Connected.");

            while (rs.next()) {
                objetos.add(readResultSet(rs));
            }

            log.trace("Finished reading medios.");

        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return objetos;
    }

    public MediosDB readResultSet(ResultSet res) {
        MediosDB obj = null;

         try {

            this.id = res.getString("medio_id");
            this.formid = res.getInt("form_id");
            this.caja = res.getBoolean("medio_caja");
            this.imagen = res.getString("medio_imagen");
            this.manual = res.getBoolean("medio_manual");
            this.observ = res.getString("medio_obs");
            this.partes = res.getInt("medio_partes");
            this.origen = res.getInt("origen_id");
            this.nombre = res.getString("medio_nom");

            obj = new MediosDB(id, formid, caja, imagen, manual, nombre, observ, partes, origen);

        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return obj;
    }

    public boolean isMedioEnDepo(String codigo) {

        boolean esta = false;
        ResultSet rs = null;
        String SQL = "SELECT ubi_id, medio_enDep FROM medio_ubic WHERE medio_id LIKE ?";

        log.trace("Checking if medio is in depo...");
        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);){
            p.setString(1, codigo);
            rs = p.executeQuery();

            if(rs.first()){
                this.ubic = rs.getString(1);
                esta = rs.getBoolean(2);
            }
            log.trace("medio " + (esta? "is":"is not") + " en depo.");

        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return esta;
    }

    public void write(String id, String nombre, int partes, boolean manual, boolean caja, String imagen, String observ, int formid, int origen) {
        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call alta_medio (?, ?, ?, ?, ?, ?, ?, ?, ?)}"); ) {

            log.trace("Creating medio...");
            sp.setString(1, id);
            sp.setString(2, nombre);
            sp.setInt(3,partes);
            sp.setBoolean(4,manual);
            sp.setBoolean(5,caja);
            if(imagen != null && !imagen.isEmpty())
                sp.setString(6, imagen);
            else
                sp.setString(6, "no-image-available.png");
            sp.setString(7, observ);
            sp.setInt(8,formid);
            sp.setInt(9,origen);
            sp.executeUpdate();
            log.trace("Done creating medio.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String id, String nuevoID, String nombre, int partes,
        boolean manual, boolean caja, String imagen, String observ,
        int formid, int origen) {

        log.trace("Updating medios...");
        try ( Connection conn = DataSource.getConnection() ) {

            CallableStatement sp = conn.prepareCall("{call mod_medio (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            sp.setString(1, nuevoID);
            sp.setString(2, id);
            sp.setString(3, nombre);
            sp.setInt(4,partes);
            sp.setBoolean(5,manual);
            sp.setBoolean(6,caja);
            if(imagen != null && !imagen.isEmpty())
                sp.setString(7, imagen);
            else
                sp.setString(7, "no-image-available.png");
            sp.setString(8, observ);
            sp.setInt(9,formid);
            sp.setInt(10,origen);
            sp.executeUpdate();
        log.trace("Done updating medios.");

        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void delete(String id) {

        log.trace("Deleting medio...");

        try ( Connection conn = DataSource.getConnection()) {

            CallableStatement sp = conn.prepareCall("{call elim_medio (?)}");
            sp.setString(1, id);
            sp.executeUpdate();
            log.trace("Done deleting medio.");

        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String search(String nombre) {
        String result = null;
        ResultSet res = null;
        log.trace("Searching for medios: " + nombre + "...");
        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call find_medioid(?,?)}");) {

            sp.setString(1, nombre);
            res = sp.executeQuery();
            int r = -1;
            if(res.first())
                r = res.getInt(1);
            result = String.valueOf(r);
            log.trace("Medio found - id: " + result);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    public String getId() { return id;  }
    public void setId(String id) {        this.id = id;    }
    public int getFormid() {        return formid;    }
    public void setFormid(int formid) {        this.formid = formid;    }
    public boolean isCaja() {        return caja;    }
    public void setCaja(boolean caja) {        this.caja = caja;    }
    public String getImagen() {        return imagen;    }
    public void setImagen(String imagen) {        this.imagen = imagen;    }
    public boolean isManual() {        return manual;    }
    public void setManual(boolean manual) {        this.manual = manual;    }
    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }
    public String getObserv() {        return observ;    }
    public void setObserv(String observ) {        this.observ = observ;    }
    public int getPartes() {        return partes;    }
    public void setPartes(int partes) {        this.partes = partes;    }
    public int getOrigen() {        return origen;    }
    public void setOrigen(int origen) {        this.origen = origen;    }
    public int getSwid() {        return swid;    }
    public void setSwid(int swid) {        this.swid = swid;    }
    public String getUbic() {        return ubic;    }
    public void setUbic(String ubic) {        this.ubic = ubic;    }
    public boolean isEnDepo() {        return enDepo;   }
    public void setEnDepo(boolean enDepo) {        this.enDepo = enDepo;    }
    public String getNuevoId() {        return nuevoId;    }
    public void setNuevoId(String nuevoId) {        this.nuevoId = nuevoId;    }
}

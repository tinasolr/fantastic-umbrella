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
public class CopiasDB {

    private static Logger log = Logger.getLogger(Logger.class);
    private static final String TABLE = "copias";

    private int id;
    private String medioid;
    private int formid;
    private String obs;
    private String ubi;
    private boolean enDepo;

    private CopiasDB(int id, String obs, int formid, String swid, String ubi, boolean enDepo) {
        this.id = id;
        this.medioid = swid;
        this.obs = obs;
        this.formid = formid;
        this.ubi = ubi;
        this.enDepo = enDepo;
    }

    public void asociarUbicacionACopia(int id, String ubi, boolean enDepo){
        log.trace("Creating ubicacion-copia relationship...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall(
            "{CALL asociar_copia_ubicacion(?, ?, ?)}");) {

            sp.setInt(1, id);
            sp.setString(2, ubi);
            sp.setBoolean(3, enDepo);

            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        log.trace("Relationship created.");

    }

    public void desasociarUbicacionACopia(int id){
        log.trace("Elimination ubicacion-copia relationship...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall(
                "{CALL desasociar_copia_ubicacion(?)}");){

            sp.setInt(1, id);
            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        log.trace("Relationship deleted.");

    }

    public void actualizarUbicacionACopia(int id, String ubi, boolean enDepo) {
        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL mod_copia_Ubic(?, ?, ?)}");) {

            sp.setInt(1, id);
            sp.setString(2, ubi);
            sp.setBoolean(3, enDepo);

            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int buscarUltimoID() {
        String SQL = "select max(copia_id) from Copias";
        log.trace("Getting last copia id...");

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet r = p.executeQuery();) {

            if(r.first()){
                int id = r.getInt(1);
                log.trace("Las copia ID: " + id);
                return id;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    public CopiasDB() {}

    public List<CopiasDB> copiasDeMedio(String medio_id) {
        ResultSet res = null;
        List<CopiasDB> objDB = new ArrayList<>();
        log.trace("Reading copias of medio: " + medio_id);

        String SQL = "SELECT * FROM `Copias` WHERE `medio_id` LIKE ?";

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ) {

            p.setString(1, medio_id);

            ResultSet r = p.executeQuery();

            while(res.next()){
                objDB.add(readResultSet(res));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        log.trace("Done reading copias of medio.");

        return objDB;
    }

    /**
     * Read all medio copies from database.
     * @return List of CopiasDB.
     */
    public List<CopiasDB> read() {
        log.trace("Reading copias...");

        List<CopiasDB> objetos = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE;

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet res = p.executeQuery();) {

            while (res.next()) {
                objetos.add(readResultSet(res));
            }
            log.trace("Finished reading copias.");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objetos;
    }

    public CopiasDB readResultSet(ResultSet res){
        CopiasDB obj = null;
        String SQL = "SELECT ubi_id, cp_enDep from copia_ubic where cp_id = ?";

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);) {

            this.id = res.getInt("copia_id");
            this.obs = res.getString("cp_obs");
            this.formid = res.getInt("form_id");
            this.medioid = res.getString("medio_id");

            PreparedStatement p2 = conn.prepareStatement(SQL);
            p2.setInt(1, id);
            ResultSet r2 = p2.executeQuery();
            if(r2.first()){
                this.ubi = r2.getString(1);
                this.enDepo = r2.getBoolean(2);
            }
            obj = new CopiasDB(id, obs, formid, medioid, ubi, enDepo);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public void write(String medioid, int formid, String obs) {
        log.trace("Creating copia...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL alta_copia(?, ?, ?)}");) {

            sp.setString(1, medioid);
            sp.setInt(2, formid);
            sp.setString(3, obs);

            sp.executeUpdate();
        log.trace("Copia created.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(int id, String medioid, int formid, String obs) {
        log.trace("Updating copia...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL mod_copia (?, ?, ?, ?)}");) {

            sp.setInt(1, id);
            sp.setString(2, medioid);
            sp.setInt(3, formid);
            sp.setString(4, obs);

            sp.executeUpdate();
            log.trace("Copia updated.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(int id) {
        log.trace("Deleting copia...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL elim_copia (?)}");){

            sp.setInt(1, id);
            sp.executeUpdate();
            log.trace("Copia deleted.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String search(String medioid, int formid) {
        log.trace("Searching for copia...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL get_idcopia (?, ?)}");){

            sp.setString(1, medioid);
            sp.setInt(2, formid);

            ResultSet r = sp.executeQuery();
            int id = -1;
            if(r.first()){
                id = r.getInt(1);
                log.trace("Copia found ID: " + id);
                return String.valueOf(id);
            }else{
                log.trace("Copia not found.");
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String findubiid (int copia_id) {
        log.trace("Searching for ubicacion of copia...");

        String SQL = "SELECT ubi_id FROM `copia_ubic` WHERE `cp_id` = ?";

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);){

            p.setInt(1, copia_id);
            ResultSet r = p.executeQuery();
            if(r.first()){
                String ubi = r.getString(1);
                log.trace("Copia in ubicacion: " + ubi);
                return String.valueOf(ubi);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getId() {        return id;    }
    public void setId(int id) {        this.id = id;    }
    public String getMedioid() {        return medioid;    }
    public void setMedioid(String medioid) {        this.medioid = medioid;    }
    public int getFormid() {        return formid;    }
    public void setFormid(int formid) {        this.formid = formid;    }
    public String getObs() {        return obs;    }
    public void setObs(String obs) {        this.obs = obs;    }
    public String getUbi() {        return ubi;    }
    public void setUbi(String ubi) {        this.ubi = ubi;    }
    public boolean isEnDepo() {        return enDepo;    }
    public void setEnDepo(boolean enDepo) {        this.enDepo = enDepo;    }
}

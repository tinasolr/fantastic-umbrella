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

/**
 *
 * @author tinar
 */
public class UbicacionesDB {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(org.apache.log4j.Logger.class);
    private static final String TABLE = "ubicaciones";

    private String codUbi;
    private String obsUbi;
    private String newCod;
    private String newObs;

    public UbicacionesDB() {
    }

    public UbicacionesDB(String codUbi, String obsUbi) {
        this.codUbi = codUbi;
        this.obsUbi = obsUbi;
    }

    /**
     * Read all formatos from database.
     * @return List of Strings.
     */
    public List<UbicacionesDB> read() {
        log.trace("Reading ubicaciones...");
        List<UbicacionesDB> objetos = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE;

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet rs = p.executeQuery();) {

            while (rs.next()) {
                objetos.add(readResultSet(rs));
            }
            log.trace("Finished reading ubicaciones.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objetos;
    }

    public UbicacionesDB readResultSet(ResultSet res) {
        UbicacionesDB obj = null;

        try {

            this.codUbi = res.getString("ubi_id");
            this.obsUbi = res.getString("ubi_obs");

            obj = new UbicacionesDB(codUbi, obsUbi);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    public void write(String codUbi, String obsUbi) {
        log.trace("Creating ubicacion...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call alta_ubicacion(?,?)}");) {

            sp.setString(1, codUbi);
            sp.setString(2, obsUbi);

            sp.executeUpdate();
        log.trace("Ubicacion created.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String codUbi, String newCod, String newObs) {
        log.trace("Updating ubicacion...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL mod_ubicacion(?, ?, ?)}");){

            sp.setString(1, codUbi);
            sp.setString(2, newCod);
            sp.setString(3, newObs);

            sp.executeUpdate();
        log.trace("Ubicacion updated.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String codUbi, String newCod) {
        log.trace("Deleting ubicaciones...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{CALL eliminar_ubicacion(?,?)}");){

            sp.setString(1, codUbi);
            sp.setString(2, newCod);
            sp.executeUpdate();
        log.trace("Ubicacion deleted.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param searchby either ID or Description
     * @param getID true if searching for ID, false if searching for description
     * @return searched value or null
     */
    public String search(String searchby, boolean getID) {
        log.trace("Searching for ubicacion" + searchby + "...");

        String SPROC_OBS = "{CALL get_ubiobs (?)}";
        String SPROC_ID = "{CALL get_ubiid (?)}";

        try ( Connection conn = DataSource.getConnection();){

            CallableStatement sp = null;
            if(getID)
                sp = conn.prepareCall(SPROC_ID);
            else
                sp = conn.prepareCall(SPROC_OBS);

            sp.setString(1, searchby);
            ResultSet r = sp.executeQuery();

            if(r.first())
                return  r.getString(1);
        log.trace("Done searching.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int cantidadMediosEnUbicacion(String id){
        int cant = -1;
        log.trace("Searching cantidad de medio in ubicacion...");

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement sp = conn.prepareStatement(
            "SELECT COUNT(ubi_id) FROM medio_ubic "
            + "WHERE ubi_id LIKE ? GROUP BY ubi_id");){

            sp.setString(1, id);

            ResultSet r = sp.executeQuery();

            if(r.first())
                cant = r.getInt(1);
            else
                cant=0;
            log.trace("Result: " + cant);

            return cant;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cant;
    }

    public int cantidadCopiasEnUbicacion(String id){
        int cant = -1;
        log.trace("Searching cantidad de copias in ubicacion...");

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement sp = conn.prepareStatement(
            "SELECT COUNT(ubi_id) FROM copia_ubic "
            + "WHERE ubi_id LIKE ? GROUP BY ubi_id");){

            sp.setString(1, id);
            ResultSet r = sp.executeQuery();
            if(r.first())
                cant = r.getInt(1);
            else
                cant=0;
        log.trace("Search done. Cantidad: " + cant);

            return cant;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cant;
    }

    public UbicacionesDB searchUbicacionesByID(String id) {
        log.trace("Searching ubicaciones by id...");

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement sp = conn.prepareStatement(
            "SELECT * FROM Ubicaciones "
            + "WHERE ubi_id LIKE ? LIMIT 1");) {

            sp.setString(1, id);
            ResultSet r = sp.executeQuery();
            if(r.first()){
                this.codUbi = r.getString(1);
                this.obsUbi = r.getString(2);
                log.trace("Search done.");
                return this;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isDuplicate(String id){
        log.trace("Is ubicacion duplicate...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall(
            "{CALL check_ubi_duplicates(?)}");){

            sp.setString(1, codUbi);

            ResultSet r = sp.executeQuery();
            int q = -1;
            if(r.first())
                q = r.getInt(1);
            log.trace((q>1)?"Is duplicate." : "Is not duplicate");
            if(q>1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public int find_EnDepo(String codUbi){
//
//        try ( Connection conn = DataSource.getConnection();
//            CallableStatement sp = conn.prepareCall("{CALL find_EnDepo(?)}");){
//
//            sp.setString(1, codUbi);
//            ResultSet r = sp.executeQuery();
//            if(r.first())
//                return  r.getInt(1);
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return -1;
//    }


    public String getCodUbi() {        return codUbi;    }
    public void setCodUbi(String codUbi) {        this.codUbi = codUbi;    }
    public String getObsUbi() {        return obsUbi;    }
    public void setObsUbi(String obsUbi) {        this.obsUbi = obsUbi;    }
    public String getNewCod() {        return newCod;    }
    public void setNewCod(String newCod) {        this.newCod = newCod;    }
    public String getNewObs() {        return newObs;   }
    public void setNewObs(String newObs) {        this.newObs = newObs;    }

}

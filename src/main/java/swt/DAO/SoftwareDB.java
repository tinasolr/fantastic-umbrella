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
public class SoftwareDB {

    private static Logger log = Logger.getLogger(Logger.class);
    private static final String TABLE = "software";
    private int codigo;
    private String nombre;
    private String version;
    private String nuevoNom;
    private String nuevoVers;
    private String soNom;

    public SoftwareDB() { }

    public SoftwareDB(int codigo, String nombre, String version) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.version = version;
    }

    public List<Integer> softwareDeMedios(String med_id){

        List<Integer> ids = new ArrayList<>();
        String SQLSWMED = "Select sw_id from Medios_Software AS s WHERE s.medio_id LIKE ?";
        try(Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQLSWMED);) {

            p.setString(1, med_id);
            ResultSet res = p.executeQuery();

            while(res.next()){
                ids.add(res.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }

    /**
     * Read all software from database.
     * @return List of SoftwareDB.
     */
    public List<SoftwareDB> read() {
        log.trace("reading software");
        List<SoftwareDB> objetos = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE;

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet rs = p.executeQuery();) {

            log.trace("connected");

            while (rs.next()) {
                objetos.add(readResultSet(rs));
            }
            log.trace("finished reading software");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objetos;
    }

    /**
     * Parse ResultSet into SoftwareDB objects.
     * @param res
     * @return SoftwareDB
     */
    public SoftwareDB readResultSet(ResultSet res) {
        SoftwareDB soft = null;

        try {

            this.codigo = res.getInt("sw_id");
            this.nombre = res.getString("sw_nom");
            this.version = res.getString("sw_vers");

            soft = new SoftwareDB(codigo, nombre, version);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return soft;
    }

    /**
     * Create software.
     * @param nombre nombre del software
     * @param version version del software ej. 1.2.3
     */
    public void write(String nombre, String version) {
        try(Connection conn = DataSource.getConnection();) {

            CallableStatement sp = conn.prepareCall("{call alta_software(?,?)}");

            Statement instr = conn.createStatement();

            sp.setString(1, nombre);
            sp.setString(2, version);

            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Modify software.
     * @param nombre nombre actual
     * @param nuevoNom nombre nuevo
     * @param version version actual
     * @param nuevoVers version nueva
     */
    public void update(String nombre, String nuevoNom, String version, String nuevoVers) {
         try (Connection conn = DataSource.getConnection()) {
            CallableStatement sp = conn.prepareCall("{call mod_software(?,?,?,?)}");
            sp.setString(1, nombre);
            sp.setString(2, version);
            sp.setString(3, nuevoNom);
            sp.setString(4, nuevoVers);

            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Eliminar sofware según el código ingresado.
     * @param codigo codigo del software
     */
    public void delete(int codigo) {
        try (Connection conn = DataSource.getConnection()) {
            CallableStatement sp = conn.prepareCall("{call eliminar_software(?)}");
            sp.setInt(1, codigo);

            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Search for software and get software ID
     * @param nombre nombre del software
     * @param version version del software
     * @return software id
     */
    public String search(String nombre, String version) {
        String result = null;
        ResultSet res = null;
        try ( Connection conn = DataSource.getConnection() ) {

            CallableStatement storedProc = conn.prepareCall("{call get_idsoftware(?,?)}");
            storedProc.setString(1, nombre);
            storedProc.setString(2, version);
            res = storedProc.executeQuery();
            int r = 0;
            if(res.first()){
                r = res.getInt(1);
                result = String.valueOf(r);
            }
            else{
              res.close();
              return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;

    }

    /**
     * Delete relation software-sistemaOperativo.
     * @param soNom nombre del sistema operativo
     * @param swCod codigo del software
     */
    public void deleteSOSw(String soNom, int swCod){

        String SQL = "SELECT so_id FROM sistoperativos WHERE `so_nom` = ?";
        String SQL2 = "DELETE FROM sistoperativos_software WHERE `so_id` = ? AND `sw_id` = ?";

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL); ) {

            p.setString(1, soNom);
            ResultSet res = p.executeQuery();

            int cod = -1;

            if(res.first())
                cod = res.getInt(1);

            PreparedStatement p2 = conn.prepareStatement(SQL2);
            p2.setInt(1, cod);
            p2.setInt(2, swCod);
            p2.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create software-sistemaOperativo relation.
     * @param soNom nombre del sistema operativo
     * @param swCod codigo del software
     */
    public void insertSOSw(String soNom, int swCod){

        ResultSet res = null;
        String SQL = "SELECT `so_id` FROM `SistOperativos` WHERE `so_nom` = ?";
        String SQL2 = "INSERT INTO SistOperativos_Software(so_id, sw_id) VALUES (?, ?)";
        try ( Connection conn = DataSource.getConnection(); ) {

            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, soNom);

            res = ps.executeQuery();

            int cod = -1;
            if(res.first())
                cod = res.getInt(1);
            if(cod>0){
                PreparedStatement ps1 = conn.prepareStatement(SQL2);
                ps1.setInt(1, cod);
                ps1.setInt(2, swCod);
                ps1.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCodigo() {return codigo;}
    public void setCodigo(int codigo) {    this.codigo = codigo;}
    public String getNombre() {    return nombre;}
    public void setNombre(String nombre) {    this.nombre = nombre;}
    public String getVersion() {    return version;}
    public void setVersion(String version) {   this.version = version;}
    public String getNuevoNom() {        return nuevoNom;    }
    public void setNuevoNom(String nuevoNom) {        this.nuevoNom = nuevoNom;    }
    public String getNuevoVers() {        return nuevoVers;    }
    public void setNuevoVers(String nuevoVers) {        this.nuevoVers = nuevoVers;   }
    public String getSoNom() {        return soNom;    }
    public void setSoNom(String soNom) {        this.soNom = soNom;   }
}

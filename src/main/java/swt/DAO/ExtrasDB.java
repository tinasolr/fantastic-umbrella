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
public class ExtrasDB {

    private static Logger log = Logger.getLogger(Logger.class);
    private static final String TABLE = "extras";
    private String nombre;
    private String version;
    private String descrip;
    private int partes;
    private int swid;
    private int id;

    public ExtrasDB() {}

    public ExtrasDB(String nombre, String version, String descrip, int partes) {
        this.nombre = nombre;
        this.version = version;
        this.descrip = descrip;
        this.partes = partes;
    }


    /**
     * Read all software extras from database.
     * @return List of ExtrasDB.
     */
    public List<ExtrasDB> read() {
        log.trace("reading extras");
        List<ExtrasDB> objetos = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE;

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet rs = p.executeQuery();) {

            log.trace("connected");

            while (rs.next()) {
                objetos.add(readResultSet(rs));
            }
            log.trace("finished reading extras");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objetos;
    }

    /**
     * Parse ResultSet into SoftwareDB objects.
     * @param res
     * @return ExtrasDB
     */
    public ExtrasDB readResultSet(ResultSet res) {
        ExtrasDB extra = null;

        try {

            this.nombre = res.getString("extra_nom");
            this.version = res.getString("extra_vers");
            this.descrip = res.getString("extra_descr");
            this.partes = res.getInt("extra_partes");

            extra = new ExtrasDB(nombre, version, descrip, partes);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return extra;
    }

    public void write(int swid, String nombre, String version, String descrip, int partes) {
        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call alta_extra(?,?,?,?,?)}");) {


            sp.setString(1, nombre);
            sp.setString(2, version);
            sp.setString(3, descrip);
            sp.setInt(4, partes);
            sp.setInt(5, swid);

            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String search(int swid, String nombre) {
        String result = null;
        ResultSet res = null;
        try ( Connection conn = DataSource.getConnection();
            CallableStatement storedProc = conn.prepareCall("{call get_idextra(?,?)}");) {

            storedProc.setInt(1, swid);
            storedProc.setString(2, nombre);

            res = storedProc.executeQuery();
            int r = -1;
            if(res.first())
                r = res.getInt(1);
            result = String.valueOf(r);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void update(int swid, int extraid, String nombre, String version, String descrip, int partes) {
        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call mod_extra(?,?,?,?,?,?)}");) {

            sp.setString(1, nombre);
            sp.setString(2, version);
            sp.setString(3, descrip);
            sp.setInt(4, partes);
            sp.setInt(5, swid);
            sp.setInt(6, extraid);

            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<ExtrasDB> extrasDeSoftware(int sw_id) {
        ResultSet res = null;
        List<ExtrasDB> exDB = new ArrayList<>();
        String SQL = "SELECT * FROM Extras WHERE sw_id = ?";

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);) {

            p.setInt(1, sw_id);

            res = p.executeQuery();

            while(res.next()){
                exDB.add(readResultSet(res));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return exDB;
    }

    public void delete(int extraID, int swID) {
        try ( Connection conn = DataSource.getConnection();
            CallableStatement storedProc = conn.prepareCall("{call eliminar_extra(?,?)}");) {

            storedProc.setInt(1, extraID);
            storedProc.setInt(2, swID);
            storedProc.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAllExtras(int swID) {
        try ( Connection conn = DataSource.getConnection();
            CallableStatement storedProc = conn.prepareCall("{call eliminar_todos_extra(?)}");) {

            storedProc.setInt(1, swID);
            storedProc.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getNombre() {  return nombre;  }
    public void setNombre(String nombre) {  this.nombre = nombre;  }

    public String getVersion() { return version;   }
    public void setVersion(String version) { this.version = version;  }

    public String getDescrip() {  return descrip;  }
    public void setDescrip(String descrip) {  this.descrip = descrip;  }

    public int getPartes() {  return partes;  }
    public void setPartes(int partes) {  this.partes = partes;   }

    public int getSwid() {return swid;}
    public void setSwid(int swid) {this.swid = swid;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    @Override
    public String toString() {
        return "ExtrasDB{" + "nombre=" + nombre + ", version=" + version + ", descrip=" + descrip + ", partes=" + partes + '}';
    }

}

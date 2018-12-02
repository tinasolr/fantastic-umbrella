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
public class SistOpDB {

    private static Logger log = Logger.getLogger(Logger.class);
    private static final String TABLE = "SistOperativos";
    private String nombre;
    private String newNombre;

    public SistOpDB(String nombre) {
        this.nombre = nombre;
    }

    public SistOpDB(){}

    public List<SistOpDB> sistopDeSoftware(int sw_id){

        List<Integer> ids = new ArrayList<>();
        List<SistOpDB> objDB = new ArrayList<>();
        String SQL = "Select so_id from `SistOperativos_Software` AS s WHERE s.sw_id = ?";
        String SQL2 = "Select * from `SistOperativos` AS s WHERE s.so_id = ?";

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            PreparedStatement p2 = conn.prepareStatement(SQL2);){

            p.setInt(1, sw_id);
            ResultSet res = p.executeQuery();

            while(res.next()){
                ids.add(res.getInt(1));
            }

            ResultSet res2 = null;
            for(Integer x : ids){
                p2.setInt(1, x);
                res2 = p2.executeQuery();
                if(res2.first())
                    objDB.add(readResultSet(res2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objDB;
    }

    /**
     * Read all sistemas operativos from database.
     * @return List of SistOpDB.
     */
    public List<SistOpDB> read() {
        log.trace("Reading sistemas operativos...");

        List<SistOpDB> objetos = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE;

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet res = p.executeQuery();) {

            while (res.next()) {
                SistOpDB s = readResultSet(res);
                if(!s.getNombre().isEmpty() && !s.getNombre().equalsIgnoreCase("INVALIDO"))
                    objetos.add(s);
            }
            log.trace("Finished reading sistemas operativos.");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objetos;
    }

    public SistOpDB readResultSet(ResultSet res) {
        SistOpDB obj = null;

        try {

            this.nombre = res.getString("so_nom");
            obj = new SistOpDB(nombre);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    public void write(String nombre) {
        log.trace("Creating sitema operativo...");
         try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call alta_so(?)}");){

            sp.setString(1, nombre);
            sp.executeUpdate();
        log.trace("Sistema operativo created.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String nombre, String newNombre) {
        log.trace("Updating sistema operativo...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call mod_so(?, ?)}");){

            sp.setString(1, newNombre);
            sp.setString(2, nombre);
            sp.executeUpdate();
        log.trace("Sistema operativo updated.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String nombre) {
        log.trace("Deleting sistema operativo...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call elim_so(?)}");){

            sp.setString(1, nombre);
            sp.executeUpdate();
        log.trace("Sistema operativo deleted.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int search(String nombre) {
        log.trace("Searching for sistema operativo...");
        int id = -1;

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call get_idSo(?)}");){

            sp.setString(1, nombre);
            ResultSet r = sp.executeQuery();

            if(r.first())
                id = r.getInt(1);
        log.trace("Done searching.");
            return id;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }
    public String getNewNombre() {        return newNombre;    }
    public void setNewNombre(String newNombre) {        this.newNombre = newNombre;   }

}

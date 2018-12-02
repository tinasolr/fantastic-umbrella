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
public class FormatoDB {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(org.apache.log4j.Logger.class);
    private static final String TABLE = "formatos";

    private String formato;
    private String newFormat;

    public FormatoDB(String nombre) {
        this.formato = nombre;
    }

    public FormatoDB(){}

    /**
     * Read all formatos from database.
     * @return List of Strings.
     */
    public List<String> read() {
        log.trace("Reading formatos...");
        List<String> objetos = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE;

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet rs = p.executeQuery();) {

            while (rs.next()) {
                String f = readResultSet(rs);
                if(!f.isEmpty() && !f.equalsIgnoreCase("INVALIDO"))
                    objetos.add(f);
            }
            log.trace("Finished reading formatos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return objetos;
    }

    public String readResultSet(ResultSet res) {
        String obj = null;

        try {

            this.formato = res.getString("form_nom");
            obj = formato;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    public void write(String formato) {
        log.trace("Creating formato...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call alta_formato(?)}");) {

            sp.setString(1, formato);
            sp.executeUpdate();
        log.trace("Formato created.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String formato, String newFormat) {

        log.trace("Updating formato...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call mod_formato(?, ?)}");){

            sp.setString(1, newFormat);
            sp.setString(2, formato);
            sp.executeUpdate();

        log.trace("Formato modified.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String formato) {

        log.trace("Deleting formato...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call elim_formato(?)}");){

            sp.setString(1, formato);
            sp.executeUpdate();

        log.trace("Formato deleted.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int search(String formato) {

        log.trace("Searching formatos for ID...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call find_formatoid(?)}");){

            sp.setString(1, formato);
            ResultSet r = sp.executeQuery();

            int id = 0;
            if(r.first())
                id = r.getInt(1);

            log.trace("Formato ID: " + id);

            return id;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public String search(int id){

        log.trace("Searching formats for name...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("{call find_formatonom(?)}");){

            sp.setInt(1, id);
            ResultSet r = sp.executeQuery();
            String name = "";
            if(r.first())
                name =  r.getString(1);
            log.trace("Formato name: " + name);
            return name;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


//    public String fetchFormatoByID(int id) {
//        try {
//
//            if(conn == null || conn.isClosed())
//                connect();
//
//            PreparedStatement sp = conn.prepareStatement("SELECT form_nom FROM Formatos WHERE form_id=?");
//
//            sp.setInt(1, id);
//            ResultSet r = sp.executeQuery();
//            r.first();
//            String nombre = r.getString(1);
//
//            conn.close();
//            return nombre;
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally{
//            try {
//                conn.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(FormatoDB.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return null;
//    }

    public String getFormato() {        return formato;    }
    public void setFormato(String formato) {        this.formato = formato;    }
    public String getNewFormat() {        return newFormat;    }
    public void setNewFormat(String newFormat) {        this.newFormat = newFormat;    }
}

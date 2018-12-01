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
public class UsersDB {

    private final Logger log;
    private static final String TABLE = "users";
    private static String username;
    private String passw;
    private String nombre;
    private int access;

    public UsersDB() {
        this.log = Logger.getLogger(Logger.class);
    }

    //0 - ONLY VIEW
    //1 - ADD, EDIT, DELETE MEDIOS, SW
    //2 - ADD, EDIT, DELETE NORMALIZED ELEMENTS (Formatos, Ubicaciones, Sistemas Op.)
    //3 - ADD, EDIT, DELETE USERS

    public void getUserInfo(String username){
        String SQL = "SELECT name, access FROM users WHERE username=? LIMIT 1";
        log.trace("Getting user info...");
        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);) {

            p.setString(1, username);
            ResultSet res = p.executeQuery(SQL);

            while(res.next()){
                this.nombre = res.getString(1);
                this.access = res.getInt(2);
            }

        } catch (SQLException ex) {ex.printStackTrace();}
        log.trace("Name: " + nombre + " - Acceso: " + access);
    }

    public int validarIngreso(String txtUsuario, String txtPass){

        username = txtUsuario;
        String clave = String.valueOf(txtPass);

        int access=0;
        log.trace("Validating user info...");

        try ( Connection conn = DataSource.getConnection();
             CallableStatement sp = conn.prepareCall("CALL validar_ingreso(?, ?, ?)");
        ){

            sp.setString(1, txtUsuario);
            sp.setString(2, txtPass);
            sp.registerOutParameter(3, java.sql.Types.INTEGER);

            sp.execute();

            access = sp.getInt(3);

        } catch (SQLException ex) {ex.printStackTrace(); }

        log.trace((access>-1)? "Validation OK." : "Forbidden.");
        return access;

    }

    public boolean isUser(String username){
        int returnValue = 0;
        log.trace("Checking if user exists...");
        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("CALL existe_user(?, ?)"); ) {

            sp.setString(1, username);
            sp.registerOutParameter(2, java.sql.Types.INTEGER);

            sp.execute();

            returnValue = sp.getInt(2);

        } catch (SQLException ex) {ex.printStackTrace();}

        log.trace(returnValue==1? "User exists." : "User does not exist.");
        if(returnValue==1) return true;
        else return false;
    }

    public void modifyPassword(String user, String pass){

        log.trace("Modifying password for  " + user + "...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("CALL mod_userPass(?, ?)"); ) {

            sp.setString(1, user);
            sp.setString(2, pass);

            sp.executeUpdate();

        } catch (SQLException ex) {ex.printStackTrace();}

        log.trace("Modified password.");
    }

    public void modifyName(String user, String name){

        log.trace("Modifying name for  " + user + "...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("CALL mod_userNombre(?, ?)");){

            sp.setString(1, user);
            sp.setString(2, name);

            sp.executeUpdate();

        } catch (SQLException ex) {ex.printStackTrace();}

        log.trace("Modified name.");

    }

    public void modifyAccess(String user, int access){

        log.trace("Modifying access for  " + user + "...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("CALL mod_userAccess(?, ?)");){

            sp.setString(1, user);
            sp.setInt(2, access);

            sp.executeUpdate();

        } catch (SQLException ex) {ex.printStackTrace();}

        log.trace("Modified access.");

    }

    public int getUserAccess(){
        int userAccess = -1;
        log.trace("Getting user access...");
        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("CALL get_userAccess(?, ?)"); ) {

            sp.setString(1, username);
            sp.registerOutParameter(2, java.sql.Types.INTEGER);

            sp.execute();

            userAccess = sp.getInt(2);

        } catch (SQLException ex) { ex.printStackTrace();}
        log.trace("Access: " + userAccess);
        return userAccess;
    }

    public int getUserAccess(String username){
        int userAccess = -1;
        log.trace("Getting user access...");
        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("CALL get_userAccess(?, ?)"); ) {

            sp.setString(1, username);
            sp.registerOutParameter(2, java.sql.Types.INTEGER);

            sp.execute();

            userAccess = sp.getInt(2);

        } catch (SQLException ex) { ex.printStackTrace();}
        log.trace("Access: " + userAccess);
        return userAccess;
    }

    public List<String> read() {

        log.trace("Reading users...");

        List<String> objetos = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE;

        try ( Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(SQL);
            ResultSet rs = p.executeQuery();) {

            while (rs.next()) {
                objetos.add(rs.getString("username"));
            }

            log.trace("Finished reading users.");

        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return objetos;
    }

    public boolean write(String nombre, String username, String passw, int access) {

        log.trace("Creating user...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("CALL alta_user(?, ?, ?, ?, ?)");){

            sp.setString(1, nombre);
            sp.setString(2, username);
            sp.setString(3, passw);
            sp.setInt(4, access);
            sp.registerOutParameter(5, Types.BOOLEAN);
            sp.execute();
            boolean created = sp.getBoolean(5);
            log.trace(created? "Created user." : "Did not create user.");
            return created;

        } catch (SQLException ex) {ex.printStackTrace();}

        return false;
    }

    public void delete(String username) {

        log.trace("Creating user...");

        try ( Connection conn = DataSource.getConnection();
            CallableStatement sp = conn.prepareCall("CALL elim_user(?)");){

            sp.setString(1, username);

            sp.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        log.trace("Deleted user.");
    }

    public int getAccess() {        return access;    }
    public void setAccess(int access) {        this.access = access;    }
    public String getUsername() {        return username;    }
    public void setUsername(String username) {        this.username = username;    }
    public String getPassw() {        return passw;    }
    public void setPassw(String passw) {        this.passw = passw;    }
    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }
}
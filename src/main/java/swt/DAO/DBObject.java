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
import java.util.logging.*;

/**
 *
 * @author tinar
 */
public abstract class DBObject {
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://db4free.net:3306/dbpruebaswt?autoReconnect=true&useSSL=false";
    private final String URLBDLOCAL = "jdbc:mysql://localhost/dbpruebaswt?autoReconnect=true&useSSL=false";
    private boolean isRemote = false;
    private final String USER = "nicotina";
    private final String PASSW = "nicotina";
    protected Connection conn;
    protected Statement instr;

    public void connect() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if(isRemote){

                conn = DriverManager.getConnection(URL, USER, PASSW);
                instr = conn.createStatement();

            }else{

                conn = DriverManager.getConnection(URLBDLOCAL, USER, PASSW);
                instr = conn.createStatement();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <E> List<E> read(String table) {
        List<E> objetos = new ArrayList<>();
        ResultSet rs = null;
        try {

            if(conn == null || conn.isClosed())
                connect();

            rs = instr.executeQuery("SELECT * FROM " + table);

            while (rs.next()) {
                objetos.add(readResultSet(rs));
            }
            conn.close();

        } catch (SQLException e) {
            System.out.println("BDObject read() :: " + e.getMessage());
        } finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return objetos;
    }

    public abstract <E> E readResultSet(ResultSet res);

    public void write() {
        try {

            if(conn == null || conn.isClosed())
                connect();

            executeWrite();

            conn.close();

        } catch (SQLException e) {
            System.out.println("BDObject write() :: " + e.getMessage());
        } finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public abstract void executeWrite();

    public void update() {
        try {

            if(conn == null || conn.isClosed())
                connect();

            executeUpdate();

            conn.close();

        } catch (SQLException e) {
            System.out.println("BDObject update() :: " + e.getMessage());
        } finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public abstract void executeUpdate();

    public void delete() {
        try {

            if(conn == null || conn.isClosed())
                connect();

            executeDelete();

            conn.close();

        } catch (SQLException e) {
            System.out.println("BDObject delete() :: " + e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public abstract void executeDelete();

    public String searchTable() {
        String id = null;
        try {

            if(conn == null || conn.isClosed())
                connect();

            id = executeSearch();

            conn.close();

        } catch (SQLException e) {
            System.out.println("BDObject search() :: " + e.getMessage());
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return id;
    }

    public abstract String executeSearch();

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Statement getInstr() {
        return instr;
    }

    final public static void printResultSet(ResultSet rs) throws SQLException{
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(" | ");
                System.out.print(rs.getString(i));
            }
            System.out.println("");
        }
    }

}
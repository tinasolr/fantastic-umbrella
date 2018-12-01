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
package swt.controller;

import java.sql.*;
import swt.DAO.*;
/**
 *
 * @author tinar
 */
public class Images {
    private static String path = "";
    private static final String GETPATH = "Select path from image_folder";
    private static String SETPATH = "UPDATE `image_folder` SET `path` = ? WHERE `key` = 1";

    public Images(){}

    public static void getPathFromDB(){
        try(Connection conn = DataSource.getConnection();
            PreparedStatement p = conn.prepareStatement(GETPATH);
            ResultSet res = p.executeQuery();){
            if(res.first())
                path = res.getString(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void setPath(String pa){
        if(pa!=null && !pa.isEmpty()){
            try(Connection conn = DataSource.getConnection();
                PreparedStatement p = conn.prepareStatement(SETPATH);){
                p.setString(1, pa);
                p.executeUpdate();
                path = pa;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getPath(){
        return path;
    }
}

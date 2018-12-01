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

import swt.DAO.*;
import swt.model.*;

/**
 *
 * @author tinar
 */
public class ExtrasCtrl {

    private ExtrasDB extrasDB = new ExtrasDB();

    public ExtrasCtrl() {
    }

    public void modificarExtra(String nombre, String descrip, String version, int partes, Software soft){
        String id = extrasDB.search(soft.getCodigo(), nombre);
        int extraid = Integer.parseInt(id);
        extrasDB.update(soft.getCodigo(), extraid, nombre, version, descrip, partes);
    }

    public void eliminarExtra(String nombre, int soft){
        String id = extrasDB.search(soft, nombre);
        int extraid = Integer.parseInt(id);
        extrasDB.delete(extraid, soft);
    }

    public void eliminarExtras(Software soft){
        extrasDB.deleteAllExtras(soft.getCodigo());
    }

    public void altaExtra(String nombre, String descrip, String version, int partes, int sw){
        extrasDB.write(sw, nombre, version, descrip, partes);
    }

}

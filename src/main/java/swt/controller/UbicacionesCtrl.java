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

import java.util.*;
import swt.DAO.*;
import swt.model.*;

/**
 *
 * @author tinar
 */
public class UbicacionesCtrl {

    private static List<Ubicaciones> ubis = new ArrayList<>();
    private Ubicaciones ubicacion;
    private UbicacionesDB dbubi = new UbicacionesDB();

    public UbicacionesCtrl() {}

    public void cargarUbicaciones(){
        if(!ubis.isEmpty())
            ubis.clear();
        List<UbicacionesDB> dbubis = dbubi.read();
        for(UbicacionesDB b : dbubis){
            ubis.add(new Ubicaciones(b.getCodUbi(), b.getObsUbi()));
        }
    }

//    public Ubicaciones fetchUbicacion(String codigo){
//
//        UbicacionesDB u = dbubi.searchUbicacionesByID(codigo);
//        Ubicaciones found = findUbicacion(u);
//        if(found == null){
//            found = new Ubicaciones(u.getCodUbi(), u.getObsUbi());
//            ubis.add(found);
//        }
//        return found;
//    }

//    public Ubicaciones findUbicacion(UbicacionesDB codigo){
//        for(Ubicaciones u : ubis)
//            if(u.getId().equalsIgnoreCase(codigo.getCodUbi()))
//                return u;
//        return null;
//    }

    public Ubicaciones findUbicacion(String codigo){
        for(Ubicaciones u : ubis)
            if(u.getId().equalsIgnoreCase(codigo))
                return u;
        return null;
    }

    public List<Ubicaciones> getUbis() {        return ubis;    }
    public void setUbis(List<Ubicaciones> ubis) {        this.ubis = ubis;   }

    public Ubicaciones getUbicacion() {        return ubicacion;    }
    public void setUbicacion(Ubicaciones ubicacion) {        this.ubicacion = ubicacion;    }

}

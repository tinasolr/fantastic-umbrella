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
public class CopiasCtrl {


    private List<Copias> copias;
    private CopiasDB copdb;
    private final FormatoDB formdb;
    private final UbicacionesDB ubidb;

    public CopiasCtrl(){
        this.ubidb = new UbicacionesDB();
        this.formdb = new FormatoDB();
        this.copdb = new CopiasDB();
        this.copias = new ArrayList<>();
    }

    public void cargarCopias(String medid){
        if(!copias.isEmpty())
            copias.clear();

        List<CopiasDB> dbcopia = copdb.read();

        Ubicaciones ubi;
        String nombreform,ubiid;

        for(CopiasDB c : dbcopia){
            if (c.getMedioid().equalsIgnoreCase(medid))
            {
                ubiid = copdb.findubiid(c.getId());
                ubi = new Ubicaciones(ubiid, ubidb.search(ubiid, Boolean.FALSE));

                nombreform = formdb.search(c.getFormid());

                copias.add(new Copias(c.getId(),nombreform,c.getObs(),ubi, c.isEnDepo()));
            }
        }
    }

     public void CrearCopia(String medioid, int formatoid, String obs){
        copdb.write(medioid, formatoid, obs);
    }

     public void asociarUbicacionACopia(int idCopia, String idUbi, boolean EnDepo){
        copdb.asociarUbicacionACopia(idCopia, idUbi, EnDepo);
     }

    public void desasociarUbicacionACopia(int idCopia){
        copdb.desasociarUbicacionACopia(idCopia);
     }

    public void modificarUbicacionACopia(int idCopia, String idUbi, boolean EnDepo){
        copdb.actualizarUbicacionACopia(idCopia, idUbi, EnDepo);
     }

    public void ModificarCopia(int copiaid, String medioid, int formid, String obs){
        copdb.update(copiaid, medioid, formid, obs);
    }

    public void EliminarCopia(int id){
        copdb.delete(id);
    }

    public int buscarUltimoID() {
        return copdb.buscarUltimoID();
    }

    public List<Copias> getCopias() { return copias;}
    public void setCopias(List<Copias> copias) {this.copias = copias;}
    public CopiasDB getCopdb() {return copdb;}
    public void setCopdb(CopiasDB copdb) {this.copdb = copdb;}




}

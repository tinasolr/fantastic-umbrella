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
package swt.model;

/**
 * @author tinar
 */
public class Extras {
    
    private String nombre;
    private String version;
    private String descrip;
    private int partes;

    public Extras(){}

    public Extras(String nombre, String version, String descrip, int partes){
        this.nombre = nombre;
        this.version = version;
        this.descrip = descrip;
        this.partes = partes;
    }

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getVersion() {return version;}
    public void setVersion(String version) {this.version = version;}

    public String getDescrip() {return descrip;}
    public void setDescrip(String descrip) {this.descrip = descrip;}

    public int getPartes() {return partes;}
    public void setPartes(int partes) {this.partes = partes;}

}

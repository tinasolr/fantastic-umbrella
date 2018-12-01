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

import java.util.*;

/**
 *
 * @author tinar
 */
public class Software {

    private int codigo;
    private String nombre;
    private List<String> sistOp = new ArrayList<>();
    private String version;
    private List<Extras> extras = new ArrayList<>();
    private List<Medios> medios = new ArrayList<>() ;

    public Software(){}

    public Software(int codigo, String nombre, List<String> sistOp, String version, List<Medios> medios) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.sistOp = sistOp;
        this.version = version;
        this.medios = medios;
    }

    public Software(int codigo, String nombre, List<String> sistOp, String version) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.sistOp = sistOp;
        this.version = version;
        this.medios = new ArrayList<>();
    }

    public int getCodigo() {return codigo;}
    public void setCodigo(int codigo) {this.codigo = codigo;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public List<String> getSistOp() {return sistOp;}
    public void setSistOp(List<String> sistOp) {this.sistOp = sistOp;}

    public String getVersion() {return version;}
    public void setVersion(String version) {this.version = version;}

    public List<Extras> getExtras() {return extras;}
    public void setExtras(String nombre, String version, String descrip, int partes) {
        this.extras.add(new Extras(nombre, version, descrip, partes));
    }
    public void setExtras(int pos, String nombre, String version, String descrip, int partes) {
        this.extras.set(pos, new Extras(nombre, version, descrip, partes));
    }

    public List<Medios> getMedios() {return medios;}
    public void setMedios(List<Medios> medios) {this.medios = medios;}

    @Override
    public String toString() {
        return "Software{" + "codigo=" + codigo + ", nombre=" + nombre + ", sistOp=" + sistOp.toString() + ", version=" + version + ", extras=" + extras.toString() + ", medios=" + medios.toString() + '}';
    }

}

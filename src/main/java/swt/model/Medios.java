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
public class Medios {

    private String codigo;
    private String nombre;
    private String formato;
    private boolean caja;
    private boolean manual;
    private int origen; //1 = Original, 2 = Mixto, 3 = Otros
    private Ubicaciones ubiDepo;
    private boolean enDepo;
    private String imagen;
    private String observ;
    private int partes;
    private List<Copias> copias = new ArrayList<>();

    public Medios(){}

    public Medios(String codigo, String nombre, String formato, boolean caja, boolean manual, int origen, Ubicaciones ubiDepo, boolean enDepo, String imagen, String observ, int partes) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.formato = formato;
        this.caja = caja;
        this.manual = manual;
        this.origen = origen;
        this.ubiDepo = ubiDepo;
        this.enDepo = enDepo;
        this.imagen = imagen;
        this.observ = observ;
        this.partes = partes;
    }

    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getFormato() {return formato;}
    public void setFormato(String formato) {this.formato = formato;}

    public int getOrigen() {return origen;}
    public void setOrigen(int origen) {this.origen = origen;}

    public Ubicaciones getUbiDepo() {return ubiDepo;}
    public void setUbiDepo(Ubicaciones ubiDepo) {this.ubiDepo = ubiDepo;}

    public boolean isEnDepo() {return enDepo;}
    public void setEnDepo(boolean enDepo) {this.enDepo = enDepo;}

    public String getImagen() {return imagen;}
    public void setImagen(String imagen) {this.imagen = imagen;}

    public String getObserv() {return observ;}
    public void setObserv(String observ) {this.observ = observ;}

    public int getPartes() {return partes;}
    public void setPartes(int partes) {this.partes = partes;}

    public boolean isCaja() {        return caja;    }
    public void setCaja(boolean caja) {        this.caja = caja;    }

    public boolean isManual() {        return manual;   }
    public void setManual(boolean manual) {        this.manual = manual;    }

    public List<Copias> getCopias() {        return copias;    }
    public void setCopias(int id, String formato, Ubicaciones ubiDepo, String observ, boolean enDepo) {
        copias.add(new Copias(id,formato, observ, ubiDepo, enDepo));
    }
    public void setCopias(int pos,int id, String formato, Ubicaciones ubiDepo, String observ, boolean enDepo) {
        copias.set(pos, new Copias(id,formato, observ, ubiDepo, enDepo));
    }

    @Override
    public String toString() {
        return "Medios{" + "codigo=" + codigo + ", nombre=" + nombre + ", formato=" + formato + ", caja=" + caja + ", manual=" + manual + ", origen=" + origen + ", ubiDepo=" + ubiDepo.getId() + ", enDepo=" + enDepo + ", imagen=" + imagen + ", observ=" + observ + ", partes=" + partes + ", copias=" + copias.size() + '}';
    }

}

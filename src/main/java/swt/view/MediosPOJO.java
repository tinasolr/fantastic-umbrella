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
package swt.view;

/**
 *
 * @author tinar
 */
public class MediosPOJO {
    //m.medio_id, medio_nom, sw_nom, sw_vers, medio_partes, medio_manual, medio_caja, medio_imagen, medio_obs, form_nom, origen_id, u.ubi_id, ubi_obs
    private String codigo;
    private String nombre;
    private String swNomb;
    private String swVers;
    private String partes;
    private boolean manual;
    private boolean caja;
    private String imagen;
    private String observ;
    private String formato;
    private String origen;
    private String ubicacion;
    private String ubiObserv;

    public MediosPOJO() {    }

    public MediosPOJO(String codigo, String nombre, String swNomb, String swVers, String partes, boolean manual, boolean caja, String imagen, String observ, String formato, String origen, String ubicacion, String ubiObserv) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.swNomb = swNomb;
        this.swVers = swVers;
        this.partes = partes;
        this.manual = manual;
        this.caja = caja;
        this.imagen = imagen;
        this.observ = observ;
        this.formato = formato;
        this.origen = origen;
        this.ubicacion = ubicacion;
        this.ubiObserv = ubiObserv;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSwNomb() {
        return swNomb;
    }

    public void setSwNomb(String swNomb) {
        this.swNomb = swNomb;
    }

    public String getSwVers() {
        return swVers;
    }

    public void setSwVers(String swVers) {
        this.swVers = swVers;
    }

    public String getPartes() {
        return partes;
    }

    public void setPartes(String partes) {
        this.partes = partes;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean isCaja() {
        return caja;
    }

    public void setCaja(boolean caja) {
        this.caja = caja;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getObserv() {
        return observ;
    }

    public void setObserv(String observ) {
        this.observ = observ;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUbiObserv() {
        return ubiObserv;
    }

    public void setUbiObserv(String ubiObserv) {
        this.ubiObserv = ubiObserv;
    }

    
    
}

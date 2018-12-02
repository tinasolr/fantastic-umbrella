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
public class CopiasPOJO {
//copia_id, m.medio_id, medio_nom, cp_obs, form_nom, u.ubi_id, ubi_obs, medio_manual, medio_caja, medio_imagen
    private String codigo;
    private String medCodigo;
    private String medNomb;
    private String observ;
    private String formato;
    private String ubicacion;
    private String ubiObserv;

    public CopiasPOJO() {
    }

    public CopiasPOJO(String codigo, String medCodigo, String medNomb, String observ, String formato, String ubicacion, String ubiObserv) {
        this.codigo = codigo;
        this.medCodigo = medCodigo;
        this.medNomb = medNomb;
        this.observ = observ;
        this.formato = formato;
        this.ubicacion = ubicacion;
        this.ubiObserv = ubiObserv;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMedCodigo() {
        return medCodigo;
    }

    public void setMedCodigo(String medCodigo) {
        this.medCodigo = medCodigo;
    }

    public String getMedNomb() {
        return medNomb;
    }

    public void setMedNomb(String medNomb) {
        this.medNomb = medNomb;
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

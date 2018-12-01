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
public class MediosTableFormat {

       private String codigo;
       private  String nombre;
       private String formato;
       private String original;
       private String ubiDepo;
       private String enDepo;
       private Integer copias;

    public MediosTableFormat(String codigo, String nombre, String formato, String original, String ubiDepo, String enDepo, Integer copias) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.formato = formato;
        this.original = original;
        this.ubiDepo = ubiDepo;
        this.enDepo = enDepo;
        this.copias = copias;
    }

    public MediosTableFormat() {
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String isOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getUbiDepo() {
        return ubiDepo;
    }

    public void setUbiDepo(String ubiDepo) {
        this.ubiDepo = ubiDepo;
    }

    public String isEnDepo() {
        return enDepo;
    }

    public void setEnDepo(String enDepo) {
        this.enDepo = enDepo;
    }

    public Integer getCopias() {
        return copias;
    }

    public void setCopias(Integer copias) {
        this.copias = copias;
    }


}

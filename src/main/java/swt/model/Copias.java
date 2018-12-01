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
 *
 * @author tinar
 */
public class Copias {

    private int id;
    private String formato;
    private String observ;
    private Ubicaciones ubiDepo;
    private boolean enDepo;


    public Copias(){}
    public Copias(int id,String formato, String observ, Ubicaciones ubiDepo, boolean enDepo){
        this.id = id;
        this.formato = formato;
        this.observ = observ;
        this.ubiDepo = ubiDepo;
        this.enDepo = enDepo;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getFormato() {return formato;}
    public void setFormato(String formato) {this.formato = formato;}

    public String getObserv() {return observ;}
    public void setObserv(String observ) {this.observ = observ;}

    public Ubicaciones getUbiDepo() {return ubiDepo;}
    public void setUbiDepo(Ubicaciones ubiDepo) {this.ubiDepo = ubiDepo;}

    public boolean isEnDepo() {
        return enDepo;
    }

    public void setEnDepo(boolean enDepo) {
        this.enDepo = enDepo;
    }
}

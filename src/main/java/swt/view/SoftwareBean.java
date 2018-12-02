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

import com.opencsv.bean.*;
import java.util.*;

/**
 *
 * @author tinar
 */
public class SoftwareBean {
    @CsvBindByPosition(position = 1)
    private String nombre;

    @CsvBindByPosition(position = 2)
    private String version;

    @CsvBindAndSplitByPosition(elementType = String.class, position = 3)
    private List<String> sistOperativos;

    @CsvBindByPosition(position = 4)
    private String nombreExtra;
    //Getters and Setters
    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }
    public String getVersion() {        return version;    }
    public void setVersion(String version) {        this.version = version;    }
    public List<String> getSistOperativos() {        return sistOperativos;    }
    public void setSistOperativos(List<String> sistOperativos) {        this.sistOperativos = sistOperativos;    }
}

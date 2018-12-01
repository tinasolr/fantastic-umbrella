/*
 * Copyright (C) 2018 Agustina y Nicolas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package swt.view;

import com.opencsv.bean.*;
import java.util.*;

/**
 *
 * @author tinar
 */
public class MediosBean {

     @CsvBindByName(column = "Codigo")
     private String codigo;

     @CsvBindByName(column = "Nombre")
     private String nombre;

     @CsvBindAndSplitByName(elementType = Integer.class, column = "Software")
     private List<Integer> software;

     @CsvBindByName(column = "Formato")
     private String formato;

     @CsvCustomBindByName(converter=ConvertOriginalToInt.class, column = "Es original")
     private int origen;

     @CsvCustomBindByName(converter = ConvertSpanishToBoolean.class, column = "Manual")
     private boolean manual;

     @CsvCustomBindByName(converter = ConvertSpanishToBoolean.class, column = "Caja")
     private boolean caja;

     @CsvBindByName(column = "Fecha")
     @CsvDate("MM/dd/yyyy HH:mm")
     private Date createdAt;

     @CsvBindByName(column = "Ubicacion")
     private String ubicacion;

     @CsvCustomBindByName(converter = ConvertSpanishToBoolean.class, column = "Guardado")
     private boolean guardado;

     @CsvBindByName(column = "Observaciones")
     private String observaciones;
     // Getters and setters

    public String getCodigo() { return codigo;    }
    public void setCodigo(String codigo) {        this.codigo = codigo;    }
    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }
    public String getFormato() {        return formato;    }
    public void setFormato(String formato) {        this.formato = formato;    }
    public int getOrigen() {        return origen;    }
    public void setOrigen(int origen) {       this.origen = origen;    }
    public boolean isManual() {        return manual;    }
    public void setManual(boolean manual) {        this.manual = manual;    }
    public boolean isCaja() {        return caja;    }
    public void setCaja(boolean caja) {        this.caja = caja;    }
    public Date getCreatedAt() {       return createdAt;    }
    public void setCreatedAt(Date createdAt) {        this.createdAt = createdAt;    }
    public String getUbicacion() {        return ubicacion;    }
    public void setUbicacion(String ubicacion) {        this.ubicacion = ubicacion;    }
    public boolean isGuardado() {        return guardado;    }
    public void setGuardado(boolean guardado) {        this.guardado = guardado;    }
    public List<Integer> getSoftware() {        return software;    }
    public void setSoftware(List<Integer> software) {        this.software = software;    }
    public String getObservaciones() {        return observaciones;    }
    public void setObservaciones(String observaciones) {        this.observaciones = observaciones;    }

}
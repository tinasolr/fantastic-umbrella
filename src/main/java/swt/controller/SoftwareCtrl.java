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
import org.apache.log4j.*;
import swt.DAO.*;
import swt.model.*;

/**
 *
 * @author tinar
 */
public class SoftwareCtrl {

    private static Logger log = Logger.getLogger(Logger.class);
    private static List<Software> sws = new ArrayList<>();
    private Software sw;
    private SoftwareDB swDB;
    private MediosCtrl medCtrl;
    private MediosDB medDB;
    private SistOpDB soDB;
    private ExtrasCtrl exCtrl;
    private ExtrasDB extrasDB;;
    private List<Software> swDeMed;

    public SoftwareCtrl(){
        this.swDB = new SoftwareDB();
        this.swDeMed = new ArrayList<>();
        this.extrasDB = new ExtrasDB();
        this.exCtrl = new ExtrasCtrl();
        this.medDB = new MediosDB();
        this.medCtrl = new MediosCtrl();
        this.swDB = new SoftwareDB();
        this.soDB = new SistOpDB();
    }

    //FIND Software de medio
    public void softwareDeMedio(String cod){
        log.trace("Loading software de medio to list...");
        swDeMed.clear();
        if(sws.isEmpty())
            cargarSoftware();
        for(Software s : sws)
            for(Medios m : s.getMedios())
                if(m.getCodigo().equalsIgnoreCase(cod)){
                    swDeMed.add(s);
                    break;
                }
        log.trace("Done.");
    }

    //INGRESO DE SOFTWARE
    public void cargarSoftware(){

        sws.clear();
        List<SoftwareDB> swdb = swDB.read();

        for(SoftwareDB s : swdb){

            int codigo = s.getCodigo();
            String nombre = s.getNombre();
            if(nombre!=null && !nombre.isEmpty()){
                String version = s.getVersion();
                //BUSCAR SO
                List<SistOpDB> sistOp = soDB.sistopDeSoftware(codigo);
                List<String> so = new ArrayList<>();
                if(!sistOp.isEmpty()){
                    for(SistOpDB si : sistOp)
                        so.add(si.getNombre());
                }
                //BUSCAR MEDIOS
    //            medCtrl = new MediosCtrl();
    //            medCtrl.cargarMedios();
                List<MediosDB> m = medDB.mediosDeSoftware(codigo);
                List<Medios> medios = new ArrayList<>();
                if(m!=null && !m.isEmpty()){
                    medCtrl.buscarMedios(m);
                    medios = medCtrl.getMedSw();
                }
                //CREAR SOFTWARE
                Software soft = new Software(codigo, nombre, so, version, medios);
                //AGREGAR EXTRAS
                List<ExtrasDB> ex = extrasDB.extrasDeSoftware(codigo);
                for(ExtrasDB x : ex){
                    soft.setExtras(x.getNombre(),x.getVersion(),x.getDescrip(),x.getPartes());
                }

                sws.add(soft);
            }
        }
    }

    public Software findSoftware(int codigo){
        for(Software s : sws){
            if(s.getCodigo() == codigo)
                return s;
        }
        return null;
    }

    public void eliminarSoDeSw(int codigo, String sistOp){
        swDB.deleteSOSw(sistOp, codigo);
    }

    public void agregarSoDeSw(int codigo, String sistOp){
        swDB.insertSOSw(sistOp, codigo);
    }

     public boolean altaSoftware(String nombre, String version){
        boolean ok = true;
         swDB  = new SoftwareDB();
        String id = swDB.search(nombre, version);
        if(id==null)
            swDB.write(nombre, version);
        else ok = false;
        return ok;
    }

     public void modSoftware(String nombre, String version, String newNom, String newVers){
        swDB.update(nombre, newNom, version, newVers);
    }

     public void elimSoftware(int codigo){
        swDB.delete(codigo);
    }

    public List<Software> getSws() {
        return sws;
    }

    public void setSws(List<Software> sws) {
        this.sws = sws;
    }

    public Software getSw() {
        return sw;
    }

    public void setSw(Software sw) {
        this.sw = sw;
    }

    public SoftwareDB getSwDB() {
        return swDB;
    }

    public void setSwDB(SoftwareDB swDB) {
        this.swDB = swDB;
    }

    public MediosCtrl getMedCtrl() {
        return medCtrl;
    }

    public void setMedCtrl(MediosCtrl medCtrl) {
        this.medCtrl = medCtrl;
    }

    public MediosDB getMedDB() {
        return medDB;
    }

    public void setMedDB(MediosDB medDB) {
        this.medDB = medDB;
    }

    public SistOpDB getSoDB() {
        return soDB;
    }

    public void setSoDB(SistOpDB soDB) {
        this.soDB = soDB;
    }

    public ExtrasCtrl getExCtrl() {
        return exCtrl;
    }

    public void setExCtrl(ExtrasCtrl exCtrl) {
        this.exCtrl = exCtrl;
    }

    public ExtrasDB getExtrasDB() {
        return extrasDB;
    }

    public void setExtrasDB(ExtrasDB extrasDB) {
        this.extrasDB = extrasDB;
    }

    public List<Software> getSwDeMed() {
        return swDeMed;
    }

    public void setSwDeMed(List<Software> swDeMed) {
        this.swDeMed = swDeMed;
    }


}

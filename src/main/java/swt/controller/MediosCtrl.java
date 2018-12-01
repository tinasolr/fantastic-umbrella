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
import swt.view.*;

/**
 *
 * @author tinar
 */
public class MediosCtrl {

    private static Logger log = Logger.getLogger(Logger.class);
    private List<Medios> medSw = new ArrayList<>();
    private MediosDB mdb = new MediosDB();
    private List<MediosDB> deDB = new ArrayList<>();
    private static List<Medios> medios = new ArrayList<>();
    private static List<MediosTableFormat> mediosForTable = new ArrayList<>();

    /**
     * Find and create all medios in list of DB objects.
     * @param m List of MediosDB
     */
    public void buscarMedios(List<MediosDB> m) {

        medSw.clear();

        log.trace("Searching for medios in list...");

        if(medios.isEmpty())
            cargarMedios();

        for(MediosDB x : m){
            FormatoDB f = new FormatoDB();

            String formato = f.search(x.getFormid());
            boolean estaEnDepo = x.isMedioEnDepo(x.getId());

            Ubicaciones ubicacion = new UbicacionesCtrl().findUbicacion(x.getUbic());
            medSw.add(findMedio(x.getId()));
        }

        log.trace("Done.");

    }

    /**
     * Load all medios.
     */
    public void cargarMedios() {
        medios.clear();
        deDB = mdb.read();

        log.trace("Loading medios...");

        for(MediosDB x : deDB){
            FormatoDB f = new FormatoDB();

            String formato = f.search(x.getFormid());
            boolean estaEnDepo = x.isMedioEnDepo(x.getId());

            Ubicaciones ubicacion = new UbicacionesCtrl().findUbicacion(x.getUbic());
            Medios nuevo = new Medios(x.getId(), x.getNombre(), formato, x.isCaja(),
                    x.isManual(), x.getOrigen(), ubicacion, estaEnDepo, x.getImagen(),
                    x.getObserv(), x.getPartes());

            CopiasCtrl copCtrl = new CopiasCtrl();
            copCtrl.cargarCopias(x.getId());
            for(Copias c: copCtrl.getCopias()){
                nuevo.setCopias(c.getId(),c.getFormato(),c.getUbiDepo(),c.getObserv(), c.isEnDepo());
            }

            medios.add(nuevo);

        }

        log.trace("Done.");

    }

    /**
     * Format Medio objects into Table-compatible objects.
     */
    public void formatMediosForTable(){
        if(medios.isEmpty()){
            log.trace("Medios is empty");
            cargarMedios();
        }
        log.trace("Formatting medios for table...");
        mediosForTable.clear();
        for(Medios m : medios){
            String isOrig = m.getOrigen()==1?"Sí" : "No";
            String enDep = m.isEnDepo()?"Sí":"No";

            mediosForTable.add(new MediosTableFormat(m.getCodigo(), m.getNombre(), m.getFormato(), isOrig, m.getUbiDepo().getId(),enDep, m.getCopias().size()));
        }

        log.trace("Formatted madios for table.");

    }

    /**
     * Find a medio in Medios list.
     * @param codigo
     * @return Medios
     */
    public Medios findMedio(String codigo){
       for(Medios x : medios){
           if(x.getCodigo().equalsIgnoreCase(codigo))
               return x;
       }
       return null;
    }

    /**
     * Create Medio.
     * @param codigo codigo de medio
     * @param nombre nombre de medio
     * @param formId codigo de formato
     * @param caja
     * @param manual
     * @param origen 1-Original, 2-Mixto, 3-Otro
     * @param ubiDepo
     * @param enDepo
     * @param imagen
     * @param observ
     * @param partes
     * @param soft
     * @return true if OK, false if not.
     */
    public boolean altaMedio(String codigo, String nombre, int formId, boolean caja,
        boolean manual, int origen, Ubicaciones ubiDepo, boolean enDepo,
        String imagen, String observ, int partes, List<Software> soft){

         log.trace("Creating medio...");

        MediosDB mdb = new MediosDB();
        mdb.write(codigo, nombre, partes, manual, caja, imagen, observ, formId, origen);
        boolean todoOK = true;
        for(Software s : soft){
            mdb.setSwid(s.getCodigo());
            todoOK = mdb.asociarMedioASoftware(codigo, s.getCodigo());
        }
        if(ubiDepo==null)
            ubiDepo = new UbicacionesCtrl().findUbicacion("NOASIG");
        mdb.setUbic(ubiDepo.getId());
        mdb.setEnDepo(enDepo);
        todoOK = mdb.asociarUbicacionAMedio(codigo, ubiDepo.getId(), enDepo);

        log.trace("Done.");

        return todoOK;
    }

    /**
     * Modify values of a Medio.
     * @param nuevoid nuevo codigo de medio
     * @param id codigo de medio
     * @param nombre nombre de medio
     * @param formid codigo de formato
     * @param caja
     * @param manual
     * @param origen 1-Original, 2-Mixto, 3-Otro
     * @param ubiDepo
     * @param enDepo
     * @param imagen
     * @param observ
     * @param partes
     * @param soft
     * @return true if OK, false if not.
     */
    public boolean modMedio(String nuevoid, String id, String nombre, int formid, boolean caja,
        boolean manual, int origen, Ubicaciones ubiDepo, boolean enDepo,
        String imagen, String observ, int partes, List<Software> soft) {

        boolean todoOK = true;

        try {
            //CREATE Medio in DB

            log.trace("Modifying medio...");
                mdb.update(id, nuevoid, nombre, partes, manual, caja, imagen, observ, formid, origen);
            log.trace("Done.");

            SoftwareCtrl sctrl = new SoftwareCtrl();
            sctrl.softwareDeMedio(id);

            //UPDATE SOFTWARE-MEDIO RELATIONS
            //Find uncommon elements between actual list and new list - returns those that were in A and are nor in B

            Set<Software> borrados = findUnCommon(sctrl.getSwDeMed(), soft);

            log.trace("Deleting software-medio relations...");

                Iterator<Software> it = borrados.iterator();
                while(it.hasNext()){
                    Software x = it.next();
                    todoOK = mdb.desasociarMedioASoftware(id, x.getCodigo());
                }

            log.trace("Done.");
            if(!todoOK) throw new Exception("Error - Deleting medio-software relations.");

            //Find uncommon elements between new list and old list - returns those that were in A and are nor in B

            Set<Software> agregados = findUnCommon(soft, sctrl.getSwDeMed());

            log.trace("Creating software-medio relations...");

                Iterator<Software> it2 = agregados.iterator();
                while(it2.hasNext()){
                    Software x = it2.next();
                    todoOK = mdb.asociarMedioASoftware(nuevoid, x.getCodigo());
                }

            log.trace("Done.");
            if(!todoOK) throw new Exception("Error - Creating medio-software relations.");

            //find Medio (unchanged)
            Medios m = findMedio(id);

            //UPDATE MEDIO-UBICACION RELATIONS

            log.trace("Eliminating ubicacion-medio relationship...");

                if(ubiDepo==null)
                    ubiDepo = new UbicacionesCtrl().findUbicacion("NOASIG");
                todoOK = mdb.desasociarUbicacionAMedio(nuevoid, m.getUbiDepo().getId());

            if(!todoOK) throw new Exception("Error - Elimination medio-ubicacion relation.");
            log.trace("Done.");

            log.trace("Creating ubicacion-medio relationship...");

                todoOK = mdb.asociarUbicacionAMedio(nuevoid, ubiDepo.getId(), enDepo);

            if(!todoOK) throw new Exception("Error - Creating medio-ubicacion relation.");
            log.trace("Done.");

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            PopUp.popUpException(ex);
        }
        return todoOK;
    }

    /**
     * Eliminate Medio from list and DB.
     * @param codigo
     */
    public void elimMedio(String codigo){
        medios.remove(findMedio(codigo));
        mdb.delete(codigo);
    }

    /**
     * Finds uncommon elements between two lists.
     * @param listaUno
     * @param listaDos
     * @return a set of Software containing those that were not in ListaDos
     */
    public Set<Software> findUnCommon(List<Software> listaUno , List<Software> listaDos){

        Set<Software> a = new HashSet<>(listaUno);
        Set<Software> b = new HashSet<>(listaDos);

        Set<Software> result = new HashSet<>();
        for (Software el: a) {
          if (!b.contains(el)) {
            result.add(el);
          }
        }
        return result;
    }
    public List<Medios> getMedSw() {        return medSw;    }
    public void setMedSw(List<Medios> medSw) {        this.medSw = medSw;    }
    public MediosDB getMdb() {   return mdb;    }
    public void setMdb(MediosDB mdb) {        this.mdb = mdb;    }
    public List<MediosDB> getDeDB() {        return deDB;    }
    public void setDeDB(List<MediosDB> deDB) {        this.deDB = deDB;    }
    public static List<Medios> getMedios() {        return medios;    }
    public static void setMedios(List<Medios> med) {        medios = med;    }
    public List<MediosTableFormat> getMediosForTable() {   return mediosForTable;  }
}

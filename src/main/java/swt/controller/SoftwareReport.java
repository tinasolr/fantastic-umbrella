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

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package Controller;
//
//import Model.*;
//import Vista.*;
//import ar.com.fdvs.dj.core.*;
//import ar.com.fdvs.dj.core.layout.*;
//import ar.com.fdvs.dj.domain.*;
//import ar.com.fdvs.dj.domain.builders.*;
//import ar.com.fdvs.dj.domain.constants.Font;
//import ar.com.fdvs.dj.domain.constants.Transparency;
//import ar.com.fdvs.dj.domain.constants.*;
//import ar.com.fdvs.dj.domain.entities.*;
//import ar.com.fdvs.dj.domain.entities.columns.*;
//import java.awt.*;
//import java.util.*;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.*;
//
///**
// * @author tinar
// */
//
//public class SoftwareReport{
//
//    private final Collection<SoftwarePOJO> list = new ArrayList<>();
//
//    public SoftwareReport(Collection<Software> c) {
//        for(Software x : c){
//            for(Extras e : x.getExtras())
//                list.add(new SoftwarePOJO(x.getCodigo(), x.getNombre(), String.join(", ", x.getSistOp()), x.getVersion(), e.getNombre(), e.getVersion(), e.getDescrip(), e.getPartes()));
//        }
//    }
//
//    public JasperPrint getReport() throws ColumnBuilderException, JRException, ClassNotFoundException {
//        Style headerStyle = createHeaderStyle();
//        Style detailTextStyle = createDetailTextStyle();
//        Style detailNumberStyle = createDetailNumberStyle();
//        DynamicReport dynaReport = getReport(headerStyle, detailTextStyle,detailNumberStyle);
//        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), new JRBeanCollectionDataSource(list));
//        return jp;
//    }
//
//    private Style createHeaderStyle() {
//        StyleBuilder sb=new StyleBuilder(true);
//        sb.setFont(Font.VERDANA_MEDIUM_BOLD);
//        sb.setBorder(Border.THIN());
//        sb.setBorderBottom(Border.THIN());
//        sb.setBorderColor(Color.LIGHT_GRAY);
//        sb.setBackgroundColor(Color.GRAY);
//        sb.setStretchWithOverflow(true);
//        sb.setTextColor(Color.WHITE);
//        sb.setHorizontalAlign(HorizontalAlign.CENTER);
//        sb.setVerticalAlign(VerticalAlign.MIDDLE);
//        sb.setTransparency(Transparency.OPAQUE);
//        return sb.build();
//    }
//
//    private Style createDetailTextStyle(){
//        StyleBuilder sb=new StyleBuilder(true);
//        sb.setFont(Font.VERDANA_MEDIUM);
//        sb.setBorder(Border.THIN());
//        sb.setBorderColor(Color.LIGHT_GRAY);
//        sb.setTextColor(Color.BLACK);
//        sb.setStretchWithOverflow(true);
//        sb.setHorizontalAlign(HorizontalAlign.LEFT);
//        sb.setVerticalAlign(VerticalAlign.MIDDLE);
//        sb.setPaddingLeft(5);
//        return sb.build();
//    }
//
//    private Style createDetailNumberStyle(){
//        StyleBuilder sb=new StyleBuilder(true);
//        sb.setFont(Font.VERDANA_MEDIUM);
//        sb.setBorder(Border.THIN());
//        sb.setBorderColor(Color.LIGHT_GRAY);
//        sb.setTextColor(Color.BLACK);
//        sb.setStretchWithOverflow(true);
//        sb.setHorizontalAlign(HorizontalAlign.RIGHT);
//        sb.setVerticalAlign(VerticalAlign.MIDDLE);
//        sb.setPaddingRight(5);
//        return sb.build();
//    }
//
//    private AbstractColumn createColumn(String property, Class type,
//        String title, int width, Style headerStyle, Style detailStyle)
//        throws ColumnBuilderException {
//        AbstractColumn columnState = ColumnBuilder.getNew()
//                .setColumnProperty(property, type.getName()).setTitle(
//                        title).setWidth(Integer.valueOf(width))
//                .setStyle(detailStyle).setHeaderStyle(headerStyle).build();
//        return columnState;
//    }
//
//    private DynamicReport getReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle) throws ColumnBuilderException, ClassNotFoundException {
//
//        DynamicReportBuilder report=new DynamicReportBuilder();
//        report.setPrintColumnNames(true);
//
//        AbstractColumn columnSwNo = createColumn("codigo", Integer.class,"No. Software", 20, headerStyle, detailTextStyle);
//        AbstractColumn columnName = createColumn("nombre", String.class,"Nombre", 40, headerStyle, detailTextStyle);
//        AbstractColumn columnVersion = createColumn("version", String.class,"Versión", 15, headerStyle, detailNumStyle);
//        AbstractColumn columnExNom = createColumn("exNomb", String.class,"Nombre Util.", 40, headerStyle, detailTextStyle);
//        AbstractColumn columnExVers = createColumn("exVers", String.class,"Versión Util.", 15, headerStyle, detailTextStyle);
//        AbstractColumn columnExDesc = createColumn("exDescr", String.class,"Descrip. Util.", 60, headerStyle, detailTextStyle);
//        AbstractColumn columnExPartes = createColumn("exPartes", Integer.class,"Partes Util.", 10, headerStyle, detailNumStyle);
//
//        GroupBuilder gb1 = new GroupBuilder();
//        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columnSwNo)
//                .setHeaderHeight(new Integer(20))
//                .setGroupLayout(GroupLayout.DEFAULT)
//                .build();
//
//        report.addColumn(columnSwNo).addColumn(columnName)
//                .addColumn(columnVersion).addColumn(columnExNom)
//                .addColumn(columnExVers).addColumn(columnExDesc)
//                .addColumn(columnExPartes);
//
//        report.addGroup(g1);
//
//        report.setPageSizeAndOrientation(Page.Page_A4_Landscape());
//        StyleBuilder titleStyle=new StyleBuilder(true);
//        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
//        titleStyle.setFont(new Font(20, Font.GEORGIA_MEDIUM.getPdfFontName(), true));
//
//        StyleBuilder subTitleStyle=new StyleBuilder(true);
//        subTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
//        subTitleStyle.setFont(new Font(Font.MEDIUM, Font.GEORGIA_MEDIUM.getPdfFontName(), true));
//
//        report.setTitle("Software Report");
//        report.setTitleStyle(titleStyle.build());
//        report.setSubtitle("Software a la fecha: " + new java.util.Date());
//        report.setSubtitleStyle(subTitleStyle.build());
//        report.setUseFullPageWidth(true);
//        return report.build();
//    }
//}

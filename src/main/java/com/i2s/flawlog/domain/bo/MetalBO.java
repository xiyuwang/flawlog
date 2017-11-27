package com.i2s.flawlog.domain.bo;

import java.sql.Time;
import java.util.Date;

/**
 * Created by wxy on 2017/11/26.
 */
public class MetalBO {
    private Integer id;
    private String gaugeclass;
    private String imagette;
   //private String hostname;
    private double posx;
    private double posy;
    private double length;
    private double width;
    private double area;
    private String time;
    private String leftedge;
    private String rightedge;
    private String refpos;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getGaugeclass() {
        return gaugeclass;
    }
    public void setGaugeclass(String gaugeclass) {
        this.gaugeclass = gaugeclass;
    }
    public String getImagette() {
        return imagette;
    }
    public void setImagette(String imagette) {
        this.imagette = imagette;
    }
   // public String getHostname() {
   //     return hostname;
    //}
   // public void setHostname(String name) {
     //   this.hostname = name;
    //}
    public double getPosx() {
        return posx;
    }
    public void setPosx(double posx) {
        this.posx = posx;
    }
    public double getPosy() {
        return posy;
    }
    public void setPosy(double posy) {
        this.posy = posy;
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getArea() {
        return area;
    }
    public void setArea(double area) {
        this.area = area;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getLeftedge() {
        return leftedge;
    }
    public void setLeftedge(String leftedge) {
        this.leftedge = leftedge;
    }
    public String getRightedge() {
        return rightedge;
    }
    public void setRightedge(String rightedge) {
        this.rightedge = rightedge;
    }
    public String getRefpos() {
        return refpos;
    }
    public void setRefpos(String refpos) {
        this.refpos = refpos;
    }
    public String toString(){
        return " id:"+getId()+"gaugeclass"+gaugeclass+" imagette:"+getImagette()+" posx:"+
                getPosx()+" posy:"+getPosy()+" length:"+getLength()+" width:"+getWidth()+" area:"+getArea()+
                " time:"+getTime()+" leftedge:"+getLeftedge()+" rightedge:"+getRightedge()+" refpos:"+getRefpos();
    }
}

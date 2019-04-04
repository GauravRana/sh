package com.sydehealth.sydehealth.model;

import com.sydehealth.sydehealth.model.PowerAIRes;

import java.util.ArrayList;
import java.util.List;

public class Classified  {

    /**
     *  gaurav
     *  event bus object to pass it on to another class.
     * @return
     */

    public ArrayList<PowerAIRes.Classified> classified;


    private Double confidence;
    private Integer ymax;
    private String label;
    private Integer xmax;
    private Integer xmin;
    private Integer ymin;


    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Integer getYmax() {
        return ymax;
    }

    public void setYmax(Integer ymax) {
        this.ymax = ymax;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getXmax() {
        return xmax;
    }

    public void setXmax(Integer xmax) {
        this.xmax = xmax;
    }

    public Integer getXmin() {
        return xmin;
    }

    public void setXmin(Integer xmin) {
        this.xmin = xmin;
    }

    public Integer getYmin() {
        return ymin;
    }

    public void setYmin(Integer ymin) {
        this.ymin = ymin;
    }


    public void setClassified(ArrayList<PowerAIRes.Classified> classified) {
        this.classified = classified;

    }


    public List<PowerAIRes.Classified> getClassified() {
        return classified;
    }

}

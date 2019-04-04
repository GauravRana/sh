package com.sydehealth.sydehealth.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerAIRes {

    private String webAPIId;
    private String imageUrl;
    private String imageMd5;
    private ArrayList<Classified> classified = null;
    private String result;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getWebAPIId() {
        return webAPIId;
    }

    public void setWebAPIId(String webAPIId) {
        this.webAPIId = webAPIId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageMd5() {
        return imageMd5;
    }

    public void setImageMd5(String imageMd5) {
        this.imageMd5 = imageMd5;
    }

    public ArrayList<Classified> getClassified() {
        return classified;
    }

    public void setClassified(ArrayList<Classified> classified) {
        this.classified = classified;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public class Classified  {

        private Double confidence;
        private Integer ymax;
        private String label;
        private Integer xmax;
        private Integer xmin;
        private Integer ymin;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }
    }
}


package com.sydehealth.sydehealth.model;

public class TreatmentImages {

    String path;
    boolean isSelected=false;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

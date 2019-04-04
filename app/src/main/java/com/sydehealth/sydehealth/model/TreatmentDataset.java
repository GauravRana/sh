package com.sydehealth.sydehealth.model;

import java.util.ArrayList;

public class TreatmentDataset implements Cloneable {

    private Integer id;
    private String headingName;
    private String preventionAdvice;
    private boolean isSelected;
    private ArrayList<EmailTreatmentResponse.SubTreatmentOption_> childDataSet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadingName() {
        return headingName;
    }

    public void setHeadingName(String headingName) {
        this.headingName = headingName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPreventionAdvice() {
        return preventionAdvice;
    }

    public void setPreventionAdvice(String preventionAdvice) {
        this.preventionAdvice = preventionAdvice;
    }

    public ArrayList<EmailTreatmentResponse.SubTreatmentOption_> getChildDataSet() {
        if (childDataSet == null)
            childDataSet = new ArrayList<>();

        return childDataSet;
    }

    public void setChildDataSet(ArrayList<EmailTreatmentResponse.SubTreatmentOption_> childDataSet) {
        this.childDataSet = childDataSet;
    }

    public ArrayList<EmailTreatmentResponse.SubTreatmentOption_> getSelectedChildDataSet() {
        if (childDataSet == null)
            childDataSet = new ArrayList<>();

        ArrayList<EmailTreatmentResponse.SubTreatmentOption_> result =
                (ArrayList<EmailTreatmentResponse.SubTreatmentOption_>)childDataSet.clone();

                result.removeIf(SubTreatmentOption_ -> !SubTreatmentOption_.isSelected());

        return result;
    }

    public TreatmentDataset clone() throws CloneNotSupportedException {
        return (TreatmentDataset) super.clone();
    }
}

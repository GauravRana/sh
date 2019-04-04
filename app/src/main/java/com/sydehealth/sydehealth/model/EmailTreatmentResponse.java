package com.sydehealth.sydehealth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EmailTreatmentResponse {

    @SerializedName("presentation")
    @Expose
    private Presentation presentation;

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }


    public class Presentation {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("treatment_options")
        @Expose
        private ArrayList<TreatmentOption> treatmentOptions = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<TreatmentOption> getTreatmentOptions() {
            return treatmentOptions;
        }

        public void setTreatmentOptions(ArrayList<TreatmentOption> treatmentOptions) {
            this.treatmentOptions = treatmentOptions;
        }

    }


    public class SubTreatmentOption {

        @SerializedName("sub_treatment_option")
        @Expose
        private SubTreatmentOption_ subTreatmentOption;

        public SubTreatmentOption_ getSubTreatmentOption() {
            return subTreatmentOption;
        }

        public void setSubTreatmentOption(SubTreatmentOption_ subTreatmentOption) {
            this.subTreatmentOption = subTreatmentOption;
        }

    }


    public class SubTreatmentOption_ {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("sub_treatment_option_image_content_type")
        @Expose
        private String subTreatmentOptionImageContentType;
        @SerializedName("sub_treatment_option_image")
        @Expose
        private String subTreatmentOptionImage;
        //local
        private boolean isSelected;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubTreatmentOptionImageContentType() {
            return subTreatmentOptionImageContentType;
        }

        public void setSubTreatmentOptionImageContentType(String subTreatmentOptionImageContentType) {
            this.subTreatmentOptionImageContentType = subTreatmentOptionImageContentType;
        }

        public String getSubTreatmentOptionImage() {
            return subTreatmentOptionImage;
        }

        public void setSubTreatmentOptionImage(String subTreatmentOptionImage) {
            this.subTreatmentOptionImage = subTreatmentOptionImage;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }


    public class TreatmentOption {

        @SerializedName("treatment_option")
        @Expose
        private TreatmentOption_ treatmentOption;

        public TreatmentOption_ getTreatmentOption() {
            return treatmentOption;
        }

        public void setTreatmentOption(TreatmentOption_ treatmentOption) {
            this.treatmentOption = treatmentOption;
        }

    }


    public class TreatmentOption_ {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("prevention_advice")
        @Expose
        private String preventionAdvice;
        @SerializedName("sub_treatment_options")
        @Expose
        private ArrayList<SubTreatmentOption> subTreatmentOptions = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPreventionAdvice() {
            return preventionAdvice;
        }

        public void setPreventionAdvice(String preventionAdvice) {
            this.preventionAdvice = preventionAdvice;
        }

        public ArrayList<SubTreatmentOption> getSubTreatmentOptions() {
            return subTreatmentOptions;
        }

        public void setSubTreatmentOptions(ArrayList<SubTreatmentOption> subTreatmentOptions) {
            this.subTreatmentOptions = subTreatmentOptions;
        }

    }

}
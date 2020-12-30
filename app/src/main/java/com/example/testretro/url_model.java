package com.example.testretro;

import com.google.gson.annotations.SerializedName;

public class url_model {
    @SerializedName("url")
    private String url;

    //Alzheimer
    @SerializedName("Mild Demented")
    private String Mild_Demented;
    @SerializedName("Moderate Demented")
    private String Moderate_Demented;
    @SerializedName("Non Demented")
    private String Non_Demented;
    @SerializedName("Very Mild Demented")
    private String Very_Mild_Demented;

    //Eye blindness
    @SerializedName("Mild Diabetic Retinopathy")
    private String Mild_Diabetic_Retinopathy;
    @SerializedName("Moderate Diabetic Retinopathy")
    private String Moderate_Diabetic_Retinopathy;
    @SerializedName("No Diabetic Retinopathy")
    private String No_Diabetic_Retinopathy;
    @SerializedName("Proliferative Diabetic Retinopathy")
    private String Proliferative_Diabetic_Retinopathy;
    @SerializedName("Severe Diabetic Retinopathy")
    private String Severe_Diabetic_Retinopathy;

    //Pneumonia
    @SerializedName("Normal")
    private String Normal;
    @SerializedName("Pneumonia")
    private String Pneumonia;

    //Brain Tumor
    @SerializedName("Glioma_tumor")
    private String Glioma_tumor;
    @SerializedName("Meningioma_tumor")
    private String Meningioma_tumor;
    @SerializedName("No_tumor")
    private String No_tumor;
    @SerializedName("Pituitary_tumor")
    private String Pituitary_tumor;

    //Skin cancer
    @SerializedName("Actinic Keratosis (Benign)")
    private String Actinic_Keratosis;
    @SerializedName("Basal Cell Carcinoma (Benign)")
    private String Basal_Cell_Carcinoma;
    @SerializedName("Benign Keratosis (Benign)")
    private String Benign_Keratosis;
    @SerializedName("Dermatofibroma (Non Cancerous-Benign)")
    private String Dermatofibroma;
    @SerializedName("Melanocytic Nevus /Normal Skin /Rash (Benign)")
    private String Melanocytic_Nevus;
    @SerializedName("Melanoma (Malignant)")
    private String Melanoma;
    @SerializedName("Squamous Cell Carcinoma (Malignant)")
    private String Squamous_Cell_Carcinoma;
    @SerializedName("Vascular Lesion (maybe Benign maybe Malignant)")
    private String Vascular_Lesion;

    //Covid
    @SerializedName("Covid")
    private String Covid;
    @SerializedName("Non-Covid")
    private String Non_Covid;

    //BreastCancer
    @SerializedName("Benign")
    private String Benign;
    @SerializedName("Malignant")
    private String Malignant;
    @SerializedName("NormalB")
    private String NormalBreastCancer;

    public url_model(String url)
    {
        this.url=url;
    }

    public String getMild_Demented() {
        return Mild_Demented;
    }

    public String getModerate_Demented() {
        return Moderate_Demented;
    }

    public String getNon_Demented() {
        return Non_Demented;
    }

    public String getVery_Mild_Demented() {
        return Very_Mild_Demented;
    }

    public String getNormal() {
        return Normal;
    }

    public String getPneumonia() {
        return Pneumonia;
    }

    public String getMild_Diabetic_Retinopathy() {
        return Mild_Diabetic_Retinopathy;
    }

    public String getModerate_Diabetic_Retinopathy() {
        return Moderate_Diabetic_Retinopathy;
    }

    public String getNo_Diabetic_Retinopathy() {
        return No_Diabetic_Retinopathy;
    }

    public String getProliferative_Diabetic_Retinopathy() {
        return Proliferative_Diabetic_Retinopathy;
    }

    public String getSevere_Diabetic_Retinopathy() {
        return Severe_Diabetic_Retinopathy;
    }

    public String getGlioma_tumor() {
        return Glioma_tumor;
    }

    public String getMeningioma_tumor() {
        return Meningioma_tumor;
    }

    public String getNo_tumor() {
        return No_tumor;
    }

    public String getPituitary_tumor() {
        return Pituitary_tumor;
    }

    public String getActinic_Keratosis() {
        return Actinic_Keratosis;
    }

    public String getBasal_Cell_Carcinoma() {
        return Basal_Cell_Carcinoma;
    }

    public String getBenign_Keratosis() {
        return Benign_Keratosis;
    }

    public String getDermatofibroma() {
        return Dermatofibroma;
    }

    public String getMelanocytic_Nevus() {
        return Melanocytic_Nevus;
    }

    public String getMelanoma() {
        return Melanoma;
    }

    public String getSquamous_Cell_Carcinoma() {
        return Squamous_Cell_Carcinoma;
    }

    public String getVascular_Lesion() {
        return Vascular_Lesion;
    }

    public String getCovid() {
        return Covid;
    }

    public String getNon_Covid() {
        return Non_Covid;
    }

    public String getBenign() {
        return Benign;
    }

    public String getMalignant() {
        return Malignant;
    }

    public String getNormalBreastCancer() {
        return NormalBreastCancer;
    }
}


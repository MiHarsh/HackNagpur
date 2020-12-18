package com.example.testretro;

import com.google.gson.annotations.SerializedName;

public class url_model {
    @SerializedName("url")
    private String url;
    @SerializedName("result")
    private String result;
    public url_model(String url)
    {
        this.url=url;
    }

    public String getResult() {
        return result;
    }
}


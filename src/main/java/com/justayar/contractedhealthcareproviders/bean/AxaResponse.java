package com.justayar.contractedhealthcareproviders.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AxaResponse {

    private List<AxaHealthCareProvider> data;

    public List<AxaHealthCareProvider> getData() {
        return data;
    }

    public void setData(List<AxaHealthCareProvider> data) {
        this.data = data;
    }
}

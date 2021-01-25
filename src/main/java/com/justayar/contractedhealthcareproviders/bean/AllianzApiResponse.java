package com.justayar.contractedhealthcareproviders.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllianzApiResponse {

    private long timestamp;
    private List<AllianzHealthCareProvider> response;
    private int responseCode;

    public AllianzApiResponse() {
    }

    public AllianzApiResponse(long timestamp, List<AllianzHealthCareProvider> response, int responseCode) {
        this.timestamp = timestamp;
        this.response = response;
        this.responseCode = responseCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<AllianzHealthCareProvider> getResponse() {
        return response;
    }

    public void setResponse(List<AllianzHealthCareProvider> response) {
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}

package com.justayar.contractedhealthcareproviders.api.anadoluinsurance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justayar.contractedhealthcareproviders.bean.HealthCareProvider;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AnadoluInsuranceHttpApi {

    private static final String AS_HEALTHCARE_PROVIDER_BASE_URL = "https://www.anadolusigorta.com.tr/Plugins/AnadoluSigorta/AnadoluSigortaWebMethods.ashx";
    private Logger logger = Logger.getLogger(AnadoluInsuranceHttpApi.class.getName());

    public List<HealthCareProvider> getPrivateHealthInsuranceCareProviders(List<NameValuePair> urlParameters) {

        HttpPost post = new HttpPost(AS_HEALTHCARE_PROVIDER_BASE_URL);

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (UnsupportedEncodingException e) {
            logger.info("Unexpected error happened. Exception message is: "+e.getMessage());
        }

        try {
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(EntityUtils.toString(response.getEntity()), new TypeReference<List<HealthCareProvider>>(){
                });
            }
        } catch (IOException e) {
            logger.info("Unexpected error happened. Exception message is: "+e.getMessage());
        }

        return new ArrayList<>();

    }

}

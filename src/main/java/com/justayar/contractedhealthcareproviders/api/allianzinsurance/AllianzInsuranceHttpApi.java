package com.justayar.contractedhealthcareproviders.api.allianzinsurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justayar.contractedhealthcareproviders.bean.AllianzApiResponse;
import com.justayar.contractedhealthcareproviders.bean.AllianzHealthCareProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AllianzInsuranceHttpApi {

    private static final String ALLIANZ_HEALTHCARE_PROVIDER_BASE_URL = "https://digitall.allianz.com.tr/OnewebHealthModule/api/myHealth/search";

    private Logger logger = Logger.getLogger(AllianzInsuranceHttpApi.class.getName());

    public List<AllianzHealthCareProvider> getPrivateHealthInsuranceCareProviders(String requestString) {

        HttpPost post = new HttpPost(ALLIANZ_HEALTHCARE_PROVIDER_BASE_URL);

        post.addHeader("Accept","application/json");
        post.addHeader("Content-Type","application/json");

        try {
            StringEntity requestPayload = new StringEntity(requestString);
            post.setEntity(requestPayload);

        } catch (UnsupportedEncodingException e) {
            logger.info("Unexpected error happened. Exception message is: "+e.getMessage());
        }

        try(CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
                ObjectMapper mapper = new ObjectMapper();
                AllianzApiResponse allianzApiResponse = mapper.readValue(EntityUtils.toString(response.getEntity()), AllianzApiResponse.class);
                return allianzApiResponse.getResponse();
        } catch (IOException e) {
            logger.info("Unexpected error happened. Exception message is: "+e.getMessage());
        }catch (Exception ex){
            ex.getStackTrace();
        }
        return new ArrayList<>();
    }
}

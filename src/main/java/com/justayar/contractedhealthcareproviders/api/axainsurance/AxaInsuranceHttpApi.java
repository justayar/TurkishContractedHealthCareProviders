package com.justayar.contractedhealthcareproviders.api.axainsurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justayar.contractedhealthcareproviders.bean.AxaHealthCareProvider;
import com.justayar.contractedhealthcareproviders.bean.AxaResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AxaInsuranceHttpApi {

    private static final String AXA_HEALTHCARE_PROVIDER_BASE_URL = "https://www.axasigorta.com.tr/Services/Ajax.aspx";

    public List<AxaHealthCareProvider> getPrivateHealthInsuranceCareProviders(List<NameValuePair> urlParameters) {
        final Logger logger = Logger.getLogger(AxaInsuranceHttpApi.class.getName());

        HttpPost post = new HttpPost(AXA_HEALTHCARE_PROVIDER_BASE_URL);

        post.setEntity(new UrlEncodedFormEntity(urlParameters, StandardCharsets.UTF_8));
        post.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

        try {
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
                ObjectMapper mapper = new ObjectMapper();
                AxaResponse axaResponse = mapper.readValue(EntityUtils.toString(response.getEntity()), AxaResponse.class);
                return axaResponse.getData();
            }
        } catch (IOException e) {
            logger.info("Unexpected error happened. Exception message is: "+e.getMessage());
        }

        return new ArrayList<>();

    }
}

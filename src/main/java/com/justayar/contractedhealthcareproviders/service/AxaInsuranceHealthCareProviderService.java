package com.justayar.contractedhealthcareproviders.service;

import com.justayar.contractedhealthcareproviders.api.axainsurance.AxaInsuranceHttpApi;
import com.justayar.contractedhealthcareproviders.bean.AxaHealthCareProvider;
import com.justayar.contractedhealthcareproviders.bean.HealthCareProvider;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AxaInsuranceHealthCareProviderService {

    private static final String HOSPITAL_ORGANIZATION_TYPE="HASTANE";
    private static final String MEDICALCENTER_ORGANIZATION_TYPE="TIP MERKEZİ";
    private static final String PHYSIOTHERAPHY_TYPE="FİZİK TEDAVİ MERKEZİ";
    private static final String MEDICINE_TYPE="ECZANE";
    private static final String DOCTOR_TYPE="DOKTOR";
    private static final String OSS_POLICY_TYPE="ÖZEL SAĞLIK SİGORTA";
    private static final String TSS_POLICY_TYPE="TAMAMLAYICI SİGORTA";

    private Logger logger = Logger.getLogger(AxaInsuranceHealthCareProviderService.class.getName());



    public List<HealthCareProvider> getAxaInsuranceHealthCareProvidersForOSS(List<String> selectedQueries){
        List<HealthCareProvider> healthCareProviders = new ArrayList<>();

        if(selectedQueries.contains("hospital")) {
            logger.info("ÖSS Hastane anlaşmalı kurumlar çekiliyor");
            healthCareProviders = getProviders(HOSPITAL_ORGANIZATION_TYPE, OSS_POLICY_TYPE);
        }


        if(selectedQueries.contains("medicalCenter")) {
            logger.info("ÖSS Tıp merkezi anlaşmalı kurumlar çekiliyor");
            healthCareProviders.addAll(getProviders(MEDICALCENTER_ORGANIZATION_TYPE, OSS_POLICY_TYPE));
        }

        if(selectedQueries.contains("pyhsioCenter")) {
            logger.info("ÖSS Fizik Tedavi merkezi anlaşmalı kurumlar çekiliyor");
            healthCareProviders.addAll(getProviders(PHYSIOTHERAPHY_TYPE, OSS_POLICY_TYPE));
        }

        if(selectedQueries.contains("medicine")){
            logger.info("ÖSS Eczaneler çekiliyor");
            healthCareProviders.addAll(getProviders(MEDICINE_TYPE, OSS_POLICY_TYPE));
        }

        if(selectedQueries.contains("doctors")){
            logger.info("ÖSS Doktorlar çekiliyor");
            healthCareProviders.addAll(getProviders(DOCTOR_TYPE, OSS_POLICY_TYPE));
        }

        return healthCareProviders;
    }

    public List<HealthCareProvider> getAxaInsuranceHealthCareProvidersForTSS(List<String> selectedQueries){
        List<HealthCareProvider> healthCareProviders = new ArrayList<>();

        if(selectedQueries.contains("hospital")) {
            logger.info("TSS Hastane anlaşmalı kurumlar çekiliyor");
            healthCareProviders = getProviders(HOSPITAL_ORGANIZATION_TYPE, TSS_POLICY_TYPE);
        }

        if(selectedQueries.contains("medicalCenter")) {
            logger.info("TSS Tıp merkezi anlaşmalı kurumlar çekiliyor");
            healthCareProviders.addAll(getProviders(MEDICALCENTER_ORGANIZATION_TYPE, TSS_POLICY_TYPE));
        }

        if(selectedQueries.contains("pyhsioCenter")) {
            logger.info("TSS Fizik Tedavi merkezi anlaşmalı kurumlar çekiliyor");
            healthCareProviders.addAll(getProviders(PHYSIOTHERAPHY_TYPE, TSS_POLICY_TYPE));
        }

        return healthCareProviders;
    }



    private List<HealthCareProvider> getProviders(String organizationType,String policyType){
        AxaInsuranceHttpApi axaInsuranceHttpApi = new AxaInsuranceHttpApi();
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("op", "KurumlarSaglikGetir"));
        urlParameters.add(new BasicNameValuePair("policeTuru", policyType));
        urlParameters.add(new BasicNameValuePair("kurumTipi", organizationType));
        urlParameters.add(new BasicNameValuePair("kurumId", "2"));

        List<HealthCareProvider> healthCareProviders = new ArrayList<>();

        List<AxaHealthCareProvider> axaHealthCareProviders = axaInsuranceHttpApi.getPrivateHealthInsuranceCareProviders(urlParameters);
        axaHealthCareProviders.forEach(axaHealthCareProvider -> {
            HealthCareProvider healthCareProvider = mapAxaResponseToHealthCareProvider(organizationType, axaHealthCareProvider);
            healthCareProviders.add(healthCareProvider);
        });

        return healthCareProviders;
    }

    private HealthCareProvider mapAxaResponseToHealthCareProvider(String organizationType, AxaHealthCareProvider axaHealthCareProvider) {
        HealthCareProvider healthCareProvider = new HealthCareProvider();
        healthCareProvider.setOrganizationName(axaHealthCareProvider.getOrganizationName());
        healthCareProvider.setQueryType("Anlaşmalı Sağlık Kurumu");
        healthCareProvider.setOrganizationType(organizationType);
        healthCareProvider.setAddress(axaHealthCareProvider.getAddress());
        healthCareProvider.setCityName(axaHealthCareProvider.getCityName());
        healthCareProvider.setCountryName(axaHealthCareProvider.getCountryName());
        healthCareProvider.setIncludedInAxaInsurance(true);
        if(axaHealthCareProvider.getPhone() != null)
            healthCareProvider.setPhone(axaHealthCareProvider.getPhone().replaceAll("[^\\d]", ""));
        return healthCareProvider;
    }
}

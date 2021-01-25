package com.justayar.contractedhealthcareproviders.service;

import com.justayar.contractedhealthcareproviders.api.allianzinsurance.AllianzInsuranceHttpApi;
import com.justayar.contractedhealthcareproviders.bean.AllianzHealthCareProvider;
import com.justayar.contractedhealthcareproviders.bean.HealthCareProvider;
import com.justayar.contractedhealthcareproviders.bean.Institute;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class AllianzInsuranceHealthCareProviderService {

    private static final String OSS_WHITE_NETWORK_HOSPITAL_STRING="serviceType=CONTRACTEDHEALTH&country=TR&instutionType=1&hospitalType=1&networkType=15&partitionType=MDSG";
    private static final String OSS_WHITE_NETWORK_MEDICALCENTER_STRING="serviceType=CONTRACTEDHEALTH&country=TR&instutionType=1&hospitalType=2&networkType=15&partitionType=MDSG";
    private static final String OSS_WHITE_NETWORK_UNIVERSITY_HOSPITAL_STRING="serviceType=CONTRACTEDHEALTH&country=TR&instutionType=1&hospitalType=17&networkType=15&partitionType=MDSG";
    private static final String OSS_PHYSIOTHERAPHY_STRING = "serviceType=CONTRACTEDHEALTH&country=TR&instutionType=1&hospitalType=5&networkType=15&partitionType=MDSG";

    private static final String SPECIALIST_REQUEST_STRING="serviceType=SPECIALIST&country=TR&&networkType=15&doctorInstutionType=3&partitionType=MDSG";
    private static final String PHARMACY_REQUEST_STRING="serviceType=PHARMACY&country=TR&instutionType=2&hospitalType=1";

    private static final String TSS_HOSPITAL_STRING="serviceType=CONTRACTEDHEALTH&country=TR&instutionType=1&hospitalType=1&networkType=25&partitionType=TSS";
    private static final String TSS_MEDICALCENTER_STRING="serviceType=CONTRACTEDHEALTH&country=TR&instutionType=1&hospitalType=2&networkType=25&partitionType=TSS";
    private static final String TSS_UNIVERSITYHOSPPITAL_STRING="serviceType=CONTRACTEDHEALTH&country=TR&instutionType=1&hospitalType=17&networkType=25&partitionType=TSS";
    private static final String TSS_PHYSIOTHERAPHY_STRING = "serviceType=CONTRACTEDHEALTH&country=TR&instutionType=1&hospitalType=5&networkType=25&partitionType=TSS";

    private Logger logger = Logger.getLogger(AllianzInsuranceHealthCareProviderService.class.getName());

    public List<HealthCareProvider> getAllianzInsuranceHealthCareProvidersForOSS(List<String> selectedQueries) {

        List<AllianzHealthCareProvider> allianzHealthCareProviders = new ArrayList<>();

        if(selectedQueries.contains("hospital")) {
            System.out.println("ÖSS Hastane anlaşmalı kurumlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(OSS_WHITE_NETWORK_HOSPITAL_STRING, "Hastane"));
        }

        if(selectedQueries.contains("medicalCenter")) {
            System.out.println("ÖSS Tıp merkezi anlaşmalı kurumlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(OSS_WHITE_NETWORK_MEDICALCENTER_STRING, "Tıp Merkezi"));
        }


        if(selectedQueries.contains("universityHospital")) {
            System.out.println("ÖSS Üniversite hastaneleri anlaşmalı kurumlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(OSS_WHITE_NETWORK_UNIVERSITY_HOSPITAL_STRING, "Üniversite Hastanesi"));
        }

        if(selectedQueries.contains("pyhsioCenter")) {
            System.out.println("ÖSS Fizik tedavi merkezleri anlaşmalı kurumlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(OSS_PHYSIOTHERAPHY_STRING, "Fizik Tedavi Merkezi"));
        }

        if(selectedQueries.contains("medicine")) {
            System.out.println("ÖSS Eczaneler çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(PHARMACY_REQUEST_STRING, "Eczaneler"));
        }

        if(selectedQueries.contains("doctors")) {
            System.out.println("ÖSS Doktorlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(SPECIALIST_REQUEST_STRING,"Doktorlar"));
        }

        return convertAllianzResponseToHealthCareProviderList(allianzHealthCareProviders);

    }

    public List<HealthCareProvider> getAllianzInsuranceHealthCareProvidersForTSS(List<String> selectedQueries) {

        List<AllianzHealthCareProvider> allianzHealthCareProviders = new ArrayList<>();

        if(selectedQueries.contains("hospital")) {
            System.out.println("TSS Hastane anlaşmalı kurumlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(TSS_HOSPITAL_STRING, "Hastane"));
        }

        if(selectedQueries.contains("medicalCenter")) {
            System.out.println("TSS Tıp merkezi anlaşmalı kurumlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(TSS_MEDICALCENTER_STRING, "Tıp Merkezi"));
        }

        if(selectedQueries.contains("universityHospital")) {
            System.out.println("TSS Üniversite hastaneleri anlaşmalı kurumlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(TSS_UNIVERSITYHOSPPITAL_STRING, "Üniversite Hastanesi"));
        }

        if(selectedQueries.contains("pyhsioCenter")) {
            System.out.println("TSS Fizik tedavi merkezleri anlaşmalı kurumlar çekiliyor");
            allianzHealthCareProviders.addAll(getProviders(TSS_PHYSIOTHERAPHY_STRING, "Fizik Tedavi Merkezi"));
        }

        return convertAllianzResponseToHealthCareProviderList(allianzHealthCareProviders);
    }

    private List<AllianzHealthCareProvider> getProviders(String requestString,String organizationType) {

        AllianzInsuranceHttpApi allianzInsuranceHttpApi = new AllianzInsuranceHttpApi();

        List<AllianzHealthCareProvider> allianzHealthCareProviders = allianzInsuranceHttpApi.getPrivateHealthInsuranceCareProviders(requestString);


        if(requestString.contains("CONTRACTEDHEALTH") || requestString.contains("PHARMACY")){
            mapHospitalsOrPharmacies(requestString, organizationType, allianzHealthCareProviders);
        }else if(requestString.contains("SPECIALIST")){
            mapSpecialists(allianzHealthCareProviders);
        }

        return allianzHealthCareProviders;
    }

    private void mapHospitalsOrPharmacies(String requestString, String organizationType, List<AllianzHealthCareProvider> allianzHealthCareProviders) {
        allianzHealthCareProviders.forEach(provider -> {

            String[] addressFields = provider.getAddress().split(" ");
            provider.setCityName(addressFields[addressFields.length - 2].toUpperCase());
            boolean isPostCode = Pattern.matches("[0-9]+", addressFields[addressFields.length - 3]);
            if (isPostCode) {
                provider.setCountryName(addressFields[addressFields.length - 4]);
            } else {
                provider.setCountryName(addressFields[addressFields.length - 3]);
            }

            if(requestString.contains("CONTRACTEDHEALTH")){
                provider.setOrganizationType(organizationType);
            }else{
                provider.setOrganizationType("Eczaneler");
            }
        });
    }

    private void mapSpecialists(List<AllianzHealthCareProvider> allianzHealthCareProviders) {
        allianzHealthCareProviders.forEach(provider -> {

            if(provider.getInstitutes() != null && !provider.getInstitutes().isEmpty()){

                Institute institute = provider.getInstitutes().get(0);

                provider.setAddress(institute.getAddress());

                String[] addressFields = provider.getAddress().split(" ");

                provider.setCityName(addressFields[addressFields.length - 2].toUpperCase());

                boolean isPostCode = Pattern.matches("[0-9]+", addressFields[addressFields.length - 3]);
                if (isPostCode) {
                    provider.setCountryName(addressFields[addressFields.length - 4]);
                } else {
                    provider.setCountryName(addressFields[addressFields.length - 3]);
                }

                provider.setOrganizationType("Doktorlar");

                provider.setOrganizationName(institute.getInstituteName());
                provider.setPhone(institute.getPhone());
            }

        });
    }

    public List<HealthCareProvider> convertAllianzResponseToHealthCareProviderList(List<AllianzHealthCareProvider> allianzHealthCareProviders){

        List<HealthCareProvider> healthCareProviders = new ArrayList<>();

        allianzHealthCareProviders.forEach(provider -> {

            HealthCareProvider healthCareProvider = new HealthCareProvider();
            healthCareProvider.setOrganizationName(provider.getOrganizationName());
            healthCareProvider.setQueryType("Anlaşmalı Sağlık Kurumu");
            healthCareProvider.setOrganizationType(provider.getOrganizationType());
            healthCareProvider.setAddress(provider.getAddress());
            healthCareProvider.setCityName(provider.getCityName());
            healthCareProvider.setCountryName(provider.getCountryName());
            healthCareProvider.setIncludedInAllianzInsurance(true);
            if(provider.getPhone() != null)
                healthCareProvider.setPhone(provider.getPhone().replaceAll("[^\\d]", ""));

            healthCareProviders.add(healthCareProvider);
        });

        return healthCareProviders;

    }
}

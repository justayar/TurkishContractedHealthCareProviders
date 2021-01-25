package com.justayar.contractedhealthcareproviders.service;

import com.justayar.contractedhealthcareproviders.bean.HealthCareProvider;
import com.justayar.contractedhealthcareproviders.api.anadoluinsurance.AnadoluInsuranceHttpApi;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AnadoluInsuranceHealthcareProviderService {

    private static final String OSS_NETWORK_CODE = "s";
    private static final String TSS_INDIVIDUAL_A_NETWORK_CODE = "BT_A";

    private static final String CONTRACTED_HOSPITAL_QUERY_TYPE = "Anlaşmalı Sağlık Kurumu";

    private Logger logger = Logger.getLogger(AnadoluInsuranceHealthcareProviderService.class.getName());

    public List<HealthCareProvider> getAnadoluInsuranceHealthCareProvidersForOSS(List<String> selectedQueries){

        List<HealthCareProvider> organizationProviders = new ArrayList<>();

        Map<String,String> organizationTypes = new HashMap<>();

        if(selectedQueries.contains("hospital")){
            organizationTypes.put("Hastane","87C494B13B3300EAE053AC190385CBE2");
        }

        if(selectedQueries.contains("medicalCenter")){
            organizationTypes.put("Tıp Merkezi","87C494B13B3500EAE053AC190385CBE2");
        }

        if(selectedQueries.contains("medicine")){
            organizationTypes.put("Eczaneler","87C494B13B3400EAE053AC190385CBE2");
        }

        if(selectedQueries.contains("doctors")){
            organizationTypes.put("Doktorlar","87C494B13B3600EAE053AC190385CBE2");
        }

        organizationTypes.forEach((organizationTypeName,organizationKey) -> {
            System.out.println("ÖSS "+ organizationTypeName+" anlaşmalı kurumları çekiliyor");
            organizationProviders.addAll(getProviders(organizationTypeName,organizationKey,OSS_NETWORK_CODE,CONTRACTED_HOSPITAL_QUERY_TYPE));
        });

        return organizationProviders.stream()
                .filter(distinctByKey(provider -> Arrays.asList(provider.getOrganizationName(), provider.getCityName(), provider.getCountryName())))
                .collect(toList());

    }

    public List<HealthCareProvider> getAnadoluInsuranceHealthCareProvidersforTSS(List<String> selectedQueries){

        List<HealthCareProvider> organizationProviders = new ArrayList<>();

        Map<String,String> organizationTypes = new HashMap<>();

        if(selectedQueries.contains("medicalCenter")) {
            organizationTypes.put("Tıp Merkezi", "87C494B13B3500EAE053AC190385CBE2");
        }

        if(selectedQueries.contains("hospital")) {
            organizationTypes.put("Hastane", "87C494B13B3300EAE053AC190385CBE2");
        }


        organizationTypes.forEach((organizationTypeName,organizationKey) -> {
            System.out.println("TSS "+ organizationTypeName+" Bireysel A Network anlaşmalı kurumları çekiliyor");
            organizationProviders.addAll(getProviders(organizationTypeName,organizationKey,TSS_INDIVIDUAL_A_NETWORK_CODE,CONTRACTED_HOSPITAL_QUERY_TYPE));

        });

        return organizationProviders.stream()
                .filter(distinctByKey(provider -> Arrays.asList(provider.getOrganizationName(), provider.getCityName(), provider.getCountryName())))
                .collect(toList());


    }

    private List<HealthCareProvider> getProviders(String organizationTypeName,String organizationKey,String networkCode,String queryType){
        AnadoluInsuranceHttpApi anadoluInsuranceHttpApi = new AnadoluInsuranceHttpApi();
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("methodname", "asksonucgetirmapping"));
        urlParameters.add(new BasicNameValuePair("SORGULAMA_TIPI", queryType));

        if(!organizationTypeName.equalsIgnoreCase("Eczaneler"))
            urlParameters.add(new BasicNameValuePair("NETWORK_CODE", networkCode));

        urlParameters.add(new BasicNameValuePair("ORGANIZATION_TYPE", organizationKey));
        urlParameters.add(new BasicNameValuePair("COUNTRY_CODE", "05"));

        if(networkCode.equalsIgnoreCase("Tüp Bebek"))
            urlParameters.add(new BasicNameValuePair("CONTRACT_TYPE_DESC", "Tss Tüp Bebek"));


        List<HealthCareProvider> turkeyHealthCareProviders = anadoluInsuranceHttpApi.getPrivateHealthInsuranceCareProviders(urlParameters);
        turkeyHealthCareProviders.forEach(healthCareProvider -> {
            healthCareProvider.setOrganizationType(organizationTypeName);
            healthCareProvider.setQueryType(queryType);
            healthCareProvider.setIncludedInAnadoluInsurance(true);
            if(healthCareProvider.getPhone() != null)
                healthCareProvider.setPhone(healthCareProvider.getPhone().replaceAll("[^\\d]", ""));

        });


        urlParameters.remove(urlParameters.size()-1);
        urlParameters.add(new BasicNameValuePair("COUNTRY_CODE", "06"));

        List<HealthCareProvider> cyprusHealthCareProviders = anadoluInsuranceHttpApi.getPrivateHealthInsuranceCareProviders(urlParameters);
        cyprusHealthCareProviders.forEach(healthCareProvider ->{
            healthCareProvider.setOrganizationType(organizationTypeName);
            healthCareProvider.setQueryType(queryType);
            healthCareProvider.setIncludedInAnadoluInsurance(true);
            if(healthCareProvider.getPhone() != null)
                healthCareProvider.setPhone(healthCareProvider.getPhone().replaceAll("[^\\d]", ""));

        });

        return Stream.concat(turkeyHealthCareProviders.stream(), cyprusHealthCareProviders.stream())
                .collect(toList());
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}

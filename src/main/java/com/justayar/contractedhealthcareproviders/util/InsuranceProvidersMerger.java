package com.justayar.contractedhealthcareproviders.util;

import com.justayar.contractedhealthcareproviders.bean.HealthCareProvider;

import java.util.ArrayList;
import java.util.List;

public class InsuranceProvidersMerger {

    public List<HealthCareProvider> mergeAnadoluWithAllianz(List<HealthCareProvider> anadoluHealthCareProviders,
                                   List<HealthCareProvider> allianzHealthCareProviders){

        List<HealthCareProvider> healthCareProviders = new ArrayList<>(anadoluHealthCareProviders);

        allianzHealthCareProviders.forEach(healthCareProvider -> {

            if(healthCareProviders.contains(healthCareProvider)){
                int objectIndex = healthCareProviders.indexOf(healthCareProvider);
                healthCareProviders.get(objectIndex).setIncludedInAllianzInsurance(true);
            }else{
                healthCareProviders.add(healthCareProvider);
            }
        });

        return healthCareProviders;
    }

    public List<HealthCareProvider> mergeAllWithAxa(List<HealthCareProvider> allHealthCareProviders,
                                                            List<HealthCareProvider> axaHealthCareProviders){

        List<HealthCareProvider> healthCareProviders = new ArrayList<>(allHealthCareProviders);

        axaHealthCareProviders.forEach(healthCareProvider -> {

            if(healthCareProviders.contains(healthCareProvider)){
                int objectIndex = healthCareProviders.indexOf(healthCareProvider);
                healthCareProviders.get(objectIndex).setIncludedInAxaInsurance(true);
            }else{
                healthCareProviders.add(healthCareProvider);
            }
        });

        return healthCareProviders;
    }
}

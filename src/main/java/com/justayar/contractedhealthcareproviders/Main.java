package com.justayar.contractedhealthcareproviders;

import com.justayar.contractedhealthcareproviders.bean.HealthCareProvider;
import com.justayar.contractedhealthcareproviders.service.AllianzInsuranceHealthCareProviderService;
import com.justayar.contractedhealthcareproviders.service.AnadoluInsuranceHealthcareProviderService;
import com.justayar.contractedhealthcareproviders.service.AxaInsuranceHealthCareProviderService;
import com.justayar.contractedhealthcareproviders.util.ExcelExporter;
import com.justayar.contractedhealthcareproviders.util.InsuranceProvidersMerger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    
    private static final String ALLIANZ_COMPANY_IDENTIFIER = "allianz";
    private static final String ANADOLU_COMPANY_IDENTIFIER = "anadolu";
    private static final String DELIMITER_STRING_FOR_LOOP="-----------------------------------------------------";

    public static void main(String args[]){

        Font font = new Font("Courier", Font.BOLD,12);


        JFrame frame = new JFrame("Anlaşmalı Kurum Sorgulama Ekranı");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3,2));
        Border padding = BorderFactory.createEmptyBorder(10, 20, 10, 20);

        mainPanel.setBorder(padding);

        JPanel companyPanel = new JPanel();
        companyPanel.setLayout(new GridLayout(1,4));


        JLabel company = new JLabel("Sorgulamak istenilen şirketler:");
        company.setFont(font);
        JCheckBox allianz = new JCheckBox("Allianz Sigorta",true);
        JCheckBox anadolu = new JCheckBox("Anadolu Sigorta", true);
        JCheckBox axa = new JCheckBox("Axa Sigorta", true);

        companyPanel.add(company);
        companyPanel.add(allianz);
        companyPanel.add(anadolu);
        companyPanel.add(axa);

        JPanel policyPanel = new JPanel();
        policyPanel.setLayout(new GridLayout(1,3));


        JLabel policyType = new JLabel("Poliçe Tipi:");
        policyType.setFont(font);


        JCheckBox tss = new JCheckBox("TSS", true);
        JCheckBox oss = new JCheckBox("ÖSS", true);

        policyPanel.add(policyType);
        policyPanel.add(tss);
        policyPanel.add(oss);

        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new GridLayout(1,5));


        JLabel queryType = new JLabel("Kurum tipi:");
        queryType.setFont(font);

        JCheckBox hospital = new JCheckBox("Hastane",true);
        JCheckBox universityHospital = new JCheckBox("Üniversite Hastanesi", true);
        JCheckBox medicalCenter = new JCheckBox("Tıp Merkezi", true);
        JCheckBox pyhsioCenter = new JCheckBox("Fizik Tedavi", true);
        JCheckBox medicine = new JCheckBox("Eczaneler", true);
        JCheckBox doctors = new JCheckBox("Uzman Hekimler", true);



        queryPanel.add(queryType);
        queryPanel.add(hospital);
        queryPanel.add(universityHospital);
        queryPanel.add(medicalCenter);
        queryPanel.add(pyhsioCenter);
        queryPanel.add(medicine);
        queryPanel.add(doctors);


        JButton submit=new JButton("Sorgula");
        submit.addActionListener(e -> {
            try {
                List<String> selectedCompanies = getSelectedCompanies(allianz, anadolu, axa);
                List<String> selectedPolicies = getSelectedPolicies(tss, oss);
                List<String> selectedQueries = getSelectedQueries(hospital,universityHospital,medicalCenter,pyhsioCenter,medicine,doctors);
                runProcess(selectedCompanies,selectedPolicies,selectedQueries);
            } catch (IOException ex) {
                System.out.println("Unexpected error happened. Exception message is: "+ex.getMessage());
            }
        });

        queryPanel.add(submit);
        
        mainPanel.add(companyPanel);
        mainPanel.add(policyPanel);
        mainPanel.add(queryPanel);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static List<String> getSelectedCompanies(JCheckBox allianz,
                                                    JCheckBox anadolu,
                                                    JCheckBox axa){
        List<String> selectedCompanies = new ArrayList<>();
        if(allianz.isSelected()){
            selectedCompanies.add(ALLIANZ_COMPANY_IDENTIFIER);
        }

        if(anadolu.isSelected()){
            selectedCompanies.add(ANADOLU_COMPANY_IDENTIFIER);
        }

        if(axa.isSelected()){
            selectedCompanies.add("axa");
        }

        return selectedCompanies;
    }

    private static List<String> getSelectedPolicies(JCheckBox tss,
                                                     JCheckBox oss){
        List<String> selectedPolicies = new ArrayList<>();
        if(tss.isSelected()){
            selectedPolicies.add("tss");
        }

        if(oss.isSelected()){
            selectedPolicies.add("oss");
        }

        return selectedPolicies;
    }

    private static List<String> getSelectedQueries(JCheckBox hospital,
                                                   JCheckBox universityHospital,
                                                   JCheckBox medicalCenter,
                                                   JCheckBox pyhsioCenter,
                                                   JCheckBox medicine,
                                                   JCheckBox doctors){

        List<String> selectedQueries = new ArrayList<>();
        if(hospital.isSelected()){
            selectedQueries.add("hospital");
        }

        if(universityHospital.isSelected()){
            selectedQueries.add("universityHospital");
        }

        if(medicalCenter.isSelected()){
            selectedQueries.add("medicalCenter");
        }

        if(pyhsioCenter.isSelected()){
            selectedQueries.add("pyhsioCenter");
        }

        if(medicine.isSelected()){
            selectedQueries.add("medicine");
        }

        if(doctors.isSelected()){
            selectedQueries.add("doctors");
        }

        return selectedQueries;
    }

    private static void runProcess(List<String> selectedCompanies,
                                   List<String> selectedPolicies,
                                   List<String> selectedQueries) throws IOException {

        long startTime = System.currentTimeMillis();
        
        ExcelExporter excelExporter = new ExcelExporter();
        List<HealthCareProvider> anadoluInsuranceHealthCareProviders = null;
        List<HealthCareProvider> allianzInsuranceHealthCareProviders = null;
        List<HealthCareProvider> axaHealthCareProviders = null;
        InsuranceProvidersMerger insuranceProvidersMerger = new InsuranceProvidersMerger();


        if (selectedCompanies.contains(ANADOLU_COMPANY_IDENTIFIER) && selectedPolicies.contains("oss")) {

            AnadoluInsuranceHealthcareProviderService anadoluInsuranceHealthcareProviderService = new AnadoluInsuranceHealthcareProviderService();
            System.out.println("Anadolu Sigorta ÖSS Anlaşmalı Kurumlar çekiliyor.");
            anadoluInsuranceHealthCareProviders = anadoluInsuranceHealthcareProviderService.getAnadoluInsuranceHealthCareProvidersForOSS(selectedQueries);
            System.out.println("Anadolu Sigorta ÖSS Anlaşmalı Kurumlar tablosu oluşturuluyor.");
            excelExporter.createReport("Anadolu ÖSS Listesi", anadoluInsuranceHealthCareProviders, false);
        }

        if (selectedCompanies.contains(ALLIANZ_COMPANY_IDENTIFIER) && selectedPolicies.contains("oss")) {

            System.out.println(DELIMITER_STRING_FOR_LOOP);
            AllianzInsuranceHealthCareProviderService allianzInsuranceHealthCareProviderService = new AllianzInsuranceHealthCareProviderService();
            System.out.println("Allianz Sigorta ÖSS Anlaşmalı Kurumlar çekiliyor.");
            allianzInsuranceHealthCareProviders = allianzInsuranceHealthCareProviderService.getAllianzInsuranceHealthCareProvidersForOSS(selectedQueries);
            System.out.println("Allianz ÖSS Anlaşmalı Kurumlar tablosu oluşturuluyor.");
            excelExporter.createReport("Allianz ÖSS Listesi", allianzInsuranceHealthCareProviders, false);
        }

        if (selectedCompanies.contains("axa") && selectedPolicies.contains("oss")) {

            System.out.println(DELIMITER_STRING_FOR_LOOP);
            AxaInsuranceHealthCareProviderService axaInsuranceHealthCareProviderService = new AxaInsuranceHealthCareProviderService();
            System.out.println("Axa Sigorta ÖSS Anlaşmalı Kurumlar çekiliyor.");
            axaHealthCareProviders = axaInsuranceHealthCareProviderService.getAxaInsuranceHealthCareProvidersForOSS(selectedQueries);
            System.out.println("Axa ÖSS Anlaşmalı Kurumlar tablosu oluşturuluyor.");
            excelExporter.createReport("Axa ÖSS Listesi", axaHealthCareProviders, false);

        }

        createOSSCompareReport(selectedCompanies, selectedPolicies, excelExporter, anadoluInsuranceHealthCareProviders, allianzInsuranceHealthCareProviders, axaHealthCareProviders, insuranceProvidersMerger);


        if(selectedCompanies.contains(ANADOLU_COMPANY_IDENTIFIER) && selectedPolicies.contains("tss")) {
            System.out.println("Anadolu Sigorta TSS Anlaşmalı Kurumlar çekiliyor");
            AnadoluInsuranceHealthcareProviderService anadoluInsuranceHealthcareProviderService = new AnadoluInsuranceHealthcareProviderService();
            anadoluInsuranceHealthCareProviders = anadoluInsuranceHealthcareProviderService.getAnadoluInsuranceHealthCareProvidersforTSS(selectedQueries);
            System.out.println("Anadolu Sigorta TSS Anlaşmalı Kurumlar tablosu oluşturuluyor.");
            excelExporter.createReport("Anadolu TSS Listesi", anadoluInsuranceHealthCareProviders, false);

            System.out.println(DELIMITER_STRING_FOR_LOOP);

        }

        if(selectedCompanies.contains(ALLIANZ_COMPANY_IDENTIFIER) && selectedPolicies.contains("tss")) {
            AllianzInsuranceHealthCareProviderService allianzInsuranceHealthCareProviderService = new AllianzInsuranceHealthCareProviderService();
            System.out.println("Allianz TSS Anlaşmalı Kurumlar çekiliyor.");
            allianzInsuranceHealthCareProviders = allianzInsuranceHealthCareProviderService.getAllianzInsuranceHealthCareProvidersForTSS(selectedQueries);
            System.out.println("Allianz TSS Anlaşmalı Kurumlar tablosu oluşturuluyor.");
            excelExporter.createReport("Allianz TSS Listesi", allianzInsuranceHealthCareProviders, false);

            System.out.println(DELIMITER_STRING_FOR_LOOP);

        }

        if(selectedCompanies.contains("axa") && selectedPolicies.contains("tss")) {
            AxaInsuranceHealthCareProviderService axaInsuranceHealthCareProviderService = new AxaInsuranceHealthCareProviderService();
            System.out.println("Axa TSS Anlaşmalı Kurumlar çekiliyor.");
            axaHealthCareProviders = axaInsuranceHealthCareProviderService.getAxaInsuranceHealthCareProvidersForTSS(selectedQueries);
            System.out.println("Axa TSS Anlaşmalı Kurumlar tablosu oluşturuluyor.");
            excelExporter.createReport("Axa TSS Listesi", axaHealthCareProviders, false);
        }

        createTSSCompareReport(selectedCompanies, selectedPolicies, excelExporter, anadoluInsuranceHealthCareProviders, allianzInsuranceHealthCareProviders, axaHealthCareProviders);


        long endTime = System.currentTimeMillis();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(endTime-startTime);


        System.out.println("İşlem "+seconds+" sn sürdü");
    }

    private static void createOSSCompareReport(List<String> selectedCompanies, List<String> selectedPolicies, ExcelExporter excelExporter, List<HealthCareProvider> anadoluInsuranceHealthCareProviders, List<HealthCareProvider> allianzInsuranceHealthCareProviders, List<HealthCareProvider> axaHealthCareProviders, InsuranceProvidersMerger insuranceProvidersMerger) throws IOException {
        List<HealthCareProvider> healthCareProviders;
        if (selectedPolicies.contains("oss") && selectedCompanies.size() > 1){
            System.out.println();
            System.out.println("ÖSS Karşılaştırma Tablosu oluşturuluyor.");
            healthCareProviders = insuranceProvidersMerger.mergeAnadoluWithAllianz(anadoluInsuranceHealthCareProviders, allianzInsuranceHealthCareProviders);
            healthCareProviders = insuranceProvidersMerger.mergeAllWithAxa(healthCareProviders, axaHealthCareProviders);
            String ossReport = excelExporter.createReport("ÖSS Karşılaştırma Tablosu", healthCareProviders, true);
            System.out.println("ÖSS Karşılaştırma Tablosu Oluşturuldu. Dosya İsmi: "+ossReport);
            System.out.println(DELIMITER_STRING_FOR_LOOP);
        }
    }

    private static void createTSSCompareReport(List<String> selectedCompanies, List<String> selectedPolicies, ExcelExporter excelExporter, List<HealthCareProvider> anadoluInsuranceHealthCareProviders, List<HealthCareProvider> allianzInsuranceHealthCareProviders, List<HealthCareProvider> axaHealthCareProviders) throws IOException {
        InsuranceProvidersMerger insuranceProvidersMerger;
        List<HealthCareProvider> healthCareProviders;
        if (selectedPolicies.contains("tss") && selectedCompanies.size() > 1) {

            System.out.println();
            System.out.println("TSS Karşılaştırma Tablosu oluşturuluyor.");

            insuranceProvidersMerger = new InsuranceProvidersMerger();
            healthCareProviders = insuranceProvidersMerger.mergeAnadoluWithAllianz(anadoluInsuranceHealthCareProviders, allianzInsuranceHealthCareProviders);
            healthCareProviders = insuranceProvidersMerger.mergeAllWithAxa(healthCareProviders, axaHealthCareProviders);
            String tssReport = excelExporter.createReport("TSS Karşılaştırma Tablosu", healthCareProviders, true);

            System.out.println("TSS Karşılaştırma Tablosu Oluşturuldu. Dosya İsmi: "+tssReport);
        }
    }
}

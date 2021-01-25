package com.justayar.contractedhealthcareproviders.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthCareProvider {

    @JsonProperty("ORGANIZATION_NAME")
    private String organizationName;
    @JsonProperty("CITY_NAME")
    private String cityName;
    @JsonProperty("COUNTY_NAME")
    private String countryName;
    @JsonProperty("ADDRESS")
    private String address;
    @JsonProperty("PHONE")
    private String phone;
    private String organizationType;
    private boolean isIncludedInAnadoluInsurance;
    private boolean isIncludedInAllianzInsurance;
    private boolean isIncludedInAxaInsurance;
    private String queryType;


    private String neighborhood;
    private String street;


    public HealthCareProvider() {
    }

    public HealthCareProvider(String organizationName, String cityName, String countryName, String address, String phone) {
        this.organizationName = organizationName;
        this.cityName = cityName;
        this.countryName = countryName;
        this.address = address;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HealthCareProvider)) {
            //implicit null check
            return false;
        }

        HealthCareProvider comparedObj = (HealthCareProvider) o;
        String addressNumber = getAddressFromText();

        String comparedAddressNumber = "comparedNumber";

        if (comparedObj.address != null) {
            int addNumIndex = comparedObj.address.toUpperCase().indexOf("NO:");
            if (addNumIndex < 0) {
                addNumIndex = comparedObj.address.toUpperCase().indexOf("NO.");
            }
            if (addNumIndex > -1) {
                boolean isNumberDetected = false;
                comparedAddressNumber = extractNumberFromText(addNumIndex, isNumberDetected, comparedObj.address);
            }
        }

        return (this.organizationName.equalsIgnoreCase(comparedObj.organizationName) &&
                this.cityName.equalsIgnoreCase(comparedObj.cityName) &&
                this.countryName.equalsIgnoreCase(comparedObj.countryName)) ||
                (this.phone != null && comparedObj.phone != null && this.phone.equalsIgnoreCase(comparedObj.phone)) ||
                (this.cityName.equalsIgnoreCase(comparedObj.cityName) &&
                        this.countryName.equalsIgnoreCase(comparedObj.countryName) && addressNumber.equalsIgnoreCase(comparedAddressNumber));

    }

    private String getAddressFromText() {
        String addressNumber = "addressNumber";
        if (this.address != null) {
            int currAddNumIndex = this.address.toUpperCase().indexOf("NO:");
            if (currAddNumIndex < 0) {
                currAddNumIndex = this.address.toUpperCase().indexOf("NO.");
            }
            if (currAddNumIndex > -1) {
                boolean isNumberDetected = false;
                addressNumber = extractNumberFromText(currAddNumIndex, isNumberDetected, this.address);
            }
        }
        return addressNumber;
    }

    private String extractNumberFromText(int index, boolean isNumberDetected, String address) {
        StringBuilder number = new StringBuilder();
        boolean slashDetected = false;
        for (char c : address.substring(index).toCharArray()) {
            if (Character.isDigit(c) || c == '/' || (slashDetected && c != ' ')) {
                number.append(c);
                if (c == '/') {
                    slashDetected = true;
                }

                if (!isNumberDetected) {
                    isNumberDetected = true;
                }
            } else {
                if (isNumberDetected) {
                    break;
                }
            }
        }
        return number.toString();
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public boolean isIncludedInAnadoluInsurance() {
        return isIncludedInAnadoluInsurance;
    }

    public void setIncludedInAnadoluInsurance(boolean includedInAnadoluInsurance) {
        isIncludedInAnadoluInsurance = includedInAnadoluInsurance;
    }

    public boolean isIncludedInAllianzInsurance() {
        return isIncludedInAllianzInsurance;
    }

    public void setIncludedInAllianzInsurance(boolean includedInAllianzInsurance) {
        isIncludedInAllianzInsurance = includedInAllianzInsurance;
    }

    public boolean isIncludedInAxaInsurance() {
        return isIncludedInAxaInsurance;
    }

    public void setIncludedInAxaInsurance(boolean includedInAxaInsurance) {
        isIncludedInAxaInsurance = includedInAxaInsurance;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}

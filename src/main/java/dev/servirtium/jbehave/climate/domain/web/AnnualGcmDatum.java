package dev.servirtium.jbehave.climate.domain.web;

public class AnnualGcmDatum {
    String gcm;
    String variable;
    int fromYear;
    int toYear;
    AnnualData annualData;

    public double getAnnualData() {
        return annualData.getValue();
    }
}

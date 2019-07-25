package com.paulhammant.climatedata;

import com.paulhammant.climatedata.domain.web.AnnualData;
import com.paulhammant.climatedata.domain.web.AnnualGcmDatum;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ClimateApi {

    public static String DEFAULT_CLIMATE_API_SITE = "http://climatedataapi.worldbank.org";

    private final String site;
    private final XStream xStream;

    public ClimateApi(String site) {
        this.site = site;
        xStream = new XStream();
        xStream.alias("domain.web.AnnualGcmDatum", AnnualGcmDatum.class);
        xStream.aliasField("double", AnnualData.class, "doubleVal");

    }

    public double getAveAnnualRainfall(final int fromCCYY, final int toCCYY, final String countryISO) {
        String connection = site + "/climateweb/rest/v1/country/annualavg/pr/" + fromCCYY + "/" + toCCYY + "/" + countryISO + ".xml";
        InputStream input = null;
        byte[] b = new byte[0];
        try {
            URL url = new URL(connection);
            input = url.openStream();
            String xml =  new String(input.readAllBytes());
            System.out.println(xml);
            if (xml.contains("Invalid country code. Three letters are required")) {
                throw new UnsupportedOperationException(countryISO + " not recognized by climateweb");
            }
            List<AnnualGcmDatum> bar = (List<AnnualGcmDatum>) xStream.fromXML(xml);
            if (bar.size() == 0) {
                throw new UnsupportedOperationException("date range " + fromCCYY + "-" + toCCYY + " not supported");
            }
            double ave = 0;
            for (AnnualGcmDatum annualGcmDatum : bar) {
                ave = ave + annualGcmDatum.annualData.doubleVal;
            }
            return ave / bar.size();
        } catch (IOException e) {
            throw new UnsupportedOperationException("IOException during operation: " + e.getMessage(), e);
        }

    }
}

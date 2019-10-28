package com.paulhammant.climatedata;

import com.paulhammant.climatedata.domain.web.AnnualData;
import com.paulhammant.climatedata.domain.web.AnnualGcmDatum;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    public double getAveAnnualRainfall(final int fromCCYY, final int toCCYY, final String... countryISOs) {

        double total = 0;

        for (String countryISO : countryISOs) {
            String connection = site + "/climateweb/rest/v1/country/annualavg/pr/" + fromCCYY + "/" + toCCYY + "/" + countryISO + ".xml";
            InputStream input = null;
            byte[] b = new byte[0];
            try {
                URL url = new URL(connection);
                input = url.openStream();
                String xml = new String(input.readAllBytes());
                System.out.println(xml);
                if (xml.contains("Invalid country code. Three letters are required")) {
                    throw new UnsupportedOperationException(countryISO + " not recognized by climateweb");
                }
                List<AnnualGcmDatum> bar = (List<AnnualGcmDatum>) xStream.fromXML(xml);
                if (bar.size() == 0) {
                    throw new UnsupportedOperationException("date range " + fromCCYY + "-" + toCCYY + " not supported");
                }
                double sum = 0;
                for (AnnualGcmDatum annualGcmDatum : bar) {
                    sum = sum + annualGcmDatum.annualData.doubleVal;
                }
                total += (sum / bar.size());
            } catch (IOException e) {
                throw new UnsupportedOperationException("IOException during operation: " + e.getMessage(), e);
            }
        }

        // Average of N averages?  OK, look past that!
        return total / countryISOs.length;

    }
}

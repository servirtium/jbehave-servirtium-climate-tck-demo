package dev.servirtium.jbehave.climate;

import dev.servirtium.jbehave.climate.domain.web.AnnualData;
import dev.servirtium.jbehave.climate.domain.web.AnnualGcmDatum;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
        XStream.setupDefaultSecurity(xStream); // to be removed after 1.5
        xStream.allowTypesByWildcard(new String[] {
                "dev.servirtium.**"
        });
    }

    public double getAveAnnualRainfall(final int fromCCYY, final int toCCYY, final String... countryISOs) {

        double total = 0;

        for (String countryISO : countryISOs) {
            String connection = site + "/climateweb/rest/v1/country/annualavg/pr/" + fromCCYY + "/" + toCCYY + "/" + countryISO + ".xml";
            InputStream input = null;
            byte[] b = new byte[0];
            try {
                URLConnection conn = new URL(connection).openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                input = conn.getInputStream();
                String xml = new String(input.readAllBytes());
                if (xml.contains("Invalid country code. Three letters are required")) {
                    throw new CountryISOwrong(countryISO + " not recognized by climateweb");
                }
                List<AnnualGcmDatum> bar = (List<AnnualGcmDatum>) xStream.fromXML(xml);
                if (bar.size() == 0) {
                    throw new BadDateRange("date range " + fromCCYY + "-" + toCCYY + " not supported");
                }
                double sum = 0;
                for (AnnualGcmDatum annualGcmDatum : bar) {
                    sum = sum + annualGcmDatum.annualData.doubleVal;
                }
                total += (sum / bar.size());
            } catch (IOException e) {
                throw new UnsupportedOperationException(e.getClass().getName() + " during operation, message: " + e.getMessage(), e);
            }
        }

        // Average of N averages?  OK, look past that!
        return total / countryISOs.length;

    }
    static class BadDateRange extends UnsupportedOperationException {
        public BadDateRange(String message) {
            super(message);
        }
    }
    static class CountryISOwrong extends UnsupportedOperationException {
        public CountryISOwrong(String message) {
            super(message);
        }
    }
}

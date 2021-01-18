package dev.servirtium.jbehave.climate;

import com.thoughtworks.xstream.XStream;
import dev.servirtium.jbehave.climate.domain.web.AnnualData;
import dev.servirtium.jbehave.climate.domain.web.AnnualGcmDatum;
import org.http4k.client.JavaHttpClient;
import org.http4k.core.Method;
import org.http4k.core.Request;
import org.http4k.core.Uri;

import java.io.IOException;
import java.util.List;

public class ClimateApi {

    private final String apiURL;
    private final XStream xstream;

    public ClimateApi(String apiURL) {
        this.apiURL = apiURL;
        xstream = configureXStream();
    }

    private XStream configureXStream() {
        XStream xstream = new XStream();
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[] { "dev.servirtium.**" });
        xstream.alias("domain.web.AnnualGcmDatum", AnnualGcmDatum.class);
        xstream.aliasField("double", AnnualData.class, "value");
        return xstream;
    }

    public double getAverageRainfall(final int fromYear, final int toYear, final String... countryISOs) {

        double total = 0;

        for (String code : countryISOs) {
            try {
                List<AnnualGcmDatum> data = getAnnualData(fromYear, toYear, code);
                if (data.size() == 0) {
                    throw new BadDateRange("date range " + fromYear + "-" + toYear + " not supported");
                }
                double sum = 0;
                for (AnnualGcmDatum datum : data) {
                    sum = sum + datum.getAnnualData();
                }
                total += (sum / data.size());
            } catch (IOException e) {
                throw new UnsupportedOperationException(e.getClass().getName() + " during operation, message: " + e.getMessage(), e);
            }
        }

        // Average of N averages?  OK, look past that!
        return total / countryISOs.length;

    }

    private List<AnnualGcmDatum> getAnnualData(int fromYear, int toYear, String code) throws IOException {
        return (List<AnnualGcmDatum>) xstream.fromXML(climatewebData(fromYear, toYear, code));
    }

    private String climatewebData(int fromYear, int toYear, String code) throws IOException {
        String url = apiURL + "/climateweb/rest/v1/country/annualavg/pr/" + fromYear + "/" + toYear + "/" + code + ".xml";
        String xml = new JavaHttpClient().invoke(Request.create(Method.GET, Uri.of(url))).bodyString();

        // World Bank's service communicates back some problems in a 200 response,
        // but with a payload that indicated the problem.
        if (xml.contains("Invalid country code. Three letters are required")) {
            throw new InvalidCountryISO(code + " not recognized by climateweb");
        }
        return xml;
    }

    static class BadDateRange extends UnsupportedOperationException {
        public BadDateRange(String message) {
            super(message);
        }
    }

    static class InvalidCountryISO extends UnsupportedOperationException {
        public InvalidCountryISO(String message) {
            super(message);
        }
    }
}

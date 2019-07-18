package com.paulhammant.climatedata;


import com.paulhammant.servirtium.InteractionMonitor;
import org.junit.Before;
import org.junit.Test;

import static com.paulhammant.climatedata.ClimateApi.CLIMATE_API_SITE;
import static java.lang.Float.NaN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ClimateApiTests {

    private ClimateApi climateApi;
    private InteractionMonitor monitor = new InteractionMonitor.NullObject();

    void setSite(String site) {
        climateApi = new ClimateApi(site);
    }

    void setInteractionMonitor(InteractionMonitor monitor) {
        this.monitor = monitor;
    }

    /*
     See overrides of this method in RecordingClimateApiTests and PlaybackClimateApiTests
     */
    @Before
    public void startup() throws Exception {

        System.out.println("****************");
        System.out.println("DIRECT MODE");
        System.out.println(" - NO SERVIRTIUM");
        System.out.println("****************");

        setSite(CLIMATE_API_SITE);
    }

    @Test
    public void averageRainfallForGreatBritainFrom1980to1999Exists() {
        monitor.setScriptFilename("src/test/mocks/averageRainfallForGreatBritainFrom1980to1999Exists.md");

        assertEquals(988.8454972331015,
                climateApi.getAveAnnualRainfall(1980, 1999, "gbr"), 0);
    }

    @Test
    public void averageRainfallForFranceFrom1980to1999Exists() {
        monitor.setScriptFilename("src/test/mocks/averageRainfallForFranceFrom1980to1999Exists.md");

        assertEquals(913.7986955122727,
                climateApi.getAveAnnualRainfall(1980, 1999, "fra"), 0);
    }

    @Test
    public void averageRainfallForEgyptFrom1980to1999Exists() {
        monitor.setScriptFilename("src/test/mocks/averageRainfallForEgyptFrom1980to1999Exists.md");

        assertEquals(54.58587712129825,
                climateApi.getAveAnnualRainfall(1980, 1999, "egy"), 0);
    }

    @Test
    public void averageRainfallForGreatBritainFrom1985to1995DoesNotExist() {
        monitor.setScriptFilename("src/test/mocks/averageRainfallForGreatBritainFrom1985to1995DoesNotExist.md");

        // wrong date ranges just return an empty list of data -
        try {
            climateApi.getAveAnnualRainfall(1985, 1995, "gbr");
            fail("should have failed in line above");
        } catch (UnsupportedOperationException e) {
            assertEquals("date range 1985-1995 not supported", e.getMessage());
        }
    }

    @Test
    public void averageRainfallForMiddleEarthFrom1980to1999DoesNotExist() {
        monitor.setScriptFilename("src/test/mocks/averageRainfallForMiddleEarthFrom1980to1999DoesNotExist.md");

        try {
            climateApi.getAveAnnualRainfall(1980, 1999, "mde");
            fail("should have failed in line above");
        } catch (UnsupportedOperationException e) {
            assertEquals("mde not recognized by climateweb", e.getMessage());
        }
    }

}

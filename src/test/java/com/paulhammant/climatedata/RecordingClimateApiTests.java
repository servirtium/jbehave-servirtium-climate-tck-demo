package com.paulhammant.climatedata;

import com.paulhammant.servirtium.*;
import com.paulhammant.servirtium.jetty.JettyServirtiumServer;
import org.junit.After;

public class RecordingClimateApiTests extends ClimateApiTests {

    private ServirtiumServer servirtium;
    private MarkdownRecorder recorder;

    @Override
    public void startup() throws Exception {

        System.out.println("**********************");
        System.out.println("SERVIRTIUM RECORD MODE");
        System.out.println("**********************");


        SimpleInteractionManipulations manipulations = new SimpleInteractionManipulations(
                "http://localhost:61417", "http://climatedataapi.worldbank.org")
                .withHeaderPrefixesToRemoveFromServiceResponse("Date:");
        recorder = new MarkdownRecorder(new ServiceInteropViaOkHttp(), manipulations);
        final ServiceMonitor.Console monitor = new ServiceMonitor.Console();
        //final ServiceMonitor.Console monitor = new ServiceMonitor.Default();
        servirtium = new JettyServirtiumServer(monitor, 61417, manipulations, recorder)
                .start();

        // Instead of climatedataapi.worldbank.org direct,
        // hit it via Servirtium so that recordings can happen
        setSite("http://localhost:61417");
        setInteractionMonitor(recorder);

    }

    @After
    public void tearDown() {
        servirtium.stop();
    }

    @Override
    public void averageRainfallForGreatBritainFrom1980to1999Exists() {
        super.averageRainfallForGreatBritainFrom1980to1999Exists();
    }

    @Override
    public void averageRainfallForFranceFrom1980to1999Exists() {
        super.averageRainfallForFranceFrom1980to1999Exists();
    }

    @Override
    public void averageRainfallForEgyptFrom1980to1999Exists() {
        super.averageRainfallForEgyptFrom1980to1999Exists();
    }

    @Override
    public void averageRainfallForGreatBritainFrom1985to1995DoesNotExist() {
        super.averageRainfallForGreatBritainFrom1985to1995DoesNotExist();
    }

    @Override
    public void averageRainfallForMiddleEarthFrom1980to1999DoesNotExist() {
        super.averageRainfallForMiddleEarthFrom1980to1999DoesNotExist();
    }

}

package com.paulhammant.climatedata;

import com.paulhammant.servirtium.*;
import com.paulhammant.servirtium.jetty.JettyServirtiumServer;
import org.junit.After;

public class PlaybackClimateApiTests extends ClimateApiTests {

    private ServirtiumServer servirtium;
    private MarkdownReplayer replayer;

    @Override
    public void startup() throws Exception {

        System.out.println("************************");
        System.out.println("SERVIRTIUM PLAYBACK MODE");
        System.out.println("************************");

        SimpleInteractionManipulations manipulations = new SimpleInteractionManipulations(
                "http://localhost:61417", "http://climatedataapi.worldbank.org")
                .withHeaderPrefixesToRemoveFromServiceResponse("Date:");
        final MarkdownReplayer.ReplayMonitor.Console monitor = new MarkdownReplayer.ReplayMonitor.Console();
        // final MarkdownReplayer.ReplayMonitor.Console monitor = new MarkdownReplayer.ReplayMonitor.Default();
        replayer = new MarkdownReplayer(monitor);
        final ServiceMonitor.Console monitor1 = new ServiceMonitor.Console();
        //final ServiceMonitor.Console monitor1 = new ServiceMonitor.Default();
        servirtium = new JettyServirtiumServer(monitor1, 61417, manipulations, replayer)
                .start();

        // Instead of climatedataapi.worldbank.org direct,
        // use recordings from via Servirtium
        setSite("http://localhost:61417");
        setInteractionMonitor(replayer);
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

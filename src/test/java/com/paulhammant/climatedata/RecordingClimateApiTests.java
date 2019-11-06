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
                .withHeaderPrefixesToRemoveFromServiceResponse("Date:", "X-", "Strict-Transport-Security",
                        "Content-Security-Policy", "Cache-Control", "Secure", "HttpOnly",
                        "Set-Cookie: climatedata.cookie=");

        final ServiceInteropViaOkHttp svcInterop = new ServiceInteropViaOkHttp()
                .withConnectionTimeout(3000)
                .withReadTimeout(3000);
        recorder = new MarkdownRecorder(svcInterop, manipulations)
                .withReplacementInRecording("Set-Cookie: AWSALB=.*",
                        "Set-Cookie: AWSALB=REPLACED-IN-RECORDING; Expires=Thu, 15 Jan 2099 11:11:11 GMT; Path=/")
                .withReplacementInRecording("Set-Cookie: TS0137860d=.*",
                        "Set-Cookie: TS0137860d=ALSO-REPLACED-IN-RECORDING; Path=/")
                .withReplacementInRecording("Set-Cookie: TS01c35ec3=.*",
                        "Set-Cookie: TS01c35ec3=ONE-MORE-REPLACED-IN-RECORDING; Path=/")
                .withReplacementInRecording("Set-Cookie: climatedataapi.cookie=.*",
                        "Set-Cookie: climatedataapi.cookie=1234567899999; Path=/")
                .withReplacementInRecording("Set-Cookie: climatedataapi_ext.cookie=.*",
                        "Set-Cookie: climatedataapi_ext.cookie=9876543211111; Path=/")
                .withReplacementInRecording("User-Agent: .*",
                        "User-Agent: Servirtium-Testing");

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

    @Override
    public void averageRainfallForGreatBritainAndFranceFrom1980to1999CanBeCalculatedFromTwoRequests() {
        super.averageRainfallForGreatBritainAndFranceFrom1980to1999CanBeCalculatedFromTwoRequests();
    }
}

package dev.servirtium.jbehave.climate;

import org.apache.commons.text.WordUtils;
import org.http4k.client.ApacheClient;
import org.http4k.core.Uri;
import org.http4k.server.SunHttp;
import org.http4k.servirtium.ServirtiumServer;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.context.Context;

import javax.validation.constraints.NotNull;
import java.io.File;

import static org.http4k.servirtium.InteractionStorage.Disk;

public class ClimateServirtiumSteps {

    private final Context context;
    private final int port;
    private ServirtiumServer servirtium;

    public ClimateServirtiumSteps(Context context, int port) {
        this.context = context;
        this.port = port;
    }

    @BeforeScenario
    public void beforeScenario() throws Exception {
        String markDownFileName = toCamelCase(context.getCurrentScenario());
        servirtium = ServirtiumServer.Recording(
                markDownFileName,
                Uri.of("http://climatedataapi.worldbank.org"),
                Disk(new File("src/main/resources")),
                new ClimateInteractionOptions(), port, SunHttp::new,
                ApacheClient.create()
        );
        servirtium.start();
    }

    @NotNull
    private String toCamelCase(String name) {
        return WordUtils.capitalizeFully(name).replaceAll(" ", "");
    }

    @AfterScenario
    public void afterScenario() throws Exception {
        servirtium.stop();
    }
}

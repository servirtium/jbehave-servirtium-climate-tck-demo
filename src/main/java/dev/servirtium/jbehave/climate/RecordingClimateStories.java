package dev.servirtium.jbehave.climate;

import org.http4k.core.Uri;
import org.http4k.servirtium.ServirtiumServer;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.ScenarioType;

import javax.validation.constraints.NotNull;
import java.io.File;

import static org.http4k.servirtium.InteractionStorage.Disk;

public class RecordingClimateStories extends ClimateStories {

    @NotNull
    @Override
    protected Object[] getSteps() {
        return new Object[]{
                new ClimateSteps("http://localhost:61417"),
                new RecordingClimateSteps(61417)
        };
    }

    public static class RecordingClimateSteps {

        private final int port;
        private ServirtiumServer servirtium;

        public RecordingClimateSteps(int port) {
            this.port = port;
        }

        @BeforeScenario(uponType = ScenarioType.ANY)
        public void beforeScenario(@Named("sv") String servirtiumMarkdownFileName) {
            servirtium = ServirtiumServer.Recording(
                    servirtiumMarkdownFileName,
                    Uri.of(CLIMATEDATA_URL),
                    Disk(new File(MD_PATH)),
                    new ClimateInteractionOptions(), port
            );
            servirtium.start();
        }

        @AfterScenario(uponType = ScenarioType.ANY)
        public void afterScenario() {
            servirtium.stop();
        }
    }

}



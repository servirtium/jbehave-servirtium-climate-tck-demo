package dev.servirtium.jbehave.climate;

import org.http4k.core.Uri;
import org.http4k.servirtium.ServirtiumServer;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.context.Context;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

import static org.http4k.servirtium.InteractionStorage.Disk;

public class RecordingClimateStories extends ClimateStories {


    @NotNull
    @Override
    protected Object[] getSteps() {
        return new Object[]{
                new ClimateSteps("http://localhost:61417"),
                new RecordingClimateSteps(context, 61417)
        };
    }

    public static class RecordingClimateSteps {

        private final Context context;
        private final int port;
        private ServirtiumServer servirtium;

        public RecordingClimateSteps(Context context, int port) {
            this.context = context;
            this.port = port;
        }

        @BeforeScenario(uponType = ScenarioType.ANY)
        public void beforeScenario() throws Exception {

            List<String> currentSteps = context.getCurrentSteps();
            currentSteps.replaceAll(s -> s.replaceAll(" ", "_"));
            servirtium = ServirtiumServer.Recording(
                    currentSteps.get(currentSteps.size() - 1),
                    Uri.of(CLIMATEDATA_URL),
                    Disk(new File(new File(MD_PATH),
                            String.join(File.separator, currentSteps.subList(0, currentSteps.size()-1)))),
                    new ClimateInteractionOptions(), port
            );
            servirtium.start();
        }

        @AfterScenario(uponType = ScenarioType.ANY)
        public void afterScenario() throws Exception {
            servirtium.stop();
        }
    }

}



package dev.servirtium.jbehave.climate;

import org.http4k.servirtium.ServirtiumServer;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.context.Context;

import javax.validation.constraints.NotNull;
import java.io.File;

import static org.http4k.servirtium.InteractionStorage.Disk;

public class ReplayingClimateStories extends ClimateStories {

    @NotNull
    @Override
    protected Object[] getSteps() {
        return new Object[]{
                new ClimateSteps("http://localhost:61417"),
                new ReplayingClimateSteps(context, 61417)
        };
    }

    public static class ReplayingClimateSteps {

        private final Context context;
        private final int port;
        private ServirtiumServer servirtium;

        public ReplayingClimateSteps(Context context, int port) {
            this.context = context;
            this.port = port;
        }

        @BeforeScenario
        public void beforeScenario() throws Exception {
            servirtium = ServirtiumServer.Replay(
                    String.join(File.separator, context.getCurrentSteps()).replaceAll(" ", "_"),
                    Disk(new File(MD_PATH)),
                    new ClimateInteractionOptions(), port
            );
            servirtium.start();
        }

        @AfterScenario
        public void afterScenario() throws Exception {
            servirtium.stop();
        }
    }

}



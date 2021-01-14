package dev.servirtium.jbehave.climate;

import org.http4k.server.SunHttp;
import org.http4k.servirtium.ServirtiumServer;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.context.Context;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static org.http4k.servirtium.InteractionStorage.Disk;

public class PlaybackClimateStories extends ClimateStories {

    @NotNull
    @Override
    protected Object[] getSteps() {
        return new Object[]{
                new ClimateSteps("http://localhost:61417"),
                new BeforeAndAfterSteps(context)
        };
    }

    public static class BeforeAndAfterSteps {

        private final Context context;
        private ServirtiumServer servirtium;

        public BeforeAndAfterSteps(Context context) {
            this.context = context;
        }

        @BeforeScenario
        public void beforeScenario() throws Exception {

            // TODO -- context.getCurrentScenario() is null for some reason.
            System.out.println(">>>>" + context.getCurrentScenario());

            String markDownFileName = "todo.md"; //TODO
            servirtium = ServirtiumServer.Replay(
                    markDownFileName,
                    Disk(new File("src/main/resources")),
                    new ClimateInteractionOptions(), 61417, SunHttp::new
            );
            servirtium.start();
        }

        @AfterScenario
        public void afterScenario() throws Exception {
            servirtium.stop();
        }
    }

}



package dev.servirtium.jbehave.climate;

import javax.validation.constraints.NotNull;

public class RecordingClimateStories extends ClimateStories {

    @NotNull
    @Override
    protected Object[] getSteps() {
        return new Object[]{
                new ClimateSteps("http://localhost:61417"),
                new ClimateServirtiumSteps(context, 61417)
        };
    }

}



package dev.servirtium.jbehave.climate;

import javax.validation.constraints.NotNull;

public class ReplayingClimateStories extends ClimateStories {

    @NotNull
    @Override
    protected Object[] getSteps() {
        return new Object[]{
                new ClimateSteps("http://localhost:61417"),
                new ClimateServitiumSteps(context, 61417)
        };
    }

}



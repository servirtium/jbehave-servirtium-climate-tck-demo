package dev.servirtium.jbehave.climate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jbehave.core.context.Context;

public class ClimateContext extends Context {
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.SHORT_PREFIX_STYLE).append("story="+getCurrentStory()).append("scenario="+getCurrentScenario()).build();
    }
}

package dev.servirtium.jbehave.climate;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.context.Context;
import org.jbehave.core.context.ContextView;
import org.jbehave.core.context.JFrameContextView;
import org.jbehave.core.embedder.PropertyBasedEmbedderControls;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.model.TableTransformers;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.ContextOutput;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.reporters.SurefireReporter;
import org.jbehave.core.steps.ContextStepMonitor;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.NullStepMonitor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.ANSI_CONSOLE;

public class ClimateStories extends JUnitStories {

    public static final String CLIMATEDATA_URL = "http://climatedataapi.worldbank.org";
    public static final String MD_PATH = "src/main/resources/md";

    protected final Context context = new ClimateContext();
    private Format contextFormat = new ContextOutput(context);
    private ContextView contextView = new JFrameContextView().sized(640, 120);
    private ContextStepMonitor contextStepMonitor = new ContextStepMonitor(context, contextView, new NullStepMonitor());

    public ClimateStories() {
        configuredEmbedder().embedderControls().doGenerateViewAfterStories(true).doIgnoreFailureInStories(false)
                .doIgnoreFailureInView(true).doVerboseFailures(true).useThreads(1).useStoryTimeouts("60");
        configuredEmbedder().useEmbedderControls(new PropertyBasedEmbedderControls());
        configuredEmbedder().useMetaFilters(asList("-skip"));
    }

    @Override
    public Configuration configuration() {
        // avoid re-instantiating configuration for the steps factory
        // alternative use #useConfiguration() in the constructor
        if ( super.hasConfiguration() ){
            return super.configuration();
        }
        Class<? extends Embeddable> embeddableClass = this.getClass();
        LoadFromClasspath resourceLoader = new LoadFromClasspath(embeddableClass);
        TableTransformers tableTransformers = new TableTransformers();
        ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(resourceLoader, tableTransformers);
        SurefireReporter.Options options = new SurefireReporter.Options().useReportName("surefire")
                .withNamingStrategy(new SurefireReporter.BreadcrumbNamingStrategy()).doReportByStory(true);
        SurefireReporter surefireReporter = new SurefireReporter(embeddableClass, options);
        return new MostUsefulConfiguration()
                .useStepMonitor(contextStepMonitor)
                .useStoryLoader(resourceLoader)
                .useStoryParser(new RegexStoryParser(examplesTableFactory))
                .useStoryReporterBuilder(
                        new StoryReporterBuilder()
                                .withCodeLocation(codeLocationFromClass(embeddableClass))
                                .withFormats(contextFormat, ANSI_CONSOLE)
                                .withFailureTrace(true)
                                .withFailureTraceCompression(true)
                                .withSurefireReporter(surefireReporter))
                .useTableTransformers(tableTransformers)
                .useCompositePaths(new HashSet<>(findPaths("**/*.steps", null)));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), getSteps());
    }

    @NotNull
    protected Object[] getSteps() {
        return new Object[] { new ClimateSteps("http://climatedataapi.worldbank.org") };
    }

    @Override
    protected List<String> storyPaths() {
        String filter = System.getProperty("story.filter", "**/*.story");
        return findPaths(filter, "");
    }

    protected List<String> findPaths(String include, String exclude) {
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), include, exclude);
    }

}

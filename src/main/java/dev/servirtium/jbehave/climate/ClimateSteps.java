package dev.servirtium.jbehave.climate;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClimateSteps {
    private ClimateApi api;
    private String baseURL;
    private double rainfall;
    private Exception exception;

    public ClimateSteps(final String baseURL) {
        this.baseURL = baseURL;
    }

    @Given("World Bank geo data for the world on decade boundaries")
    public void givenWorldBankGeoDataForTheWorldOnDecadeBoundaries() {
        api = new ClimateApi(baseURL);
    }

    @When("rainfall totals sought for $fromYear thru $toYear for $countryCode")
    public void whenRainfallTotalsSoughtBetweenDates(int fromYear, int toYear, String countryCode) {
        try {
            rainfall = api.getAverageRainfall(fromYear, toYear, countryCode.split("\\+"));
        } catch ( Exception e ){
            rainfall = -1;
            exception = e;
        }
    }

    @Then("the total was $total")
    public void thenTotalWas(double total) {
        assertThat(rainfall, equalTo(total));
    }

    @Then("message '$message' is received instead of rainfall")
    public void thenMessageIsReceivedInsteadOfRainfall(String message) {
        assertThat(exception, notNullValue());
        assertThat(this.exception.getMessage(), equalTo(message));
    }
}

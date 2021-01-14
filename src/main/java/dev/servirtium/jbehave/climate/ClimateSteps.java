package dev.servirtium.jbehave.climate;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClimateSteps {
    private ClimateApi api;
    private double rainfall;
    private String message;
    private String apiURL;

    public ClimateSteps(final String apiURL) {
        this.apiURL = apiURL;
    }

    @Given("World Bank geo data for the world on decade boundaries")
    public void givenWorldBankGeoDataForTheWorldOnDecadeBoundaries() {
        api = new ClimateApi(apiURL);
    }

    @When("rainfall totals sought for $from thru $to for $ccy")
    public void whenRainfallTotalsSoughtBetweenDates(int from, int to, String ccy) {
        try {
            rainfall = api.getAverageRainfall(from, to, ccy.split(" and "));
        } catch ( Exception e ){
            message = e.getMessage();
        }
    }

    @Then("the total was $total")
    public void thenTotalWas(double total) {
        assertThat(rainfall, equalTo(total));
    }

    @Then("message '$message' is received instead of rainfall")
    public void thenMessageIsReceivedInsteadOfRainfall(String message) {
        assertThat(this.message, equalTo(message));
    }
}

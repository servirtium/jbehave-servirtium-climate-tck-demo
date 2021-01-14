package dev.servirtium.jbehave.climate;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClimateSteps {
    private double rainfall;

    private ClimateApi api;
    private String message;

    @Given("World Bank geo data for the world on decade boundaries")
    public void givenWorldBankGeoDataForTheWorldOnDecadeBoundaries() {
        api = new ClimateApi("http://climatedataapi.worldbank.org");
    }

    @When("rainfall totals sought for $from thru $to for $ccy")
    public void whenRainfallTotalsSoughtBetweenDates(int from, int to, String ccy) {
        try {
            rainfall = api.getAveAnnualRainfall(from, to, ccy.split(","));
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

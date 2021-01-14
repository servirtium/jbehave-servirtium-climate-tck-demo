package com.paulhammant.climatedata;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Pending;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClimateSteps {
    private double rainfall;

    private ClimateApi api;

    @Given("World Bank geo data for the world on decade boundaries")
    public void givenWorldBankGeoDataForTheWorldOnDecadeBoundaries() {
        api = new ClimateApi("http://climatedataapi.worldbank.org");
    }

    @When("rainfall totals sought for $from thru $to for $ccy")
    public void whenRainfallTotalsSoughtBetweenDates(int from, int to, String ccy) {
        rainfall = api.getAveAnnualRainfall(from, to, ccy);
    }

    @Then("the total was $total")
    public void thenTotalWas(double total) {
        assertThat(rainfall, equalTo(total));
    }
}

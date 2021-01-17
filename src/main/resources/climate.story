Scenario: Checking positive Great Britain
Meta:
@sv averageRainfallForGreatBritainFrom1980to1999Exists

Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for 1980 thru 1999 for gbr
Then the total was 988.8454972331015

Scenario: Checking positive France
Meta:
@sv averageRainfallForFranceFrom1980to1999Exists

Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for 1980 thru 1999 for fra
Then the total was 913.7986955122727

Scenario: Checking positive Egypt
Meta:
@sv averageRainfallForEgyptFrom1980to1999Exists

Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for 1980 thru 1999 for egy
Then the total was 54.58587712129825

Scenario: Checking positive Great Britain and France
Meta:
@sv averageRainfallForGreatBritainAndFranceFrom1980to1999CanBeCalculatedFromTwoRequests

Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for 1980 thru 1999 for gbr+fra
Then the total was 951.3220963726872

Scenario: Check bad date range
Meta:
@sv averageRainfallForGreatBritainFrom1985to1995DoesNotExist

Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for 1985 thru 1995 for gbr
Then message 'date range 1985-1995 not supported' is received instead of rainfall

Scenario: Check bad country code
Meta:
@sv averageRainfallForMiddleEarthFrom1980to1999DoesNotExist

Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for 1980 thru 1999 for mde
Then message 'mde not recognized by climateweb' is received instead of rainfall

Scenario: Checking positive cases return rainfall

Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for <FromYear> thru <ToYear> for <Country>
Then the total was <Total>

Examples:
|Country|FromYear|ToYear|Total|
|gbr    |1980    |1999  |988.8454972331015|
|fra    |1980    |1999  |913.7986955122727|
|egy    |1980    |1999  |54.58587712129825|
|gbr+fra|1980    |1999  |951.3220963726872|

Scenario: Checking negative cases return failure message

Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for 1985 thru 1995 for gbr
Then message 'date range 1985-1995 not supported' is received instead of rainfall
When rainfall totals sought for 1980 thru 1999 for mde
Then message 'mde not recognized by climateweb' is received instead of rainfall

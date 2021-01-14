
Given World Bank geo data for the world on decade boundaries
When rainfall totals sought for <FromYear> thru <ToYear> for <Country>
Then the total was <Total>

Examples:
|Country|FromYear|ToYear|Total|
|gbr    |1980    |1999  |988.8454972331015|
|fra    |1980    |1999  |913.7986955122727|
|egy    |1980    |1999  |54.58587712129825|
|gbr,fra|1980    |1999  |951.3220963726872|

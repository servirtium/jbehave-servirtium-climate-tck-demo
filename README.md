This requires JDK 12 (or above) and Maven 3.6.1+ (or above) to run.

Running the tests against the the World Bank's Climate API (http://climatedataapi.worldbank.org) **direct**:

```
mvn clean install -Pdirect
```

Running the tests against the the World Bank's Climate API (http://climatedataapi.worldbank.org) **indirectly** 
via Servirtium's Man-in-the-Middle recorder server:

```
mvn clean install -Precord
```

^ this makes the recordings shown here: [climate-data-tck/tree/master/src/test/mocks/](https://github.com/paul-hammant/climate-data-tck/tree/master/src/test/mocks)


Running the tests against the the Servirtium's playback server (using recordings from the above):

```
mvn clean install -Pplayback
```




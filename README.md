# Demo of Servirtium Service Virtualization

Servirtium itself: https://github.com/paul-hammant/servirtium

A video that talks through this repo: https://youtu.be/256kAL890GI

## Running it

This requires JDK 12 (or above) and Maven 3.6.1+ (or above) to run.

Running the tests directly against the the World Bank's Climate API (http://climatedataapi.worldbank.org) **direct**:

```
mvn clean install -Pdirect
```

Running the same tests against the the World Bank's Climate API (http://climatedataapi.worldbank.org) **indirectly** 
via Servirtium's Man-in-the-Middle recording server:

```
mvn clean install -Precord
```

^ this makes the recordings shown here: [climate-data-tck/tree/master/src/test/mocks/](https://github.com/paul-hammant/climate-data-tck/tree/master/src/test/mocks)


Running the same tests against the the Servirtium's playback server (using recordings from the above):

```
mvn clean install -Pplayback
```

# How to make this a Technology Compatibiiy Kit:

Make two jobs in your Travis/CircleCI setup:

1. A job that uses tests in playback on a per commit basis. If you team says "we do CI", then this 
job being green confirms that, as always.

2. A job that runs nightly or weekly that attempts to re-record the Servitium markdown, followed b a 
test that fails the job if there are significant differences. This bash script may help do that:

```
difff=$(git diff --unified=0 path/to/module/src/test/mocks/)

# optional ‘sed’ tricks above too if you want

if [[ -z "${difff}" ]]
then
      echo " - No differences versus recorded interactions committed to Git, so that's good"

else
      echo " - There should be no differences versus last TCK recording, but there were - see build log :-("
      echo "**** DIFF ****:${difff}."
      exit 1
fi
```




# Demo of Servirtium Service Virtualization

Servirtium itself: https://github.com/paul-hammant/servirtium

A video that talks through this repo: https://youtu.be/256kAL890GI

## Running it

This requires JDK 12 (or above) and Maven 3.6.1+ (or above) to run.

### Running the tests DIRECTLY against the the World Bank's Climate API 

This will hit the API **direct** at http://climatedataapi.worldbank.org, with 
no Servirtium involved:

```
mvn clean install -Pdirect
```

^ -P is a profile in Maven, and 'direct' is the name of a profile which sets up tests 
with no Servirtium

### Running the tests and RECORDING the the World Bank's Climate API 

This will hit the API **indirect** at http://climatedataapi.worldbank.org) 
via Servirtium's Man-in-the-Middle recording server:

```
mvn clean install -Precord
```

^ a Maven profile of 'record' sets up tests with Servirtium in record mode, recording to
[src/test/mocks/](/servirtium/demo-java-climate-data-tck/tree/master/src/test/mocks) - 
(one recording per test method).

### Running the tests against the the Servirtium's playback server 

Ths used the using recordings from the recording above - straight from the file system (the adjacent mocks/ 
dir under source control):

```
mvn clean install -Pplayback
```

^ a Maven profile of 'playback' sets up tests with Servirtium in playback mode, using recordings 
done previously in [src/test/mocks/](/servirtium/demo-java-climate-data-tck/tree/master/src/test/mocks) - 
(one recording per test method).

### Running direct, playback and record modes together

```
mvn install -PdirectAndPlaybackAndRecord 
```

### Overcoming climateweb's flakiness

Maven's test runner has a retry feature:

```
mvn install -Pdirect -Dsurefire.rerunFailingTestsCount=4
#or
mvn install -Precord -Dsurefire.rerunFailingTestsCount=4
```

Playback is never flaky of course.

### Seeing ClimateAPI's flakiness

Sometimes the World Bank's climate API is a little flaky. Here's what that looks like in a regular 
browser:

![2019-11-17_2242](https://user-images.githubusercontent.com/82182/69015460-dfea4d80-098b-11ea-97b5-dbb75ced9f94.png)

Here's a video of the same flaky experience inside Intellij while trying to run tests:

[![Watch the video](https://user-images.githubusercontent.com/82182/68976194-2ce3ed80-07ed-11ea-8d8b-4340f608751f.png)](https://youtu.be/PEsVkMUH6uQ)

# How to make this a Technology Compatibility Kit:

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




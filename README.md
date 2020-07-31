# gridtime-perf

Performance testing tools for gridtime.

Performance testing tools can be configured to run against any gridtime instance, local or remote.  Uses the the gridtime-rest-client to run through a series of performance benchmark tests for core platform behaviors such as:

* Spinning up new accounts, teams, and organizations
* Spinning up new learning circuits, and talking over talk
* Transitioning learning circuits through the full state machine workflow
* Writing in journals and creating new projects and tasks

## IDE Setup

Application stack is Gradle and Java/Groovy.  To setup the project in Intellij, first run:

`./gradlew idea`

Then open the generated Intellij project file in your IDE.

## Run the Tests

To run the full suite of performance tests and generate reports, run: 

`./gradlew benchmark`


# TDD Pilot Implementation
(TDD, Spring, Java, RDMS)
Implemented a Pilot model in the Aviation app using TDD.

Wrote tests for the service and controller layer based on the requirements provided. After basic 
CRUD functionality was running, improved pilot controller methods such as `getPilotById` to 
explicitly handle exceptions in order to return better error messages. 

Updated the Aircraft model to reference a pilot rather than store a string and wrote a Flyway 
migration to update the table columns. 

TDD helped ensure the Aircraft-related classes were updated properly. Added a findAllByPilot custom
query that leverages JPA's SQL inference to return all the aircraft that a given pilot has flown.
Also added supporting controller and service functionality, writing tests first. Utilized 
query strings to keep url paths short and organized.

# Spring Entity Relationships
(Spring, Java, RDMS)
Discovered that implicitly, the `@ManyToOne` annotation does not support any type of cascading. 
During testing, I found that I had to create a pilot on the database first before I was able to 
create an airframe with that pilot id. Based on a tip from Curt, found out that you can add the 
cascade parameter to the annotation so that JPA/hibernate will automatically create a new pilot 
too from one request. By using `CascadeType.PERSIST`, it limits cascading exclusively to create 
operations to avoid unwanted deletes (deleting an aircraft shouldn't delete all the pilots 
who fly it)

# TDD Flight Hour Ranking w/ Postgres Views and Java Records
(TDD, RDMS, Java, Spring, User Stories, Git)
Extended the aviation app further for a basic user story: "As a pilot, I want to be able to see 
how I rank against my peers in total flight hours."

Updated the Pilot model with a new `flightHours` field, wrote a migration to add the column to 
the table, and edited the tests for the new shape.

Wrote another Flyway migration that added a `view` to the postgres database. The view leverages the 
`Rank()` window function to order the pilots by flight hours. 

Instead of creating a JPA Entity to model the results from the view, implemented a 
`FlightHourRanking` record. Since interacting with views will always be read-only, records are a 
good fit because they reduce boilerplate code, are immutable, and we can avoid the overhead of 
an Entity class. A tradeoff however is that JPA cannot infer queries for the records, so the 
`Query` annotation was used to write a native postgres query in the PilotRepository. The 
controller and service methods were also added (writing tests first).

Built everything on a new feature branch and merged back to main once complete. Committed and 
pushed changes as soon as tests passed.

# Views and Records Part 2 + Dynamic Query Parameters
Reinforced new Spring wisdom by extending the aviation application for another user story: "As a 
pilot, I want to be able to see which airframes are the most popular among my peers."

Wrote a flyway migration to add the `aircraft_popularity` view to the database. Once that was 
added, wrote the related record class and the native query in the AircraftRepository. Created 
tests for the service and controller layer and used them implement working service and 
controller methods.

Extended the feature for an additional requirement: "I must be able to filter the list of 
popular aircraft to the top {x} models."

Added additional tests for the service and controller then added an overloaded native query 
method in the AircraftRepository that uses query params to dynamically adjust the `LIMIT` value.

Built everything on a new feature branch and merged back to main once complete. Committed and
pushed changes as soon as tests passed.

# Custom Response Classes

# PATCH (update pilot flight hours)











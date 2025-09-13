# TDD Pilot Implementation
Implemented a Pilot model in the Aviation app using TDD.

Wrote tests for the service and controller layer based on the requirements provided. After basic 
CRUD functionality was running, improved pilot controller methods such as `getPilotById` to 
explicitly handle exceptions in order to return better error messages. 

Updated the Aircraft model to reference a pilot rather than store a string and a Flyway 
migration was generated to update the table. 

TDD helped ensure the Aircraft-related classes were updated properly. Added a findAllByPilot custom
query that leverages JPA's SQL inference to return all the aircraft that a given pilot has flown.
Also added supporting controller and service functionality, writing tests first. Utilized 
query strings to keep url paths short and organized.

# Spring Entity Relationships
Discovered that implicitly, the `@ManyToOne` annotation does not support any type of cascading. 
During testing, I found that I had to create a pilot on the database first before I was able to 
create an airframe with that pilot id. Based on a tip from Curt, found out that you can add the 
cascade parameter to the annotation so that JPA/hibernate will automatically create a new pilot 
too from one request. By using `CascadeType.PERSIST`, it limits cascading exclusively to create 
operations to avoid unwanted deletes (deleting an aircraft shouldn't delete all the pilots 
who fly it)

# TDD Flight Hour Ranking w/ Postgres Views and Java Records

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










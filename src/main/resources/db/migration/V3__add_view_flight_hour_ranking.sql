CREATE VIEW pilot_hours_ranked AS
SELECT id,
       first_name,
       last_name,
       flight_hours,
       RANK() OVER (ORDER BY flight_hours DESC) AS rank
FROM pilot;
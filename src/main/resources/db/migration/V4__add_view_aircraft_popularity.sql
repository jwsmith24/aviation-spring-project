CREATE VIEW aircraft_popularity AS
    SELECT airframe,
           COUNT(DISTINCT aircraft.pilot_id) AS total_pilots
    FROM aircraft
    GROUP BY airframe;
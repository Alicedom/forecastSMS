-- get key

SELECT
	a.id, a.apikey
FROM
	`apikey` a
WHERE
	a.website = "darksky"
AND a.called_inday < a.max_number
LIMIT 1;

-- update called key

UPDATE apikey a
SET a.called_inday = a.called_inday + 1
WHERE
	a.id = 2;

-- reset called key

UPDATE apikey a
SET a.called_inday = 0;


-- check and reset called
UPDATE apikey a SET a.called_inday = 0 WHERE a.id = 3 AND DATE(a.last_called) < CURDATE() ;
--get apikey
SELECT a.id, a.apikey FROM `apikey` a WHERE a.website = "darksky" AND a.called_inday < a.max_number LIMIT 1;
--update called
UPDATE apikey JOIN ( SELECT a.id FROM `apikey` a WHERE a.website = "darksky" AND a.called_inday < a.max_number LIMIT 1 ) a ON apikey.id = a.id SET called_inday = called_inday + 1, apikey.last_called = NOW()

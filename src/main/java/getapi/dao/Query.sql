-- check and reset called
UPDATE apikey a
SET a.called_inday = 0
WHERE DATE(a.last_called) < CURDATE() AND a.dead = 0;
--get apikey
SELECT
  a.id,
  a.apikey
FROM `apikey` a
WHERE a.website = '" + source + "' AND a.called_inday < a.max_number AND a.dead = 0
LIMIT 1;
--update called
UPDATE apikey
SET called_inday = called_inday + 1, apikey.last_called = NOW()
WHERE apikey.id = 'APIKeyID';

-- get key
SELECT
  a.id,
  a.apikey
FROM `apikey` a
WHERE a.website = "darksky" AND a.called_inday < a.max_number
LIMIT 1;
-- update called key
UPDATE apikey a
SET a.called_inday = a.called_inday + 1
WHERE a.id = 2;
-- reset all called key
UPDATE apikey a
SET a.called_inday = 0;


INSERT INTO game (status)
VALUES (:status)
RETURNING id;

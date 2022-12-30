INSERT INTO box (game_id, opened, winning, picked)
VALUES (:gameId, :opened, :winning, :picked)
RETURNING id;

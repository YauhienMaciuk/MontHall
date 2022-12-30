SELECT id, game_id, opened, winning, picked
FROM box
WHERE game_id = :gameId
  AND picked = TRUE;

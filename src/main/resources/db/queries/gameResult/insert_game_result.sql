INSERT INTO game_result (game_id, picked_box_was_changed, win)
VALUES (:gameId, :pickedBoxWasChanged, :win)
RETURNING id;

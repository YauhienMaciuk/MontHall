SELECT id,
       boxes_number,
       games_number,
       change_origin_choice_win_percentage,
       stick_to_origin_choice_win_percentage
FROM statistic
WHERE boxes_number = :boxes_number
  AND games_number = :games_number

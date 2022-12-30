INSERT INTO statistic (boxes_number,
                       games_number,
                       change_origin_choice_win_percentage,
                       stick_to_origin_choice_win_percentage)
VALUES (:boxes_number,
        :games_number,
        :change_origin_choice_win_percentage,
        :stick_to_origin_choice_win_percentage)
RETURNING id;

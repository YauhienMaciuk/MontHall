package com.hometask.montyhall.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class GameResult {

    Long id;
    Long gameId;
    Boolean pickedBoxWasChanged;
    Boolean win;

}

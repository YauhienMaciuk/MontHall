package com.hometask.montyhall.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Game {

    Long id;
    GameStatus status;
}

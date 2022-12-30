package com.hometask.montyhall.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Box {

    Long id;
    Long gameId;
    Boolean opened;
    Boolean winning;
    Boolean picked;

}

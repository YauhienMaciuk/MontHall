package com.hometask.montyhall.dto;

import javax.validation.constraints.NotNull;

public class ChooseDecision {
    @NotNull(message = "changePickedBox value must not be null")
    private Boolean changePickedBox;

    public Boolean getChangePickedBox() {
        return changePickedBox;
    }

    public void setChangePickedBox(Boolean changePickedBox) {
        this.changePickedBox = changePickedBox;
    }
}

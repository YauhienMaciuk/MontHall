package com.hometask.montyhall.dto;

import com.hometask.montyhall.entity.Box;

public class BoxDto {
    private Long id;
    private Boolean opened;
    private Boolean picked;

    public static BoxDto of(Box box) {
        BoxDto boxDto = new BoxDto();
        boxDto.setId(box.getId());
        boxDto.setOpened(box.getOpened());
        boxDto.setPicked(box.getPicked());
        return boxDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public Boolean getPicked() {
        return picked;
    }

    public void setPicked(Boolean picked) {
        this.picked = picked;
    }
}

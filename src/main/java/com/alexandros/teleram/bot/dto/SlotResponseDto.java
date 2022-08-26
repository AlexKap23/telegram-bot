package com.alexandros.teleram.bot.dto;

import java.util.List;

public class SlotResponseDto extends ResponseDto{

    private List<SlotDto> slots;

    public List<SlotDto> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotDto> slots) {
        this.slots = slots;
    }
}

package com.krushit.dto;

public class DistanceCalculatorDTO {
    private int from;
    private int to;

    public DistanceCalculatorDTO() {
    }

    public DistanceCalculatorDTO(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}

package com.jpm.techtest.trade.data;

public enum Direction {
    B("Buy"),
    S("Sell");

    private String text;

    Direction(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

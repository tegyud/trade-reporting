package com.jpm.techtest.trade.data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

public class Instruction {
    private static final Set<Currency> SUNDAY_WEEK = new HashSet<>(Arrays.asList(
            Currency.getInstance("AED"),
            Currency.getInstance("SAR")
    ));

    private String entity;
    private Direction direction;
    private double agreedFx;
    private Currency currency;
    private LocalDate instructionDate;
    private LocalDate settlementDate;
    private long units;
    private double price;

    public Instruction(String entity, Direction direction, double agreedFx, Currency currency,
                       LocalDate instructionDate, LocalDate settlementDate, long units, double price) {
        this.entity = entity;
        this.direction = direction;
        this.agreedFx = agreedFx;
        this.currency = currency;
        this.instructionDate = instructionDate;
        this.settlementDate = settlementDate;
        this.units = units;
        this.price = price;

        adjustSettlementDate();
    }

    private void adjustSettlementDate() {
        final DayOfWeek dayOfWeek = settlementDate.getDayOfWeek();
        if (SUNDAY_WEEK.contains(currency)) {
            if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY) {
                settlementDate = settlementDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            }
        } else {
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                settlementDate = settlementDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            }
        }
    }

    public String getEntity() {
        return entity;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public Direction getDirection() {
        return direction;
    }

    public double getAmountInUSD() {
        return price * units * agreedFx;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "entity=" + entity +
                ", direction=" + direction +
                ", agreedFx=" + agreedFx +
                ", currency=" + currency +
                ", instructionDate=" + instructionDate +
                ", settlementDate=" + settlementDate +
                ", units=" + units +
                ", price=" + price +
                '}';
    }
}

package com.jpm.techtest.trade.data;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

public class InstructionTest {
    private static final double DELTA = 1e-15;

    @Test
    public void testGetAmountInUSD() throws Exception {
        // given
        Instruction instruction = new Instruction("foo", Direction.B, 0.5, Currency.getInstance("USD"),
                LocalDate.now(), LocalDate.now().plusDays(1), 100, 200);

        // when
        final double amountInUSD = instruction.getAmountInUSD();

        // then
        assertEquals(10000., amountInUSD, DELTA);
    }

    @Test
    public void testAdjustSettlementDateWhenWeekendAED() {
        // given
        Instruction instruction = new Instruction("foo", Direction.B, 0.5, Currency.getInstance("AED"),
                LocalDate.of(2017, 1, 5), LocalDate.of(2017, 1, 7), 100, 200);

        // when
        final LocalDate settlementDate = instruction.getSettlementDate();

        // then
        assertEquals(LocalDate.of(2017, 1, 8), settlementDate);
    }

    @Test
    public void testAdjustSettlementDateWhenWeekendSAR() {
        // given
        Instruction instruction = new Instruction("foo", Direction.B, 0.5, Currency.getInstance("SAR"),
                LocalDate.of(2017, 1, 5), LocalDate.of(2017, 1, 6), 100, 200);

        // when
        final LocalDate settlementDate = instruction.getSettlementDate();

        // then
        assertEquals(LocalDate.of(2017, 1, 8), settlementDate);
    }

    @Test
    public void testNotAdjustSettlementDateOnWeekday() {
        // given
        Instruction instruction = new Instruction("foo", Direction.B, 0.5, Currency.getInstance("USD"),
                LocalDate.of(2017, 1, 5), LocalDate.of(2017, 1, 6), 100, 200);

        // when
        final LocalDate settlementDate = instruction.getSettlementDate();

        // then
        assertEquals(LocalDate.of(2017, 1, 6), settlementDate);
    }
}
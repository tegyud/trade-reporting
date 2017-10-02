package com.jpm.techtest.trade.parser;

import com.jpm.techtest.trade.data.Direction;
import com.jpm.techtest.trade.data.Instruction;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

public class InputParserTest {

    private InputParser inputParser;

    @Before
    public void setUp() {
        inputParser = new InputParser("/test_input.csv");
    }

    @Test
    public void testGetNextInstruction() throws Exception {
        // given
        Instruction expected = new Instruction("foo", Direction.B, 0.5, Currency.getInstance("SAR"),
                LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 3),
                200, 100.25);

        // when
        final Instruction actual = inputParser.getNextInstruction();

        // then
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetCount() throws Exception {
        // when
        for (int i = 0; i < 3; i++) {
            inputParser.getNextInstruction();
        }

        // then
        assertEquals(3, inputParser.getCount());
    }
}
package com.jpm.techtest.trade.parser;

import com.jpm.techtest.trade.data.Direction;
import com.jpm.techtest.trade.data.Instruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

public class InputParser {
    private static final short NUMBER_OF_FIELDS = 8;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final BufferedReader reader;
    private long count = 0;

    public InputParser(String fileName) {
        final InputStream in = getClass().getResourceAsStream(fileName);
        reader = new BufferedReader(new InputStreamReader(in));
    }

    public Instruction getNextInstruction() {
        Instruction instruction = null;
        try {
            if (count == 0) {
                reader.readLine();
            }
            final String line = reader.readLine();
            if (line != null) {
                final String[] fields = line.split(",", NUMBER_OF_FIELDS);
                instruction = new Instruction(
                        fields[0],
                        Direction.valueOf(fields[1]),
                        Double.parseDouble(fields[2]),
                        Currency.getInstance(fields[3]),
                        LocalDate.parse(fields[4], DATE_FORMATTER),
                        LocalDate.parse(fields[5], DATE_FORMATTER),
                        Long.parseLong(fields[6]),
                        Double.parseDouble(fields[7]));
                count++;
            } else {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instruction;
    }

    public long getCount() {
        return count;
    }
}

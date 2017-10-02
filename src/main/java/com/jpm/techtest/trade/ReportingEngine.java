package com.jpm.techtest.trade;

import com.jpm.techtest.trade.data.Instruction;
import com.jpm.techtest.trade.parser.InputParser;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReportingEngine {
    private static final String INPUT_PATH = "/input.csv";

    private final InputParser parser;
    private PriorityQueue<Instruction> executionQueue;
    private LocalDate lastSettlementDate = null;
    private Map<String, Double> dailyOutgoing;
    private Map<String, Double> dailyIncoming;

    public ReportingEngine(InputParser parser) {
        this.parser = parser;
        this.executionQueue = new PriorityQueue<>(Comparator.comparing(Instruction::getSettlementDate));
        this.dailyOutgoing = new HashMap<>();
        this.dailyIncoming = new HashMap<>();
    }

    private void parseInput() {
        Instruction i;
        while ((i = parser.getNextInstruction()) != null) {
            executionQueue.add(i);
        }
    }

    private void printDailyStats() {
        System.out.println(lastSettlementDate);
        System.out.println("\tOutgoing ranking: " + getDailyRank(dailyOutgoing));
        System.out.println("\tSum outgoing amount in USD: " + getDailySum(dailyOutgoing));
        System.out.println("\tIncoming ranking: " + getDailyRank(dailyIncoming));
        System.out.println("\tSum incoming amount in USD: " + getDailySum(dailyIncoming));
    }

    private double getDailySum(Map<String, Double> dailyMap) {
        return dailyMap.values().stream().reduce(0., Double::sum);
    }

    private Map<String, Double> getDailyRank(Map<String, Double> dailyMap) {
        return dailyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private void createReport() {
        System.out.println("-=Daily Report=-");
        while (!executionQueue.isEmpty()) {
            final Instruction nextToSettle = executionQueue.poll();
            final LocalDate currentSettlementDate = nextToSettle.getSettlementDate();
            if (lastSettlementDate != null && currentSettlementDate.isAfter(lastSettlementDate)) {
                printDailyStats();
                dailyOutgoing.clear();
                dailyIncoming.clear();
            }
            final String entity = nextToSettle.getEntity();
            final double amountInUSD = nextToSettle.getAmountInUSD();
            switch (nextToSettle.getDirection()) {
                case B:
                    dailyOutgoing.merge(entity, amountInUSD, Double::sum);
                    break;
                case S:
                    dailyIncoming.merge(entity, amountInUSD, Double::sum);
                    break;
            }
            lastSettlementDate = currentSettlementDate;
        }
        printDailyStats();
    }

    public static void main(String[] args) {
        final InputParser parser = new InputParser(INPUT_PATH);
        ReportingEngine engine = new ReportingEngine(parser);

        engine.parseInput();
        engine.createReport();
    }
}

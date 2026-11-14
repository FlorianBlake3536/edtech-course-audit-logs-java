package edtech.logs;

import java.time.LocalDate;

public final class DeadlineDecisionTest {
    public static void main(String[] args) {
        LocalDate today = LocalDate.of(2026, 10, 23);
        require("send-reminder".equals(DeadlineDecision.forLearner(today, today)));
        require("wait".equals(DeadlineDecision.forLearner(today.plusDays(1), today)));
        System.out.println("deadline decision test passed");
    }

    private static void require(boolean value) {
        if (!value) throw new AssertionError("deadline decision changed");
    }
}

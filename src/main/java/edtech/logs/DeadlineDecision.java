package edtech.logs;

import java.time.LocalDate;

public final class DeadlineDecision {
    private DeadlineDecision() { }

    public static String forLearner(LocalDate deadline, LocalDate today) {
        return deadline.isBefore(today) || deadline.equals(today) ? "send-reminder" : "wait";
    }
}

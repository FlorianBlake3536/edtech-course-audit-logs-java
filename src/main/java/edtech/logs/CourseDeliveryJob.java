package edtech.logs;

import java.time.LocalDate;

public final class CourseDeliveryJob {
    private final InfraiClient client;

    public CourseDeliveryJob(InfraiClient client) { this.client = client; }

    public String deliver(String courseId, String learnerId, LocalDate deadline, LocalDate today)
            throws Exception {
        String decision = DeadlineDecision.forLearner(deadline, today);
        String entry = "[{\"service\":\"course-delivery\",\"message\":\"" + decision
                + "\",\"course_id\":\"" + courseId
                + "\",\"learner_id\":\"" + learnerId + "\",\"decision\":\"" + decision
                + "\",\"deadline\":\"" + deadline + "\"}]";
        client.ingest(entry);
        return decision;
    }
}

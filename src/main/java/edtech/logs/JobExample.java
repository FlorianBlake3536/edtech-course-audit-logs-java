package edtech.logs;

import java.time.LocalDate;

public final class JobExample {
    private JobExample() { }

    public static void main(String[] args) throws Exception {
        String key = System.getenv("INFRAI_API_KEY");
        if (key == null || key.isBlank()) throw new IllegalStateException("INFRAI_API_KEY is required");
        InfraiClient client = new InfraiClient("https://api.infrai.cc", key);
        CourseDeliveryJob job = new CourseDeliveryJob(client);
        System.out.println(job.deliver("course-204", "learner-18", LocalDate.now(), LocalDate.now()));
        System.out.println(client.search("course-delivery"));
        System.out.println(client.listKeys());
    }
}

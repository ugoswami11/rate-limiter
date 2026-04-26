import service.RateLimiterService;
import strategy.TokenBucketStrategy;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        RateLimiterService service =
                new RateLimiterService(new TokenBucketStrategy(5, 1));

        String user = "John.Wick";

        // Burst requests
        for (int i = 0; i < 7; i++) {
            boolean allowed = service.isAllowed(user);
            System.out.println("Request " + i + " : " + allowed);
        }

        System.out.println("Waiting for refill...");
        Thread.sleep(3000);

        // After refill
        for (int i = 0; i < 3; i++) {
            boolean allowed = service.isAllowed(user);
            System.out.println("After refill Request " + i + " : " + allowed);
        }

    }
}
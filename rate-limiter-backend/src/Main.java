public class Main {
    public static void main(String[] args) {

        RateLimiterService service = new RateLimiterService();

        String user = "John.Wick";

        for(int i=0; i<7 ; i++){
            boolean allowed = service.isAllowed(user);
            System.out.println("Request "+ i+" : "+allowed);
        }

        System.out.println("Waiting for refill");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(int i=0; i<3; i++){
            boolean allowed = service.isAllowed(user);
            System.out.println("Request "+ i+" : "+allowed);
        }

    }
}
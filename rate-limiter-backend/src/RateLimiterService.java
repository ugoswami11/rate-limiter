import java.util.HashMap;
import java.util.Map;

public class RateLimiterService {

    public Map<String, TokenBucket> buckets = new HashMap<>();

    public boolean isAllowed(String userId){
        TokenBucket bucket =  buckets.computeIfAbsent(userId, k -> new TokenBucket(5,1));
        return bucket.allowRequest();
    }
}

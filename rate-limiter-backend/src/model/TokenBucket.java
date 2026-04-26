package model;

public class TokenBucket {

    private int capacity;
    private int tokens;
    private int refillRate;
    private long lastRefillTime;

    public TokenBucket(int capacity, int refillRate ){
        this.capacity = capacity;
        this.refillRate = refillRate;
        tokens = capacity;
        lastRefillTime = System.currentTimeMillis();
    }

    public void refill(){
        long currentTime = System.currentTimeMillis();

        long elapsedTime = (currentTime - lastRefillTime)/1000;

        if(elapsedTime > 0){
            int tokensToAdd = (int) (elapsedTime * refillRate);
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = currentTime;
        }
    }

    public synchronized boolean allowRequest(){
        refill();

        if(tokens>0){
            tokens--;
            return true;
        }else{
            return false;
        }
    }

    public synchronized int getRemainingTokens() {
        return tokens;
    }

}

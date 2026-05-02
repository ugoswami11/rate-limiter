-- KEYS[1] = token key
-- Example: rate_limit:user123:tokens
--
-- KEYS[2] = last refill time key
-- Example: rate_limit:user123:last_refill
--
-- ARGV[1] = bucket capacity
-- Example: 5
--
-- ARGV[2] = refill rate (tokens per second)
-- Example: 1
--
-- ARGV[3] = current timestamp in milliseconds
-- Example: System.currentTimeMillis()

local tokenKey = KEYS[1]
local lastRefillKey = KEYS[2]

local capacity = tonumber(ARGV[1])
local refillRate = tonumber(ARGV[2])
local currentTime = tonumber(ARGV[3])

--------------------------------------------------
-- Step 1: Read current values from Redis
--------------------------------------------------

local tokens = redis.call("GET", tokenKey)
local lastRefillTime = redis.call("GET", lastRefillKey)

--------------------------------------------------
-- Step 2: First request initialization
--------------------------------------------------

if tokens == false or lastRefillTime == false then
    tokens = capacity
    lastRefillTime = currentTime
else
    tokens = tonumber(tokens)
    lastRefillTime = tonumber(lastRefillTime)
end

--------------------------------------------------
-- Step 3: Refill logic
--------------------------------------------------

local elapsedTime = math.floor(
    (currentTime - lastRefillTime) / 1000
)

if elapsedTime > 0 then
    local tokensToAdd = elapsedTime * refillRate

    tokens = math.min(
        capacity,
        tokens + tokensToAdd
    )

    lastRefillTime = currentTime
end

--------------------------------------------------
-- Step 4: Allow or block request
--------------------------------------------------

local allowed = 0

if tokens > 0 then
    tokens = tokens - 1
    allowed = 1
end

--------------------------------------------------
-- Step 5: Save updated values back to Redis
--------------------------------------------------

redis.call("SET", tokenKey, tokens)
redis.call("SET", lastRefillKey, lastRefillTime)

--------------------------------------------------
-- Step 6: Optional cleanup with TTL
--
-- Prevent Redis from storing old inactive users forever
--
-- Example:
-- expire after 1 hour (3600 seconds)
--------------------------------------------------

redis.call("EXPIRE", tokenKey, 3600)
redis.call("EXPIRE", lastRefillKey, 3600)

--------------------------------------------------
-- Step 7: Return result
--
-- return:
-- [allowed, remainingTokens]
--
-- Example:
-- [1, 4]
--------------------------------------------------

return {
    allowed,
    tokens
}
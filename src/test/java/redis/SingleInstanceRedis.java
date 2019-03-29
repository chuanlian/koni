package redis;

import redis.clients.jedis.Jedis;

public class SingleInstanceRedis {

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        int count = 0;
        while (count < 10000) {
            String key = "single" + count;
            Jedis jedis = new Jedis(RedisUtils.HOST, RedisUtils.PORT);
            jedis.set(key, "javaRedis");
            count++;
        }
        Long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
    }
}

package redis;

import redis.clients.jedis.Jedis;

/**
 * 不采用线程池访问单一redis实例
 */
public class SingleInstanceRedis {

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        try {
            int count = 0;
            while (count < 10000) {
                count++;
                System.out.println("count:" + count);
                Jedis jedis = getJedis();
                if (jedis != null) {
                    jedis.set("single" + count, "javaRedis");
                    jedis.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex);
        } finally {
            Long end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");
        }
    }

    private static Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = new Jedis(RedisUtils.HOST, RedisUtils.PORT_0);
        } catch (Exception ex) {
            System.out.println("redis init error:" + ex);
        }
        return jedis;
    }
}

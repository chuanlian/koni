package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class PoolRedis {

    private static JedisPool jedisPool = null;

    static {
        //获得池子对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //最大闲置个数
        poolConfig.setMaxIdle(RedisUtils.MAX_IDLE);
        //最小闲置个数
        poolConfig.setMinIdle(RedisUtils.MIN_IDLE);
        //最大连接数
        poolConfig.setMaxTotal(RedisUtils.MAX_TOTAL);
        jedisPool = new JedisPool(poolConfig, RedisUtils.HOST, RedisUtils.PORT);
    }

    //获得jedis资源的方法
    public synchronized static Jedis getJedis() {
        if (jedisPool != null) {
            Jedis resource = jedisPool.getResource();
            return resource;
        } else {
            return null;
        }
    }

    //存值：
    public static void setPool(String key, String value) {
        Jedis resource = getJedis();
        resource.set(key, value);
        jedisPool.returnResource(resource);
    }

    //取值：
    public static String getPool(String key) {
        Jedis resource = getJedis();
        return resource.get(key);
    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        try {
            int count = 0;
            while (count < 10000) {
                count++;
                System.out.println("count:" + count);
                String key = "pool" + count;
                setPool(key, "value");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            Long end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");
        }
    }
}

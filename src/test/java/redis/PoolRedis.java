package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * 单redis实例：通过线程池访问
 */
public class PoolRedis {

    private static JedisPool jedisPool = null;

    static {
        //连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //最大闲置个数
        poolConfig.setMaxIdle(RedisUtils.MAX_IDLE);
        //最小闲置个数
        poolConfig.setMinIdle(RedisUtils.MIN_IDLE);
        //最大连接数
        poolConfig.setMaxTotal(RedisUtils.MAX_TOTAL);
        jedisPool = new JedisPool(poolConfig, "10.94.162.58", 8068);
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
        Jedis jedis = getJedis();
        jedis.set(key, value);
        jedis.close();
    }

    //取值：
    public static String getPool(String key) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        try {
            int count = 0;
            while (count < 100000) {
                count++;
                System.out.println("count:" + count);
                setPool("pool" + count, "value");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            Long end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");
        }
    }
}

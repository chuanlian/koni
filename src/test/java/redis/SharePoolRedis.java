package redis;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Arrays;
import java.util.List;

/**
 * 采用线程池访问redis集群（ShardedJedis 一致性哈希）
 */
public class SharePoolRedis {

    private static ShardedJedisPool shardedJedisPool = null;

    static {
        //获得池子对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //最大闲置个数
        poolConfig.setMaxIdle(RedisUtils.MAX_IDLE);
        //最小闲置个数
        poolConfig.setMinIdle(RedisUtils.MIN_IDLE);
        //最大连接数
        poolConfig.setMaxTotal(RedisUtils.MAX_TOTAL);
        // 获取连接最大等待时间
        poolConfig.setMaxWaitMillis(2000);

        JedisShardInfo shardInfo1 = new JedisShardInfo(RedisUtils.HOST, RedisUtils.PORT_0, RedisUtils.TIME_OUT);
        JedisShardInfo shardInfo2 = new JedisShardInfo(RedisUtils.HOST, RedisUtils.PORT_1, RedisUtils.TIME_OUT);
        JedisShardInfo shardInfo3 = new JedisShardInfo(RedisUtils.HOST, RedisUtils.PORT_2, RedisUtils.TIME_OUT);
        List<JedisShardInfo> infoList = Arrays.asList(shardInfo1, shardInfo2, shardInfo3);

        //初始化SharedJedisPool
        shardedJedisPool = new ShardedJedisPool(poolConfig, infoList);
    }

    //获得jedis资源的方法
    public synchronized static ShardedJedis getShardedJedis() {
        if (shardedJedisPool != null) {
            ShardedJedis shardedJedis = shardedJedisPool.getResource();
            return shardedJedis;
        } else {
            return null;
        }
    }

    //存值：
    public static void setPool(String key, String value) {
        ShardedJedis shardedJedis = getShardedJedis();
        shardedJedis.set(key, value);
        shardedJedisPool.returnBrokenResource(shardedJedis);
    }

    //取值：
    public static String getPool(String key) {
        ShardedJedis shardedJedis = getShardedJedis();
        String value = shardedJedis.get(key);
        shardedJedisPool.returnResource(shardedJedis);
        return value;
    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        try {
            int count = 0;
            while (count < 100000) {
                count++;
                System.out.println("count:" + count);
                setPool("sharedPool" + count, "value");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            Long end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");
        }
    }
}

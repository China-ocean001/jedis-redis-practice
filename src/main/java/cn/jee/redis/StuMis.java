package cn.jee.redis;

import cn.jee.redis.domain.Stu;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.ArrayList;
import java.util.List;

public class StuMis {
    private static final String ID_KEY = "stu:id";
    private static final String JAVA_ZSET = "java:scores";
    private static final String MATH_ZSET = "math:scores";

    private Jedis jedis;

    public StuMis() {
        jedis = new Jedis();
    }

    // 清空数据库
    public void clear() {
        jedis.flushAll();
    }

    // 产生一个学生学号（自增）
    public long newId() {
        return jedis.incr(ID_KEY);
    }

    /**
     * 给Stu一个学号，然后保存在stu:学号的具体值的hash表中
     * 同时保存java成绩在sorted set中
     * 保存math成绩在sorted set中
     */
    public void add(Stu stu) {
        long id = newId();
        stu.setId(id);
        String hashKey = "stu:" + id;
        jedis.hset(hashKey, "id", String.valueOf(id));
        jedis.hset(hashKey, "username", stu.getUsername());
        jedis.hset(hashKey, "java", String.valueOf(stu.getJava()));
        jedis.hset(hashKey, "math", String.valueOf(stu.getMath()));
        jedis.zadd(JAVA_ZSET, stu.getJava(), String.valueOf(id));
        jedis.zadd(MATH_ZSET, stu.getMath(), String.valueOf(id));
    }

    /** 数学成绩最高的5个人的姓名 */
    public String[] top5ByMath() {
        // 从高到低取前5个成员的id
        List<String> ids = jedis.zrevrange(MATH_ZSET, 0, 4);
        String[] names = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            String hashKey = "stu:" + ids.get(i);
            names[i] = jedis.hget(hashKey, "username");
        }
        return names;
    }

    /** 成绩在min和max之间的java成绩 */
    public double[] betweenJava(double min, double max) {
        List<Tuple> tuples = jedis.zrangeByScoreWithScores(JAVA_ZSET, min, max);
        double[] scores = new double[tuples.size()];
        for (int i = 0; i < tuples.size(); i++) {
            scores[i] = tuples.get(i).getScore();
        }
        return scores;
    }

    /** 按学号查找，没有返回null */
    public Stu findById(long id) {
        String hashKey = "stu:" + id;
        String username = jedis.hget(hashKey, "username");
        if (username == null) {
            return null;
        }
        Stu stu = new Stu();
        stu.setId(id);
        stu.setUsername(username);
        stu.setJava(Double.parseDouble(jedis.hget(hashKey, "java")));
        stu.setMath(Double.parseDouble(jedis.hget(hashKey, "math")));
        return stu;
    }

    /** 按java成绩从低到高分页查询 */
    public List<Stu> pageByJava(int page, int size) {
        int start = (page - 1) * size;
        int end = start + size - 1;
        List<String> ids = jedis.zrange(JAVA_ZSET, start, end);
        List<Stu> result = new ArrayList<>();
        for (String id : ids) {
            Stu stu = findById(Long.parseLong(id));
            if (stu != null) {
                result.add(stu);
            }
        }
        return result;
    }
}

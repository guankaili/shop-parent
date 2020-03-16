package com.world.task;

import com.redis.RedisUtil;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2019/12/25$ 16:45$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019/12/25$ 16:45$     guankaili          v1.0.0           Created
 */
public class redisTest {
    public static void main(String[] args) {
        RedisUtil.set("test1111","111w2233111",10*60);
    }
}

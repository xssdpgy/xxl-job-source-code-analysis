package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.route.ExecutorRouter;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 轮询
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteRound extends ExecutorRouter {

    //缓存map
    private static ConcurrentHashMap<Integer, Integer> routeCountEachJob = new ConcurrentHashMap<Integer, Integer>();
    //缓存过期时间戳
    private static long CACHE_VALID_TIME = 0;
    private static int count(int jobId) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            //缓存过期，清空缓存map
            routeCountEachJob.clear();
            //设置缓存有效期为一天
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000*60*60*24;
        }

        // count++
        Integer count = routeCountEachJob.get(jobId);
        /**
         * 当第一次执行轮循这个策略的时候，routeCountEachJob这个Map里面没有这个地址，count==null
         * 当count==null或者count大于100万的时候，默认在100之间随机一个数字，放入hashMap，然后返回该数字
         * 当系统第二次进来的时候，count!=null 并且小于100万， 那么把count加1 之后返回出去。
         * 避免默认指定第一台时，所有任务的首次加载全部会到第一台执行器上面去，导致第一台机器刚开始的时候压力很大。
         */
        count = (count==null || count>1000000)?(new Random().nextInt(100)):++count;  // 初始化时主动Random一次，缓解首次压力
        routeCountEachJob.put(jobId, count);
        return count;
    }

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        //通过count（jobId）拿到数字之后， 通过求余的方式，拿到执行器地址
        String address = addressList.get(count(triggerParam.getJobId())%addressList.size());
        return new ReturnT<String>(address);
    }

}

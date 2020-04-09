package com.world.task.sbms.thread;

import cn.hutool.core.date.DateUtil;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkHomeYStats;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2020/3/20$ 8:39$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/3/20$ 8:39$     guankaili          v1.0.0           Created
 */
public class WorkHomeYStatsThread extends Thread{
    private static Logger logger = Logger.getLogger(WorkHomeYStatsThread.class);
    private WorkHomeYStats workHomeYStats;
    private CountDownLatch countDownLatch;

    public WorkHomeYStatsThread(WorkHomeYStats workHomeYStats, CountDownLatch countDownLatch) {
        this.workHomeYStats = workHomeYStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try{
            //1、插入数据
            String sql = "insert into work_home_y_stats (dealer_code,dealer_name,dealer_cm_id,shop_id,shop_type," +
                    "large_area_code,large_area_name,province_code,province_name,city_code,city_name,area_code," +
                    "area_name,brand_code,brand_name,sign_shop_quantity_y,sign_shop_task_quantity_y," +
                    "sign_shop_task_cquantity_y,sign_shop_task_nquantity_y,sign_shop_task_crate_y,create_date) " +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            List<Object> param = new ArrayList<>();
            param.add(workHomeYStats.getDealerCode());
            param.add(workHomeYStats.getDealerName());
            param.add(workHomeYStats.getDealerCmId());
            param.add(workHomeYStats.getShopId());
            param.add(workHomeYStats.getShopType());
            param.add(workHomeYStats.getLargeAreaCode());
            param.add(workHomeYStats.getLargeAreaName());
            param.add(workHomeYStats.getProvinceCode());
            param.add(workHomeYStats.getProvinceName());
            param.add(workHomeYStats.getCityCode());
            param.add(workHomeYStats.getCityName());
            param.add(workHomeYStats.getAreaCode());
            param.add(workHomeYStats.getAreaName());
            param.add(workHomeYStats.getBrandCode());
            param.add(workHomeYStats.getBrandName());
            param.add(workHomeYStats.getSignShopQuantityY() != null ? workHomeYStats.getSignShopQuantityY() : 0);
            param.add(workHomeYStats.getSignShopTaskQuantityY() != null ? workHomeYStats.getSignShopTaskQuantityY() : 0);
            param.add(workHomeYStats.getSignShopTaskCquantityY() != null ? workHomeYStats.getSignShopTaskCquantityY() : 0);
            param.add(workHomeYStats.getSignShopTaskNquantityY() != null ? workHomeYStats.getSignShopTaskNquantityY() : 0);
            param.add(0);
            param.add(DateUtil.date());
            sqls.add(new OneSql(sql, 1, param.toArray(), "sbms_main"));
            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                logger.info("插入成功");
            } else {
                logger.error("插入失败");
            }
        }catch (Exception e) {
            logger.error("数据新增失败",e);
        } finally {
            countDownLatch.countDown();
        }
    }
}

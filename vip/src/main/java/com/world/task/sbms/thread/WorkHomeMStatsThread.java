package com.world.task.sbms.thread;

import cn.hutool.core.date.DateUtil;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.WorkHomeMStats;
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
public class WorkHomeMStatsThread extends Thread{
    private static Logger logger = Logger.getLogger(WorkHomeMStatsThread.class);
    private WorkHomeMStats workHomeMStats;
    private CountDownLatch countDownLatch;

    public WorkHomeMStatsThread(WorkHomeMStats workHomeMStats, CountDownLatch countDownLatch) {
        this.workHomeMStats = workHomeMStats;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try{
            //1、插入数据
            String sql = "insert into work_home_m_stats (dealer_code,dealer_name,dealer_cm_id,shop_id,shop_type," +
                    "large_area_code,large_area_name,province_code,province_name,city_code,city_name,area_code," +
                    "area_name,brand_code,brand_name,sign_shop_quantity_m,sign_shop_task_quantity_m," +
                    "sign_shop_task_cquantity_m,sign_shop_task_nquantity_m,sign_shop_task_crate_m,create_date) " +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            List<Object> param = new ArrayList<>();
            param.add(workHomeMStats.getDealerCode());
            param.add(workHomeMStats.getDealerName());
            param.add(workHomeMStats.getDealerCmId());
            param.add(workHomeMStats.getShopId());
            param.add(workHomeMStats.getShopType());
            param.add(workHomeMStats.getLargeAreaCode());
            param.add(workHomeMStats.getLargeAreaName());
            param.add(workHomeMStats.getProvinceCode());
            param.add(workHomeMStats.getProvinceName());
            param.add(workHomeMStats.getCityCode());
            param.add(workHomeMStats.getCityName());
            param.add(workHomeMStats.getAreaCode());
            param.add(workHomeMStats.getAreaName());
            param.add(workHomeMStats.getBrandCode());
            param.add(workHomeMStats.getBrandName());
            param.add(workHomeMStats.getSignShopQuantityM() != null ? workHomeMStats.getSignShopQuantityM() : 0);
            param.add(workHomeMStats.getSignShopTaskQuantityM() != null ? workHomeMStats.getSignShopTaskQuantityM() : 0);
            param.add(workHomeMStats.getSignShopTaskCquantityM() != null ? workHomeMStats.getSignShopTaskCquantityM() : 0);
            param.add(workHomeMStats.getSignShopTaskNquantityM() != null ? workHomeMStats.getSignShopTaskNquantityM() : 0);
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

package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataHomeDealerStats;
import com.world.util.ObjectConversion;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2020/3/19$ 13:21$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/3/19$ 13:21$     guankaili          v1.0.0           Created
 */
public class DataHomeDealerStatsWorker extends Worker {
    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataHomeDealerStatsWorker(String name, String des) {
        super(name, des);
    }
    @Override
    public void run(){
        log.info("数据模块经销商进出货物统计:【经销商进出货物统计】开始");
        if (workFlag) {
            workFlag = false;
            try {
                //1、获取经销商进货量==年度
                String iSql = "SELECT t.dealer_code dealerCode,t.dealer_name dealerName,t.brand_name brandCode," +
                        "t.province_code provinceCode,t.province_name provinceName,IFNULL(sum(t.goods_num),0) dealerInQuantityY " +
                        "FROM t_order_outlet_goods t WHERE DATE_FORMAT(t.outlet_date,'%Y')=DATE_FORMAT(CURDATE(),'%Y') " +
                        "GROUP BY t.dealer_code,t.brand_name";
                List<Bean> list = (List<Bean>) Data.Query("qly", iSql, null, DataHomeDealerStats.class);
                List<DataHomeDealerStats> dataHomeDealerStatIns = null;
                if(!CollectionUtils.isEmpty(list)){
                    dataHomeDealerStatIns = ObjectConversion.copy(list, DataHomeDealerStats.class);
                }
                //2、获取经销商退货量==年度

            } catch (Exception e) {
                log.error("经销商进出货物统计失败", e);
            } finally {
                workFlag = true;
            }
        }else {
            log.info("工作模块门店任务统计:【经销商进出货物统计】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        DataHomeDealerStatsWorker workHomeStatsWorker = new DataHomeDealerStatsWorker("dataHomeDealerStatsWorker","经销商进出货物统计");
        workHomeStatsWorker.run();
    }
}

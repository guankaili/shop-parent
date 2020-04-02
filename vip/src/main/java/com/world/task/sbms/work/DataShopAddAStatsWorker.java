package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.*;
import com.world.task.sbms.thread.DataShopAddAStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/22 10:43
 * @desc 门店统计 新增门店 柱状图 数据表
 **/
public class DataShopAddAStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataShopAddAStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        log.info("门店统计数据首页:【新增门店柱状图统计】开始");
        if (workFlag) {
            try {
                workFlag = false;
                //获取当月的新增门店
                String sqlScanAdd = " SELECT " +
                        " t.dealer_name AS dealerName, " +
                        " t.dealer_code AS dealerCode, " +
                        " t.dealer_cm_id AS dealerCmId, " +
                        " t.dealer_cm_name AS dealerCmName, " +
                        " t.large_area_code AS largeAreaCode, " +
                        " t.large_area AS largeArea, " +
                        " date_format( t.contract_time, '%Y' ) AS shopAddYear, " +
                        " date_format( t.contract_time, '%m' ) AS shopAddMonth,  " +
                        " COUNT(t.id) AS shopAddCount "+
                        "FROM " +
                        " es_shop_detail t  " +
                        "WHERE " +
                        " ( t.shop_status = 5 OR t.shop_status = 6 OR t.shop_status = 7 )  " +
                        " AND date_format( t.contract_time, '%Y-%m' ) = DATE_FORMAT( now(), '%Y-%m' )  " +
                        "GROUP BY " +
                        " t.dealer_cm_id ";
                List<Bean> beans = Data.Query("shop_member", sqlScanAdd, null, DataShopAddAStats.class);
                if (StringUtil.isNotEmpty(beans)){
                    List<DataShopAddAStats> scanList = ObjectConversion.copy(beans, DataShopAddAStats.class);
                    List<DataShopAddAFromStats> list = fromData(scanList);

                    //一次性获取中间表所有数据，判断新增或更新
                    Map<String,String> map = new HashMap<>();
                    String ifSql = " SELECT t1.dealer_cm_id AS dealerCmId,t1.`year` AS `year` FROM data_shop_add_stats t1   ";
                    List<Bean> dealerCmIdStatuses = Data.Query("sbms_main", ifSql, null, DataDealerCmIdStatus.class);
                    if (StringUtil.isNotEmpty(dealerCmIdStatuses)){
                        List<DataDealerCmIdStatus> paList = ObjectConversion.copy(dealerCmIdStatuses, DataDealerCmIdStatus.class);
                        map = paList.stream().collect(Collectors.toMap(k->k.getDealerCmId()+k.getYear(),k->k.getDealerCmId()));
                    }

                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    for(DataShopAddAFromStats dataShopAddAFromStats : list){
                        //业务处理线程
                        DataShopAddAStatsThread dataShopAddAStatsThread = new DataShopAddAStatsThread(dataShopAddAFromStats,countDownLatch,map);
                        executorService.submit(dataShopAddAStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }

            } catch (Exception e) {
                log.error("新增门店柱状图数据同步失败", e);
            } finally {
                workFlag = true;
            }
        }
    }

    /**
     * 数据处理
     *
     * @param scanList
     * @return
     */
    private List<DataShopAddAFromStats> fromData(List<DataShopAddAStats> scanList) {
        List<DataShopAddAFromStats> add = new ArrayList<DataShopAddAFromStats>();
        for (DataShopAddAStats bean : scanList) {
            DataShopAddAFromStats fromBean = new DataShopAddAFromStats();
            fromBean.setDealerCmId(bean.getDealerCmId());
            fromBean.setDealerCode(bean.getDealerCode());
            fromBean.setDealerName(bean.getDealerName());
            fromBean.setLargeAreaCode(bean.getLargeAreaCode());
            fromBean.setLargeArea(bean.getLargeArea());
            fromBean.setShopAddYear(bean.getShopAddYear());
            switch (bean.getShopAddMonth()) {
                case "01":  //一月
                    fromBean.setShopAddQuantityM1(bean.getShopAddCount().intValue());
                    break;
                case "02":  //二月
                    fromBean.setShopAddQuantityM2(bean.getShopAddCount().intValue());
                    break;
                case "03":  //三月
                    fromBean.setShopAddQuantityM3(bean.getShopAddCount().intValue());
                    break;
                case "04":  //四月
                    fromBean.setShopAddQuantityM4(bean.getShopAddCount().intValue());
                    break;
                case "05":  //五月
                    fromBean.setShopAddQuantityM5(bean.getShopAddCount().intValue());
                    break;
                case "06":  //六月
                    fromBean.setShopAddQuantityM6(bean.getShopAddCount().intValue());
                    break;
                case "07":  //七月
                    fromBean.setShopAddQuantityM7(bean.getShopAddCount().intValue());
                    break;
                case "08":  //八月
                    fromBean.setShopAddQuantityM8(bean.getShopAddCount().intValue());
                    break;
                case "09":  //九月
                    fromBean.setShopAddQuantityM9(bean.getShopAddCount().intValue());
                    break;
                case "10":  //十月
                    fromBean.setShopAddQuantityM10(bean.getShopAddCount().intValue());
                    break;
                case "11":  //十一月
                    fromBean.setShopAddQuantityM11(bean.getShopAddCount().intValue());
                    break;
                case "12":  //十二月
                    fromBean.setShopAddQuantityM12(bean.getShopAddCount().intValue());
                    break;
                default:
            }
            add.add(fromBean);
        }
        return add;
    }

    public static void main(String[] args) {
        DataShopAddAStatsWorker dataShopAddAStatsWorker = new DataShopAddAStatsWorker("workDataShopAddAStatsThread", "门店统计 新增门店统计 数据同步");
        dataShopAddAStatsWorker.run();
    }

}

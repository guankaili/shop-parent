package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.*;
import com.world.task.sbms.thread.DataShopSignBuyAStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/22 10:44
 * @desc 签约、抢购 柱状图 数据获取
 **/
public class DataShopSignBuyAStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataShopSignBuyAStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {

        log.info("签约、抢购门店统计柱状图任务统计:【签约、抢购门店统计柱状图数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {
                String sql = "SELECT " +
                        "t.dealer_name AS dealerName, " +
                        "t.dealer_code AS dealerCode, " +
                        "t.dealer_cm_id AS dealerCmId, " +
                        "t.dealer_cm_name AS dealerCmName, " +
                        "t.large_area_code AS largeAreaCode, " +
                        "t.large_area AS largeArea, " +
                        "date_format(t.contract_time,'%Y') AS shopSignBuyYear," +
                        "date_format(t.contract_time,'%m') AS shopSignBuyMonth, " +
                        "COUNT( " +
                        " t.shop_status = 6 " +
                        "OR NULL " +
                        ") AS shopBuyCount, " +
                        "COUNT(t.id) AS shopSignCount " +
                        "FROM " +
                        "es_shop_detail t " +
                        " WHERE date_format( t.contract_time, '%Y-%m' ) = DATE_FORMAT( now(), '%Y-%m' ) AND (t.shop_status = 5 OR t.shop_status = 6 OR t.shop_status = 7 )" +
                        "GROUP BY " +
                        "t.dealer_cm_id ";
                List<Bean> beans = Data.Query("shop_member", sql, null, DataShopSignBuyAStats.class);
                if (StringUtil.isNotEmpty(beans)) {
                    List<DataShopSignBuyAStats> scanList = ObjectConversion.copy(beans, DataShopSignBuyAStats.class);
                    List<DataShopSignBuyAFromStats> list = fromData(scanList);

                    //一次性获取中间表所有数据，判断新增或更新
                    Map<String, String> map = new ConcurrentHashMap<String, String>();
                    String ifSql = " SELECT t1.dealer_cm_id AS dealerCmId,t1.`year` AS `year` FROM data_shop_signbuy_stats t1   ";
                    List<Bean> dealerCmIdStatuses = Data.Query("sbms_main", ifSql, null, DataDealerCmIdStatus.class);
                    if (StringUtil.isNotEmpty(dealerCmIdStatuses)) {
                        List<DataDealerCmIdStatus> paList = ObjectConversion.copy(dealerCmIdStatuses, DataDealerCmIdStatus.class);
                        map = paList.stream().collect(Collectors.toMap(k -> k.getDealerCmId() + k.getYear(), k -> k.getDealerCmId()));
                    }

                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    for (DataShopSignBuyAFromStats dataShopSignBuyAFromStats : list) {
                        //业务处理线程
                        DataShopSignBuyAStatsThread dataShopSignBuyAStatsThread = new DataShopSignBuyAStatsThread(dataShopSignBuyAFromStats, countDownLatch, map);
                        executorService.submit(dataShopSignBuyAStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }
            } catch (Exception e) {
                log.error("签约、抢购门店统计数据同步失败", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("签约、抢购门店统计任务统计:【签约、抢购门店统计数据同步】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    /**
     * 数据处理
     *
     * @param scanList
     * @return
     */
    private List<DataShopSignBuyAFromStats> fromData(List<DataShopSignBuyAStats> scanList) {
        List<DataShopSignBuyAFromStats> signBay = new ArrayList<DataShopSignBuyAFromStats>();
        for (DataShopSignBuyAStats bean : scanList) {
            DataShopSignBuyAFromStats fromBean = new DataShopSignBuyAFromStats();
            fromBean.setDealerCmId(bean.getDealerCmId());
            fromBean.setDealerCode(bean.getDealerCode());
            fromBean.setDealerName(bean.getDealerName());
            fromBean.setLargeAreaCode(bean.getLargeAreaCode());
            fromBean.setLargeArea(bean.getLargeArea());
            fromBean.setShopSignBuyYear(bean.getShopSignBuyYear());
            switch (bean.getShopSignBuyMonth()) {
                case "01":  //一月
                    fromBean.setShopSignQuantityM1(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM1(bean.getShopBuyCount().intValue());
                    break;
                case "02":  //二月
                    fromBean.setShopSignQuantityM2(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM2(bean.getShopBuyCount().intValue());
                    break;
                case "03":  //三月
                    fromBean.setShopSignQuantityM3(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM3(bean.getShopBuyCount().intValue());
                    break;
                case "04":  //四月
                    fromBean.setShopSignQuantityM4(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM4(bean.getShopBuyCount().intValue());
                    break;
                case "05":  //五月
                    fromBean.setShopSignQuantityM5(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM5(bean.getShopBuyCount().intValue());
                    break;
                case "06":  //六月
                    fromBean.setShopSignQuantityM6(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM6(bean.getShopBuyCount().intValue());
                    break;
                case "07":  //七月
                    fromBean.setShopSignQuantityM7(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM7(bean.getShopBuyCount().intValue());
                    break;
                case "08":  //八月
                    fromBean.setShopSignQuantityM8(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM8(bean.getShopBuyCount().intValue());
                    break;
                case "09":  //九月
                    fromBean.setShopSignQuantityM9(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM9(bean.getShopBuyCount().intValue());
                    break;
                case "10":  //十月
                    fromBean.setShopSignQuantityM10(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM10(bean.getShopBuyCount().intValue());
                    break;
                case "11":  //十一月
                    fromBean.setShopSignQuantityM11(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM11(bean.getShopBuyCount().intValue());
                    break;
                case "12":  //十二月
                    fromBean.setShopSignQuantityM12(bean.getShopSignCount().intValue());
                    fromBean.setShopBuyQuantityM12(bean.getShopBuyCount().intValue());
                    break;
                default:
            }
            signBay.add(fromBean);
        }
        return signBay;
    }

    public static void main(String[] args) {
        DataShopSignBuyAStatsWorker dataShopSignBuyAStatsWorker = new DataShopSignBuyAStatsWorker("workDataShopSignBuyAStatsWorker", "门店统计 签约、抢购门店列表 数据同步");
        dataShopSignBuyAStatsWorker.run();
    }
}

package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataDealerCmIdStatus;
import com.world.model.sbms.DataShopScanInMYFromStats;
import com.world.model.sbms.DataShopScanInMYStats;
import com.world.task.sbms.thread.DataShopScanInMYStatsThread;
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
 * @date 2020/3/21 9:25
 * @desc 门店扫码 --扫码入库柱状图数据 --定时任务
 **/
public class DataShopScanInMYStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataShopScanInMYStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        log.info("门店扫码入库柱状图任务统计:【门店扫码入库柱状图数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {
                //获取扫码入库的数量  根据信贷员分组获取 仅统计当前的月份
                String sqlScan = " SELECT t.dealer_name AS dealerName," +
                        "t.dealer_code AS dealerCode," +
                        "t.brand_code AS brandCode," +
                        "t.brand_name AS brandName," +
                        "t.dealer_cm_id AS dealerCmId," +
                        "date_format(t.create_datetime,'%Y') AS scanYear," +
                        "date_format(t.create_datetime,'%m') AS scanMonth, " +
                        "COUNT(t.scan_type = 3 AND date_format(t.create_datetime, '%Y-%m') = DATE_FORMAT(now(), '%Y-%m') OR NULL) AS scanInCount " +
                        "FROM scan_batch_record_detail t GROUP BY t.dealer_cm_id ";

                List<Bean> shopBeans = Data.Query("scan_main", sqlScan, null, DataShopScanInMYStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<DataShopScanInMYStats> scanList = ObjectConversion.copy(shopBeans, DataShopScanInMYStats.class);
                    List<DataShopScanInMYFromStats> list = fromData(scanList);

                    //一次性获取中间表所有数据，判断新增或更新
                    Map<String,String> map = new ConcurrentHashMap<String, String>();
                    String ifSql = " SELECT t1.dealer_cm_id AS dealerCmId,t1.`year` AS `year` FROM data_scan_in_stats t1  ";
                    List<Bean> dealerCmIdStatuses = Data.Query("sbms_main", ifSql, null, DataDealerCmIdStatus.class);
                    if (StringUtil.isNotEmpty(dealerCmIdStatuses)){
                        List<DataDealerCmIdStatus> paList = ObjectConversion.copy(dealerCmIdStatuses, DataDealerCmIdStatus.class);
                        map = paList.stream().collect(Collectors.toMap(k->k.getDealerCmId()+k.getYear(), k->k.getDealerCmId()));
                    }

                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    for(DataShopScanInMYFromStats dataShopScanInMYFromStats : list){
                        //业务处理线程
                        DataShopScanInMYStatsThread dataShopScanInMYStatsThread = new DataShopScanInMYStatsThread(dataShopScanInMYFromStats,countDownLatch,map);
                        executorService.submit(dataShopScanInMYStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }

            } catch (Exception e) {
                log.error("门店扫码入库柱状图同步失败", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("工作模块门店任务统计:【门店扫码入库柱状图数据同步】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    /**
     * 数据处理
     *
     * @param scanList
     * @return
     */
    private List<DataShopScanInMYFromStats> fromData(List<DataShopScanInMYStats> scanList) {
        List<DataShopScanInMYFromStats> ins = new ArrayList<DataShopScanInMYFromStats>();
        for (DataShopScanInMYStats bean : scanList) {
            DataShopScanInMYFromStats fromBean = new DataShopScanInMYFromStats();
            fromBean.setDealerCmId(bean.getDealerCmId());
            fromBean.setDealerCode(bean.getDealerCode());
            fromBean.setDealerName(bean.getDealerName());
            fromBean.setLargeAreaCode(bean.getLargeAreaCode());
            fromBean.setLargeAreaName(bean.getLargeAreaName());
            fromBean.setScanYear(bean.getScanYear());
            switch (bean.getScanMonth()) {
                case "01":  //一月
                    fromBean.setShopInQuantityM1(bean.getScanInCount().intValue());
                    break;
                case "02":  //二月
                    fromBean.setShopInQuantityM2(bean.getScanInCount().intValue());
                    break;
                case "03":  //三月
                    fromBean.setShopInQuantityM3(bean.getScanInCount().intValue());
                    break;
                case "04":  //四月
                    fromBean.setShopInQuantityM4(bean.getScanInCount().intValue());
                    break;
                case "05":  //五月
                    fromBean.setShopInQuantityM5(bean.getScanInCount().intValue());
                    break;
                case "06":  //六月
                    fromBean.setShopInQuantityM6(bean.getScanInCount().intValue());
                    break;
                case "07":  //七月
                    fromBean.setShopInQuantityM7(bean.getScanInCount().intValue());
                    break;
                case "08":  //八月
                    fromBean.setShopInQuantityM8(bean.getScanInCount().intValue());
                    break;
                case "09":  //九月
                    fromBean.setShopInQuantityM9(bean.getScanInCount().intValue());
                    break;
                case "10":  //十月
                    fromBean.setShopInQuantityM10(bean.getScanInCount().intValue());
                    break;
                case "11":  //十一月
                    fromBean.setShopInQuantityM11(bean.getScanInCount().intValue());
                    break;
                case "12":  //十二月
                    fromBean.setShopInQuantityM12(bean.getScanInCount().intValue());
                    break;
                default:
            }
            ins.add(fromBean);
        }
        return ins;
    }

    public static void main(String[] args) {
        DataShopScanInMYStatsWorker dataShopScanInMYStatsWorker = new DataShopScanInMYStatsWorker("WorkDataShopScanInMYStatsWorker", "门店扫码入库柱状图数据同步 数据同步");
        dataShopScanInMYStatsWorker.run();
    }


}

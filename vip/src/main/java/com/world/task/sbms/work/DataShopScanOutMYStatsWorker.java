package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.DataShopScanOutMYFromStats;
import com.world.model.sbms.DataShopScanOutMYStats;
import com.world.task.sbms.thread.DataShopScanOutMYStatsThread;
import com.world.util.ObjectConversion;
import com.world.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO
 *
 * @author tanweizheng
 * @date 2020/3/21 9:25
 * @desc 门店扫码 --扫码出库柱状图数据 --定时任务
 **/
public class DataShopScanOutMYStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public DataShopScanOutMYStatsWorker(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        log.info("门店扫码出库柱状图任务统计:【门店扫码出库柱状图数据同步】开始");
        if (workFlag) {
            workFlag = false;
            try {
                //获取扫码入库的数量  根据信贷员分组获取 仅统计当前的月份
                String sqlScan = " SELECT t.dealer_name AS dealerName,t.dealer_code AS dealerCode,t.brand_code AS brandCode,t.brand_name AS brandName,t.dealer_cm_id AS dealerCmId,date_format(t.create_datetime,'%Y') AS scanYear,date_format(t.create_datetime,'%m') AS scanMonth, " +
                        "COUNT(t.scan_type = 4 AND date_format(t.create_datetime, '%Y-%m') = DATE_FORMAT(now(), '%Y-%m') OR NULL) AS scanOutCount " +
                        "FROM scan_batch_record_detail t GROUP BY t.dealer_cm_id ";

                List<Bean> shopBeans = Data.Query("scan_main", sqlScan, null, DataShopScanOutMYStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<DataShopScanOutMYStats> scanList = ObjectConversion.copy(shopBeans, DataShopScanOutMYStats.class);
                    List<DataShopScanOutMYFromStats> list = fromData(scanList);
                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    for(DataShopScanOutMYFromStats dataShopScanOutMYFromStats : list){
                        //业务处理线程
                        DataShopScanOutMYStatsThread dataShopScanOutMYStatsThread = new DataShopScanOutMYStatsThread(dataShopScanOutMYFromStats,countDownLatch);
                        executorService.submit(dataShopScanOutMYStatsThread);
                    }
                    countDownLatch.await();
                    /*关闭线程池*/
                    executorService.shutdown();
                }

            } catch (Exception e) {
                log.error("门店扫码出库柱状图同步失败", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("工作模块门店任务统计:【门店扫码出库柱状图数据同步】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    /**
     * 数据处理
     *
     * @param scanList
     * @return
     */
    private List<DataShopScanOutMYFromStats> fromData(List<DataShopScanOutMYStats> scanList) {
        List<DataShopScanOutMYFromStats> ins = new ArrayList<DataShopScanOutMYFromStats>();
        for (DataShopScanOutMYStats bean : scanList) {
            DataShopScanOutMYFromStats fromBean = new DataShopScanOutMYFromStats();
            fromBean.setDealerCmId(bean.getDealerCmId());
            fromBean.setDealerCode(bean.getDealerCode());
            fromBean.setDealerName(bean.getDealerName());
            fromBean.setLargeAreaCode(bean.getLargeAreaCode());
            fromBean.setLargeAreaName(bean.getLargeAreaName());
            fromBean.setScanYear(bean.getScanYear());
            switch (bean.getScanMonth()) {
                case "01":  //一月
                    fromBean.setShopOutQuantityM1(bean.getScanOutCount().intValue());
                    break;
                case "02":  //二月
                    fromBean.setShopOutQuantityM2(bean.getScanOutCount().intValue());
                    break;
                case "03":  //三月
                    fromBean.setShopOutQuantityM3(bean.getScanOutCount().intValue());
                    break;
                case "04":  //四月
                    fromBean.setShopOutQuantityM4(bean.getScanOutCount().intValue());
                    break;
                case "05":  //五月
                    fromBean.setShopOutQuantityM5(bean.getScanOutCount().intValue());
                    break;
                case "06":  //六月
                    fromBean.setShopOutQuantityM6(bean.getScanOutCount().intValue());
                    break;
                case "07":  //七月
                    fromBean.setShopOutQuantityM7(bean.getScanOutCount().intValue());
                    break;
                case "08":  //八月
                    fromBean.setShopOutQuantityM8(bean.getScanOutCount().intValue());
                    break;
                case "09":  //九月
                    fromBean.setShopOutQuantityM9(bean.getScanOutCount().intValue());
                    break;
                case "10":  //十月
                    fromBean.setShopOutQuantityM10(bean.getScanOutCount().intValue());
                    break;
                case "11":  //十一月
                    fromBean.setShopOutQuantityM11(bean.getScanOutCount().intValue());
                    break;
                case "12":  //十二月
                    fromBean.setShopOutQuantityM12(bean.getScanOutCount().intValue());
                    break;
                default:
            }
            ins.add(fromBean);
        }
        return ins;
    }

    public static void main(String[] args) {
        DataShopScanOutMYStatsWorker dataShopScanOutMYStatsWorker = new DataShopScanOutMYStatsWorker("WorkDataShopScanOutMYStatsWorker", "门店扫码出库柱状图数据同步 数据同步");
        dataShopScanOutMYStatsWorker.run();
    }


}

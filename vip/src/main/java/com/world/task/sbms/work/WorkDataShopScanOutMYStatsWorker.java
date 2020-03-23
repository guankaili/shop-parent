package com.world.task.sbms.work;

import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.sbms.WorkDataShopScanInMYFromStats;
import com.world.model.sbms.WorkDataShopScanInMYStats;
import com.world.model.sbms.WorkDataShopScanOutMYFromStats;
import com.world.model.sbms.WorkDataShopScanOutMYStats;
import com.world.task.sbms.thread.WorkDataShopScanInMYStatsThread;
import com.world.task.sbms.thread.WorkDataShopScanOutMYStatsThread;
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
public class WorkDataShopScanOutMYStatsWorker extends Worker {

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public WorkDataShopScanOutMYStatsWorker(String name, String des) {
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

                List<Bean> shopBeans = Data.Query("scan_main", sqlScan, null, WorkDataShopScanOutMYStats.class);
                if (StringUtil.isNotEmpty(shopBeans)) {
                    List<WorkDataShopScanOutMYStats> scanList = ObjectConversion.copy(shopBeans, WorkDataShopScanOutMYStats.class);
                    List<WorkDataShopScanOutMYFromStats> list = fromData(scanList);
                    //构建线程池
                    //当提交的任务数量为1000的时候，会开辟20个线程数
                    ExecutorService executorService = Executors.newFixedThreadPool(10);
                    CountDownLatch countDownLatch = new CountDownLatch(list.size());
                    for(WorkDataShopScanOutMYFromStats workDataShopScanOutMYFromStats : list){
                        //业务处理线程
                        WorkDataShopScanOutMYStatsThread workDataShopScanOutMYStatsThread = new WorkDataShopScanOutMYStatsThread(workDataShopScanOutMYFromStats,countDownLatch);
                        executorService.submit(workDataShopScanOutMYStatsThread);
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
    private List<WorkDataShopScanOutMYFromStats> fromData(List<WorkDataShopScanOutMYStats> scanList) {
        List<WorkDataShopScanOutMYFromStats> ins = new ArrayList<WorkDataShopScanOutMYFromStats>();
        for (WorkDataShopScanOutMYStats bean : scanList) {
            WorkDataShopScanOutMYFromStats fromBean = new WorkDataShopScanOutMYFromStats();
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
        WorkDataShopScanOutMYStatsWorker workDataShopScanOutMYStatsWorker = new WorkDataShopScanOutMYStatsWorker("WorkDataShopScanOutMYStatsWorker", "门店扫码出库柱状图数据同步 数据同步");
        workDataShopScanOutMYStatsWorker.run();
    }


}

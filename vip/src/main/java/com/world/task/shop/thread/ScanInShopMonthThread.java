package com.world.task.shop.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Bean;
import com.world.model.shop.ScanBatchRecordDetailModel;
import com.world.model.shop.ShopConfIntegralModel;
import com.world.model.shop.ShopConfRebateModel;
import com.world.model.shop.ShopConfTaskMModel;
import com.world.model.shop.ShopDetailModel;
import com.world.task.shop.util.ScanUtil;

public class ScanInShopMonthThread extends Thread {
    private static Logger log = Logger.getLogger(ScanInShopThread.class);
    /*sql语句*/
//    private String sql = "";
    /**报警msg*/
    private  String msg = "";
    private CountDownLatch countDownLatch;
    private ScanBatchRecordDetailModel scanBatchRecordDetailModel;
    //扫码工具栏
    private ScanUtil scanUtil;
    /**
     * 全局使用
     */
    //已扫码数量，没扫1次加1，全局使用
    private int hasScanInNum;
    //已扫码的积分
    private int calScanIntegralTotalScore;
    //已扫码的返利
    private int calScanRebateTotalScore;

    public ScanInShopMonthThread(ScanBatchRecordDetailModel scanBatchRecordDetailModel, CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.scanBatchRecordDetailModel = scanBatchRecordDetailModel;
        this.scanUtil = new ScanUtil();
        this.hasScanInNum = 0;
        this.calScanIntegralTotalScore = 0;
        this.calScanRebateTotalScore = 0;
    }

    @Override
    public void run() {
        log.info("ScanInShopMonthThread...run...scanBatchRecordDetailModel = " + JSON.toJSONString(scanBatchRecordDetailModel));
        /**门店ID*/
        int shopId = scanBatchRecordDetailModel.getShop_id();

        try {
        	//门店类型
            int shopType = 1;
            //买家ID
//            int memberId = 0;
            //门店信息
            ShopDetailModel shopDetailModel = scanUtil.findShopDetail(shopId);
            if (null == shopDetailModel || null == shopDetailModel.getShop_type() || null == shopDetailModel.getMember_id()) {
                msg = "扫码退货报警REWARDERROR【门店信息不存在】";
                log.info(msg);
                throw new Exception(msg);
            }
            shopType = null == shopDetailModel.getShop_type() ? 1 : shopDetailModel.getShop_type();
//            memberId = shopDetailModel.getMember_id();
        	
            /**
             * 查询门店月度任务量
             */
            ShopConfTaskMModel shopConfTaskMModel = scanUtil.findShopConfTaskM(shopType);
        	
            /**
             * 获取本月正常的扫码入库记录
             * `flow_state` int(11) DEFAULT '0' COMMENT '订单状态：1-正常;2-门店入库退货;3-门店未入库退货;'   不需要
             * `scan_type` int(11) DEFAULT NULL COMMENT '扫码种类：1-经销商出库;2-经销商退货扫码;3-门店入库;4-门店出库',
             */
            List<Bean> scanBatchRecordDetailModelList = scanUtil.listScanBatchRecordDetail(shopId, "2020-04");
            
            /**检查月度任务量是否完成*/
            boolean checkMonthTaskFlag = this.checkMonthTask(scanBatchRecordDetailModelList.size(), shopType, shopConfTaskMModel);
            log.info("checkMonthTaskFlag = " + checkMonthTaskFlag);
            if(!checkMonthTaskFlag) {
            	log.info("扫码月结报警REWARDINFO:【门店月度任务未完成，不能获得相应积分和返利】");
            	return;
            }
            
            /**
             * 先删除已有数据，防止重复计算
             */
            
            /**月结循环处理*/
            this.calMonth(scanBatchRecordDetailModelList, shopConfTaskMModel, shopType);


        } catch (Exception e) {
            log.info("扫码入库报警REWARDERROR:ScanInShopMonthThread", e);
        } finally {
            countDownLatch.countDown();
        }




    }
    
    
    
    
    
    private boolean checkMonthTask(int monthScanInNum, int shopType, ShopConfTaskMModel shopConfTaskMModel ) throws Exception {
    	//check返回值
    	boolean checkMonthTaskFlag = false;
    	
    	//月度任务量
        int shopTaskM = shopConfTaskMModel.getShop_task_m();
        log.info("monthScanInNum = " + monthScanInNum + ", shopTaskM = " + shopTaskM);
        if (monthScanInNum >= shopTaskM) {
        	checkMonthTaskFlag = true;
        }
        
        return checkMonthTaskFlag;
	}
    

	public void calMonth(List<Bean> scanBatchRecordDetailModelList, ShopConfTaskMModel shopConfTaskMModel, int shopType) throws Exception {
    	//扫码入库记录
        ScanBatchRecordDetailModel scanBatchRecordDetailModel = null;
        for (int i = 0; i < scanBatchRecordDetailModelList.size(); i++) {
            scanBatchRecordDetailModel = (ScanBatchRecordDetailModel) scanBatchRecordDetailModelList.get(i);
            
            /**
             * 按照扫码顺序，逐一处理
             */
            this.calOneByOne(scanBatchRecordDetailModel, shopConfTaskMModel, shopType);
        }
        log.info("hasScanInNum = " + hasScanInNum);
        log.info("calScanIntegralTotalScore = " + calScanIntegralTotalScore);
        log.info("calScanRebateTotalScore = " + calScanRebateTotalScore);
	}
    
    public void calOneByOne(ScanBatchRecordDetailModel scanBatchRecordDetailModel, ShopConfTaskMModel shopConfTaskMModel, int shopType) throws Exception {
    	/**拆分获取商品size*/
        int goodsSizeInt = scanUtil.splitGoodsSize(scanBatchRecordDetailModel);
        
        /**
         * 按照当前已扫描数量和门店类型，计算扫描此条应获得的积分，可升级
         */
        int calScanIntegralScore = scanUtil.produceScanIntegralScore(hasScanInNum, shopConfTaskMModel, shopType, goodsSizeInt);
        /**
         * 按照当前已扫描数量和门店类型，计算扫描此条应获得的返利，可升级
         */
        int calScanRebateScore = scanUtil.produceScanRebateScore(hasScanInNum, shopConfTaskMModel, shopType, goodsSizeInt);
    	log.info("calScanIntegralScore = " + calScanIntegralScore + ", calScanRebateScore = " + calScanRebateScore);
        
        /**
         * 积分配置：按门店类型和size匹配对应积分分值，原始配置
         */
        ShopConfIntegralModel shopConfIntegralModel = scanUtil.findShopConfIntegral(shopType, goodsSizeInt);
        //对应的积分值
        int confIntegralScore = null == shopConfIntegralModel.getScore() ? 0 : shopConfIntegralModel.getScore();
        log.info("confIntegralScore = " + confIntegralScore);

        /**
         * 返利配置：按门店类型和size匹配对应返利分值，原始配置
         */
        ShopConfRebateModel shopConfRebateModel = scanUtil.findShopConfRebate(shopType, goodsSizeInt);
        //对应的积分值
        int confRebateScore = shopConfRebateModel.getScore();
        log.info("confRebateScore = " + confRebateScore);
        
        /**
         * 记录日志
         * TODO
         */
        
        
        /**
         * 扫码数量，积分，返利
         */
        //计数加1
        this.hasScanInNum++;
        //已扫码的积分
        calScanIntegralTotalScore = calScanRebateTotalScore + calScanRebateScore;
        //已扫码的返利
        calScanRebateTotalScore = calScanRebateTotalScore + calScanRebateScore;
	}
    
    
    
    

	
}

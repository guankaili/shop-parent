package com.world.task.sbms.thread;

import cn.hutool.core.date.DateUtil;
import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.sbms.DealerGoodsStats;
import com.world.model.sbms.GoodsQuantity;
import com.world.util.enums.BrandEnum;
import com.world.util.enums.MonthEnum;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
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
public class DataDealerOgoodsStatsThread extends Thread{
    private static Logger logger = Logger.getLogger(DataDealerOgoodsStatsThread.class);
    private List<DealerGoodsStats> dealerGoodsStatsList;
    private CountDownLatch countDownLatch;

    public DataDealerOgoodsStatsThread(List<DealerGoodsStats> dealerGoodsStatsList, CountDownLatch countDownLatch) {
        this.dealerGoodsStatsList = dealerGoodsStatsList;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<OneSql> sqls = new ArrayList<>();
        TransactionObject txObj = new TransactionObject();
        try{
            //1、插入数据
            String sql = "insert into data_dealer_ogoods_stats (dealer_code,dealer_name,province_code,province_name," +
                    "brand_code,brand_name,YEAR,out_goods_quantity_m1,out_goods_quantity_m2,out_goods_quantity_m3," +
                    "out_goods_quantity_m4,out_goods_quantity_m5,out_goods_quantity_m6,out_goods_quantity_m7,out_goods_quantity_m8," +
                    "out_goods_quantity_m9,out_goods_quantity_m10,out_goods_quantity_m11,out_goods_quantity_m12,create_date) " +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            List<Object> param = new ArrayList<>();
            //获取年月日
            LocalDateTime nowTime = LocalDateTime.now();
            //年
            int year = nowTime.getYear();
            GoodsQuantity goodsQuantity = new GoodsQuantity();
            dealerGoodsStatsList.forEach(item -> {
                int goodsNum = item.getGoodsNum() != null ? Integer.valueOf(item.getGoodsNum().toString()) : 0;
                if(item.getM().toString().equals(MonthEnum.M1.getCode())){
                    goodsQuantity.setGoodsQuantityM1(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M2.getCode())){
                    goodsQuantity.setGoodsQuantityM2(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M3.getCode())){
                    goodsQuantity.setGoodsQuantityM3(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M4.getCode())){
                    goodsQuantity.setGoodsQuantityM4(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M5.getCode())){
                    goodsQuantity.setGoodsQuantityM5(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M6.getCode())){
                    goodsQuantity.setGoodsQuantityM6(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M7.getCode())){
                    goodsQuantity.setGoodsQuantityM7(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M8.getCode())){
                    goodsQuantity.setGoodsQuantityM8(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M9.getCode())){
                    goodsQuantity.setGoodsQuantityM9(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M10.getCode())){
                    goodsQuantity.setGoodsQuantityM10(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M11.getCode())){
                    goodsQuantity.setGoodsQuantityM11(goodsNum);
                }
                if(item.getM().toString().equals(MonthEnum.M12.getCode())){
                    goodsQuantity.setGoodsQuantityM12(goodsNum);
                }
            });
            param.add(dealerGoodsStatsList.get(0).getDealerCode());
            param.add(dealerGoodsStatsList.get(0).getDealerName());
            param.add(dealerGoodsStatsList.get(0).getProvinceCode());
            param.add(dealerGoodsStatsList.get(0).getProvinceName());
            param.add(dealerGoodsStatsList.get(0).getBrandCode());
            param.add(BrandEnum.getByCode(dealerGoodsStatsList.get(0).getBrandCode()).getCode());
            param.add(year);
            param.add(goodsQuantity.getGoodsQuantityM1());
            param.add(goodsQuantity.getGoodsQuantityM2());
            param.add(goodsQuantity.getGoodsQuantityM3());
            param.add(goodsQuantity.getGoodsQuantityM4());
            param.add(goodsQuantity.getGoodsQuantityM5());
            param.add(goodsQuantity.getGoodsQuantityM6());
            param.add(goodsQuantity.getGoodsQuantityM7());
            param.add(goodsQuantity.getGoodsQuantityM8());
            param.add(goodsQuantity.getGoodsQuantityM9());
            param.add(goodsQuantity.getGoodsQuantityM10());
            param.add(goodsQuantity.getGoodsQuantityM11());
            param.add(goodsQuantity.getGoodsQuantityM12());
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

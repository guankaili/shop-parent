package com.world.task.shop.thread;

import com.world.data.mysql.OneSql;
import com.world.data.mysql.transaction.TransactionObject;
import com.world.model.shop.EsMemberCouponModel;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class ConponAppendThread extends Thread {
    private static Logger log = Logger.getLogger(ConponAppendThread.class);
    /**
     * sql语句
     */
    private String sql = "";

    private CountDownLatch countDownLatch;
    private EsMemberCouponModel esMemberCouponModel;

    public ConponAppendThread(EsMemberCouponModel esMemberCouponModel, CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.esMemberCouponModel = esMemberCouponModel;
    }

    @Override
    public void run() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(calendar.DATE, 60); //把日期往后增加60天,整数  往后推,负数往前移动
        Date afterSixDay = calendar.getTime();

        String startTime = simpleDateFormat.format(now);
        String endTime = simpleDateFormat.format(afterSixDay);

        //获取member_id
        Integer memberId = esMemberCouponModel.getMemberId();

        try {
            //开启事务处理
            List<OneSql> sqls = new ArrayList<OneSql>();
            TransactionObject txObj = new TransactionObject();

            //生成赠券64张
            sql = "insert into es_member_coupon (coupon_sn,member_id,create_time,title,coupon_price,used_status,seller_id,seller_name,coupon_type,start_time,end_time) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?)";
            for (int i = 0; i < 64; i++) {
                Object[] object = new Object[]{UUID.randomUUID().toString().substring(0, 8).toUpperCase(), memberId, startTime, "新人激励券", 15, 0, 1, "森麒麟自营店", 2, startTime, endTime};
                sqls.add(new OneSql(sql, 1, object, "shop_trade"));
            }

            txObj.excuteUpdateList(sqls);
            if (txObj.commit()) {
                log.info("门店追加64张赠券REWARDINFO:【门店追加64张赠券】处理成功");
            } else {
                log.info("门店追加64张赠券REWARDERROR:【门店追加64张赠券】处理失败");
            }

        } catch (Exception e) {
            log.info("门店追加64张赠券REWARDERROR:ConponAppendThread", e);
        } finally {
            countDownLatch.countDown();
        }
    }


}

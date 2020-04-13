package com.world.task.shop.work;

import com.alibaba.fastjson.JSON;
import com.world.data.mysql.Bean;
import com.world.data.mysql.Data;
import com.world.model.dao.task.Worker;
import com.world.model.qly.CbmMagUserExt;
import com.world.model.qly.EsMember;
import com.world.model.shop.MemberCouponModel;
import com.world.model.shop.ScanBatchRecordDetailModel;
import com.world.task.shop.thread.ScanInShopThread;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class QueryBusiShopOrderWork extends Worker {

    private static final long serialVersionUID = 1L;
    /*查询SQL*/
    private String sql = "";

    /*此轮定时任务结束标识*/
    private static boolean workFlag = true;

    public QueryBusiShopOrderWork(String name, String des) {
        super(name, des);
    }

    @Override
    public void run() {
        /*记录核算开始时间*/
        long startTime = System.currentTimeMillis();
        /*现在时间获取*/
        log.info("扫码入库报警REWARDINFO:【扫码入库处理】开始");

        if (workFlag) {
            workFlag = false;
            try {
                /**
                 *1、森麒麟每个业务员下都多少家门店，这些门店的订单数量、抢购轮胎数量
                 */
                sql = "select user_id, nickname from cbm_mag_user_ext " +
                        "where user_id in (select user_id from cbm_mag_l_user_role where role_id = '921a5417-a374-44e2-a617-396e77959ff7' )";
                log.info("sql = " + sql);
                List<Bean> cbmMagUserExtList = (List<Bean>) Data.Query("weilai_uaa", sql, null, CbmMagUserExt.class);
                log.info("cbmMagUserExtList = " + JSON.toJSONString(cbmMagUserExtList));
                log.info("cbmMagUserExtList = " + cbmMagUserExtList.size());
//
                if (null != cbmMagUserExtList && cbmMagUserExtList.size() > 0) {
                    CbmMagUserExt cbmMagUserExt = null;

                    String userId = "";
                    String userName = "";
                    String inviteUserIds = "";
                    int inviteUserCount = 0;
                    long orderNum = 0;
                    long goodsNum = 0;
                    for (int i = 0; i < cbmMagUserExtList.size(); i++) {
                        inviteUserIds = "";
                        cbmMagUserExt = (CbmMagUserExt) cbmMagUserExtList.get(i);

                        userId = cbmMagUserExt.getUser_id();
                        userName = cbmMagUserExt.getNickname();

                        sql = "select member_id from es_member where invite_user_id = '" + userId + "' ";
                        List<Bean> esMemberList = (List<Bean>) Data.Query("shop_member", sql, null, EsMember.class);
//                        log.info("esMemberList.size = " + esMemberList.size());

                        if (null != esMemberList && esMemberList.size() > 0) {
                            inviteUserCount = esMemberList.size();
                            EsMember esMember = null;
                            for (int e = 0; e < esMemberList.size(); e++) {
                                esMember = (EsMember) esMemberList.get(e);
                                inviteUserIds += esMember.getMember_id() + ",";
                            }
                            if (inviteUserIds.length() > 1) {
                                inviteUserIds = inviteUserIds.substring(0, inviteUserIds.length() - 1);
                            }
//                            log.info("inviteUserIds = " + inviteUserIds);

                            sql = "select count(fa.order_id) orderNum, cast(sum(fb.num) as signed) goodsNum from es_order fa, es_order_items fb " +
                                    "where fa.sn = fb.order_sn and fa.pay_status = 'PAY_YES' and fa.member_id in (" + inviteUserIds + ") ";
                            log.info("sql = " + sql);
                            EsMember esMemberOrder = (EsMember) Data.GetOne("shop_trade", sql, null, EsMember.class);
//                            log.info("esMemberOrder = " + JSON.toJSONString(esMemberOrder));
                            orderNum = esMemberOrder.getOrderNum();
                            goodsNum = esMemberOrder.getGoodsNum();

                            log.info("userName = " + userName + ", inviteUserCount = " + inviteUserCount + ", orderNum = " + orderNum + ", goodsNum = " + goodsNum);
                        }


                    }

                }

                long endTime = System.currentTimeMillis();
                log.info("扫码入库报警REWARDTASK:【扫码入库处理】结束!!!【核算耗时：" + (endTime - startTime) + "】");
            } catch (Exception e) {
                log.info("扫码入库报警REWARDERROR:ScanInDealerWork", e);
            } finally {
                workFlag = true;
            }
        } else {
            log.info("扫码入库报警REWARDTASK:【扫码入库处理】上一轮任务尚未结束，本轮不需要运行");
        }
    }

    public static void main(String[] args) {
        QueryBusiShopOrderWork queryBusiShopOrderWork = new QueryBusiShopOrderWork("", "");
        for (int i = 0; i < 1; i++) {
            queryBusiShopOrderWork.run();
        }
    }
}

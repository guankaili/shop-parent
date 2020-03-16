package com.world.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
	* @author cuihuofei
	* @date   2019年12月26日 上午10:29:15
	* @version v1.0.0
	* @Description
	* OrderStatus.java 
	* Modification History:
	* Date                         Author          Version          Description
	---------------------------------------------------------------------------------*
	* 2019年12月26日 上午10:29:15       cuihuofei        v1.0.0           Created
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatus {
	/**
	 * 已保存未提交（麒麟云经销商）：0。保存后显示“未提交”
	 * 1.已提交。（麒麟云经销商）操作状态从0变成1.操作后显示“已提交”
	 * 2.K3如果审核了，返回1. 用1来更新，更新成2.2转（麒麟云经销商）付款。 K3审核确认之前，经销商是否已付款？。目前K3没有退回。
	 * 3.待付款（麒麟云经销商点击付款）：3转（K3销售确认已付款）。 此项预留
	 * 4.已确认付款（K3销售确认已付款）：4。
	 * 5.销售出库单有对应的K3编号，更新成5.备货中（K3）：5。
	 * 6.发货通知单有对应的K3编号，更新成6，配送中（K3）：6。
	 *,7.前置状态是6，麒麟云经销商操作。已签收（麒麟云）：7。 有未结清的，结清状态。
	 * 8.销售发票通知单有对应的K3编号，更新成6，财务已出票8。
	 * 最终状态8，订单已完成。
	 */
	CREATE(-1, "创建"),
    SEVED(0, "未提交"),
    SUBMIT(1, "已提交"),
    VERIFY(2, "已审核"),
//    WAITPAY(3, "待付款"),
    PAY(4, "已付款"),
    STOCK(5, "备货中"),
    DELIVERY(6, "配送中"),
    SIGN(7, "已签收"),
    TICKETING(8, "已出票"),
    ;

    /**
     * 代码
     */
    private final int code;

    /**
     * 内容
     */
    private final String msg;

    /**
     * @param code 代码
     * @param msg  内容
     */
    OrderStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据代码获取枚举
     * 不要尝试缓存全部的枚举，该方法用到的频率不会太高，且枚举很少，不会造成资源浪费
     *
     * 使用 @JsonCreator 让 jackson 解析 json 的时候能匹配到该枚举
     * 参考：https://segmentfault.com/q/1010000020636087
     * @param code
     * @return
     */
    @JsonCreator
    public static OrderStatus getByCode(int code) {

        OrderStatus[] values = OrderStatus.values();

        for (OrderStatus value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
		return null;
    }
}

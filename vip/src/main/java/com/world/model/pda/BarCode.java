package com.world.model.pda;

import java.io.Serializable;
import java.util.Date;

/**
 * 物料条码表
 *
 * @author zhouqi
 * @date 2019/12/19 13:21
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019/12/19 13:21     zhouqi          v1.0.0           Created
 *
 */
public class BarCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户编码
     */
    private String custNo;

    /**
     * 销售订单
     */
    private String salesNo;

    /**
     * 发货通知
     */
    private String deliverNo;

    /**
     * 销售出库
     */
    private String outNo;

    /**
     * 趾口编码
     */
    private String barCode;

    /**
     * 物料编码
     */
    private String number;

    /**
     * 物料名称
     */
    private String name;

    /**
     * 标记
     * 默认为 0 , 若已经拉取到本地库 , 将其 修改为 1
     */
    private Integer flag;

    /**
     * 传入时间
     */
    private Date writeTime;

    /**
     * 接收时间
     */
    private Date readTime;

    private String docTypeId;

    private Date scaningDate;

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Date getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }

    public Date getScaningDate() {
        return scaningDate;
    }

    public void setScaningDate(Date scaningDate) {
        this.scaningDate = scaningDate;
    }
}

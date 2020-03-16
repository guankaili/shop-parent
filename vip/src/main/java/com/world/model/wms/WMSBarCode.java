package com.world.model.wms;

import java.util.Date;

import com.world.data.mysql.Bean;

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
public class WMSBarCode extends Bean {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
    private int id;
	
	/**
	 * 客户编码
	 */
    private String FCUSTNO;

    /**
     * 销售订单
     */
    private String FSALESNO;

    /**
     * 发货通知
     */
    private String FDELIVERNO;

    /**
     * 销售出库
     */
    private String FOUTNO;

    /**
     * 趾口编码
     */
    private String FBARCODE;

    /**
     * 物料编码
     */
    private String FNUMBER;

    /**
     * 物料名称
     */
    private String FNAME;

    /**
     * 标记
     * 默认为 0 , 若已经拉取到本地库 , 将其 修改为 1
     */
    private Integer FLAG;

    /**
     * 传入时间
     */
    private Date FWRITETIME;

    /**
     * 接收时间
     */
    private Date FREADTIME;

    private String FDOCTYPEID;

    private Date Scaningdate;


    public String getFCUSTNO() {
        return FCUSTNO;
    }

    public void setFCUSTNO(String FCUSTNO) {
        this.FCUSTNO = FCUSTNO;
    }

    public String getFSALESNO() {
        return FSALESNO;
    }

    public void setFSALESNO(String FSALESNO) {
        this.FSALESNO = FSALESNO;
    }

    public String getFDELIVERNO() {
        return FDELIVERNO;
    }

    public void setFDELIVERNO(String FDELIVERNO) {
        this.FDELIVERNO = FDELIVERNO;
    }

    public String getFOUTNO() {
        return FOUTNO;
    }

    public void setFOUTNO(String FOUTNO) {
        this.FOUTNO = FOUTNO;
    }

    public String getFBARCODE() {
        return FBARCODE;
    }

    public void setFBARCODE(String FBARCODE) {
        this.FBARCODE = FBARCODE;
    }

    public String getFNUMBER() {
        return FNUMBER;
    }

    public void setFNUMBER(String FNUMBER) {
        this.FNUMBER = FNUMBER;
    }

    public String getFNAME() {
        return FNAME;
    }

    public void setFNAME(String FNAME) {
        this.FNAME = FNAME;
    }

    public Integer getFLAG() {
        return FLAG;
    }

    public void setFLAG(Integer FLAG) {
        this.FLAG = FLAG;
    }

    public Date getFWRITETIME() {
        return FWRITETIME;
    }

    public void setFWRITETIME(Date FWRITETIME) {
        this.FWRITETIME = FWRITETIME;
    }

    public Date getFREADTIME() {
        return FREADTIME;
    }

    public void setFREADTIME(Date FREADTIME) {
        this.FREADTIME = FREADTIME;
    }

    public String getFDOCTYPEID() {
        return FDOCTYPEID;
    }

    public void setFDOCTYPEID(String FDOCTYPEID) {
        this.FDOCTYPEID = FDOCTYPEID;
    }

    public Date getScaningdate() {
        return Scaningdate;
    }

    public void setScaningdate(Date scaningdate) {
        Scaningdate = scaningdate;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    
}

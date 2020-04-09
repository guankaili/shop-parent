package com.world.model.shop;

import com.world.data.mysql.Bean;
import java.util.Date;

public class ScanTaskMModel extends Bean {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private int id;

    /** 定时任务类名称 */
    private String task_name;

    /** 定时任务描述 */
    private String task_desc;

    /** 开启标志默认0不开启；1-开启； */
    private int open_flag;

    /** 执行时间 */
    private Date execute_datetime;

    /** 计算月份格式：2020-03 */
    private String cal_month;

    /** 执行标志默认0；1-执行成功；2-执行失败 */
    private int cal_flag;

    /** 执行结果备注 */
    private String cal_msg;

    /** 最近执行时间 */
    private Date cal_datetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_desc() {
        return task_desc;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    public int getOpen_flag() {
        return open_flag;
    }

    public void setOpen_flag(int open_flag) {
        this.open_flag = open_flag;
    }

    public Date getExecute_datetime() {
        return execute_datetime;
    }

    public void setExecute_datetime(Date execute_datetime) {
        this.execute_datetime = execute_datetime;
    }

    public String getCal_month() {
        return cal_month;
    }

    public void setCal_month(String cal_month) {
        this.cal_month = cal_month;
    }

    public int getCal_flag() {
        return cal_flag;
    }

    public void setCal_flag(int cal_flag) {
        this.cal_flag = cal_flag;
    }

    public String getCal_msg() {
        return cal_msg;
    }

    public void setCal_msg(String cal_msg) {
        this.cal_msg = cal_msg;
    }

    public Date getCal_datetime() {
        return cal_datetime;
    }

    public void setCal_datetime(Date cal_datetime) {
        this.cal_datetime = cal_datetime;
    }
}

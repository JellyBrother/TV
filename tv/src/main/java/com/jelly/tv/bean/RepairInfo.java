package com.jelly.tv.bean;

/**
 * 获取维修进度
 */
public class RepairInfo{
    public String repairStatus;//工单状态
    public String tatStartTime;//受理时间（受理时间-当x前时间）
    public String light;//红色：R；绿色：G；黄色：Y
    public String queueingNo;//排队号
    public String repairNo;//工单状态描述
    public long orderby;//排队字段
    public boolean isRepair = false;//是否有维修信息，true显示默认图片
    public int repairStatusNum;//工单状态，根据repairStatus设置

    @Override
    public String toString() {
        return "RepairInfo{" +
                "repairStatus='" + repairStatus + '\'' +
                ", tatStartTime='" + tatStartTime + '\'' +
                ", light='" + light + '\'' +
                ", queueingNo='" + queueingNo + '\'' +
                ", repairNo='" + repairNo + '\'' +
                ", orderby=" + orderby +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepairInfo that = (RepairInfo) o;

        if (orderby != that.orderby) return false;
        if (!repairStatus.equals(that.repairStatus)) return false;
        if (!tatStartTime.equals(that.tatStartTime)) return false;
        if (!light.equals(that.light)) return false;
        if (!queueingNo.equals(that.queueingNo)) return false;
        return repairNo.equals(that.repairNo);

    }

    @Override
    public int hashCode() {
        int result = repairStatus.hashCode();
        result = 31 * result + tatStartTime.hashCode();
        result = 31 * result + light.hashCode();
        result = 31 * result + queueingNo.hashCode();
        result = 31 * result + repairNo.hashCode();
        result = 31 * result + (int) (orderby ^ (orderby >>> 32));
        return result;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    public String getTatStartTime() {
        return tatStartTime;
    }

    public void setTatStartTime(String tatStartTime) {
        this.tatStartTime = tatStartTime;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getQueueingNo() {
        return queueingNo;
    }

    public void setQueueingNo(String queueingNo) {
        this.queueingNo = queueingNo;
    }

    public String getRepairNo() {
        return repairNo;
    }

    public void setRepairNo(String repairNo) {
        this.repairNo = repairNo;
    }

    public long getOrderby() {
        return orderby;
    }

    public void setOrderby(long orderby) {
        this.orderby = orderby;
    }

    public boolean isRepair() {
        return isRepair;
    }

    public void setRepair(boolean repair) {
        isRepair = repair;
    }

    public int getRepairStatusNum() {
        return repairStatusNum;
    }

    public void setRepairStatusNum(int repairStatusNum) {
        this.repairStatusNum = repairStatusNum;
    }
}

package com.to8to.tbt.msc.entity;

/**
 * @author juntao.guo
 */
public class MultixMt {
    /**
     * 这写里可以填扩展号或*号。填*号时表示不扩展。填写扩展号时要注意扩展号位数不超过6位
     */
    private String sourceAddress = "*";

    /**
     * 11位手机号码手机号码规则：如果只发送中国的号码，号码前面无需加国际电话区号(0086)；如果发送的号码当中既有中国也有国外，那么号码规则必须是：00+国家电话区号+手机     * 号码。如：香港号码56455811,中国号码13265661400，那么发送时应该填0085256455811,008613265661400
     */
    private String phone;

    /**
     * 信息内容由GBK编码再转换成base64编码。
     */
    private String msgContent;

    /**
     * 业务类型,最大长度32字节,必须是字母、数字、字母数字混合 (可选)
     */
    private String serviceType = "|";

    /**
     * 自定义参数1
     */
    private String param1 = "|";

    /**
     * 自定义参数2
     */
    private String param2 = "|";

    /**
     * 自定义参数3
     */
    private String param3 = "|";

    /**
     * 自定义参数4
     */
    private String param4 = "|";

    /**
     * 自定义流水号,一个8字节64位的大整型（INT64），格式化成的字符串。因此该字段必须为纯数字，且范围不能超过INT64的取值范围（-(2^63）……2^63-1）
     */
    private String serialNumber = "|";

    /**
     * 模块ID，4字节整型正数（0~2^31-1），0视为无效值，请填写非0值（可选）
     */
    private String moduleId = "|";

    /**
     * 短信定时发送时间（可选）。此处格式为：yyyyMMddHHmmss，即年月日时分秒，14位，24小时制计时。例如2012年12月31日16时59分0秒，即为20121231165900。小于或等于    * 0或无该字段时视为不需定时。定时时间不能小于当前时间。
     */
    private String timingTime = "|";

    /**
     * 短信有效存活时间（可选）。此处格式为：yyyyMMddHHmmss，即年月日时分秒，14位，24小时制计时。例如2012年12月31日16时59分0秒，即为20121231165900小于或等于     * 0或无该字段时按系统默认有效期（48小时）执行。存活时间不能小于当前时间。
     */
    private String aliveTime = "|";

    /**
     * 是否需要状态报告（可选）。0:表示不需要，非0:表示需要。该值若不填，系统默认为需要状态报告
     */
    private int needStatusReport = 1;

    public String getSourceAddress()
    {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress)
    {
        this.sourceAddress = sourceAddress;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getMsgContent()
    {
        return msgContent;
    }

    public void setMsgContent(String msgContent)
    {
        this.msgContent = msgContent;
    }

    public String getServiceType()
    {
        return serviceType;
    }

    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }

    public String getParam1()
    {
        return param1;
    }

    public void setParam1(String param1)
    {
        this.param1 = param1;
    }

    public String getParam2()
    {
        return param2;
    }

    public void setParam2(String param2)
    {
        this.param2 = param2;
    }

    public String getParam3()
    {
        return param3;
    }

    public void setParam3(String param3)
    {
        this.param3 = param3;
    }

    public String getParam4()
    {
        return param4;
    }

    public void setParam4(String param4)
    {
        this.param4 = param4;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getModuleId()
    {
        return moduleId;
    }

    public void setModuleId(String moduleId)
    {
        this.moduleId = moduleId;
    }

    public String getTimingTime()
    {
        return timingTime;
    }

    public void setTimingTime(String timingTime)
    {
        this.timingTime = timingTime;
    }

    public String getAliveTime()
    {
        return aliveTime;
    }

    public void setAliveTime(String aliveTime)
    {
        this.aliveTime = aliveTime;
    }

    public int getNeedStatusReport()
    {
        return needStatusReport;
    }

    public void setNeedStatusReport(int needStatusReport)
    {
        this.needStatusReport = needStatusReport;
    }

    @Override
    public String toString()
    {
        return "multixmt="
                + sourceAddress + '|'
                + phone + '|'
                + msgContent + '|'
                + serviceType
                + param1
                + param2
                + param3
                + param4
                + serialNumber
                + moduleId
                + timingTime
                + aliveTime
                + needStatusReport;
    }

    public String getMultiXmt()
    {
        return  sourceAddress + '|'
                + phone + '|'
                + msgContent + '|'
                + serviceType
                + param1
                + param2
                + param3
                + param4
                + serialNumber
                + moduleId
                + timingTime
                + aliveTime
                + needStatusReport;
    }
}

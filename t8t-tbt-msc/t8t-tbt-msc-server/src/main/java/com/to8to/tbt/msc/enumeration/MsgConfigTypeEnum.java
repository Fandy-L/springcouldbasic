package com.to8to.tbt.msc.enumeration;

/**
 * @author juntao.guo
 */
public enum MsgConfigTypeEnum {
    /**
     * 节点类型:1.业务类型 2.业务项 3.发送节点 4.接收对象 5.产品模块
     */
    BUSINESS_TYPE(1), BUSINESS_ITEM(2), SEND_NODE(3), RECEIVE_OBJECT(5), PRODUCT_MODULE(6);

    private int code;

    MsgConfigTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

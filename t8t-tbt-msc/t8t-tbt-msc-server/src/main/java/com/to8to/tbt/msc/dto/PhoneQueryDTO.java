package com.to8to.tbt.msc.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.to8to.tbt.msc.entity.CodecWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneQueryDTO {
    /**
     * 手机号ID列表
     */
    @JSONField(name = "encryarray")
    private List<CodecWrapper.PhoneItem> phoneIdList;

    /**
     * 密钥
     */
    private String token;
}

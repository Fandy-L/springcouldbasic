package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
public class GroupNoteWrapper {

    @Data
    @Valid
    @NoArgsConstructor
    @ApiModel(value = "新增群发短信模板")
    public static class CreateVO{

    }
}

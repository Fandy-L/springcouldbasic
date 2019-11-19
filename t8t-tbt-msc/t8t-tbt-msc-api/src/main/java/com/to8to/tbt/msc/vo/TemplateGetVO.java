package com.to8to.tbt.msc.vo;

import com.to8to.tbt.msc.entity.template.TemplateInfo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "获取单个短信及APP模板结果集")
public class TemplateGetVO<T> {
    private TemplateInfo<T> result;
}

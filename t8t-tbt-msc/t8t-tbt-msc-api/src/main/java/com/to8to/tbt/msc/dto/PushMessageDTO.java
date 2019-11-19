package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author edmund.yu
 */
@Data
public class PushMessageDTO {

    @ApiModelProperty(value = "标题",example = "标题",required = true)
    @NotBlank
    @Length(max = 20)
    private String title;

    @ApiModelProperty(value = "内容",example = "内容",required = true)
    @NotBlank
    @Length(max = 50)
    private String content;

    @ApiModelProperty(value = "推送目标设备唯一标识",required = true)
    @NotEmpty
    @Size(max = 200)
    private List<String> audiences;

    @ApiModelProperty(value = "自定义消息Id",required = true)
    @NotBlank
    private String msgId;

    @ApiModelProperty(value = "定义点击后动作，打开应用-0，打开应用内页-1，打开url-2",example = "2")
    @Range(min = 0,max = 2)
    private Integer clickType;

    @ApiModelProperty(value = "应用内页")
    private String intent;

    @ApiModelProperty(value = "url地址")
    private String url;

    @ApiModelProperty(value = "推送类型，单推-1，群推-2")
    private Byte scope;
}

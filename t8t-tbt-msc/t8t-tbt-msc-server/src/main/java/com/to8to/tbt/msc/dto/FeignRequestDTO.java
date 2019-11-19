package com.to8to.tbt.msc.dto;

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
public class FeignRequestDTO<T> {
    /**
     * 请求参数体
     */
    private T args;
}

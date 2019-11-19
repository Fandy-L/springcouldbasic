package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.ConfigureCreateDTO;
import com.to8to.tbt.msc.dto.ConfigureDeleteDTO;
import com.to8to.tbt.msc.dto.ConfigureSearchDTO;
import com.to8to.tbt.msc.dto.ConfigureUpdateDTO;
import com.to8to.tbt.msc.export.ConfigureApi;
import com.to8to.tbt.msc.service.ConfigureService;
import com.to8to.tbt.msc.vo.ConfigureSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author juntao.guo
 */
@RestController
public class ConfigureController implements ConfigureApi {

    @Autowired
    private ConfigureService configureService;

    @Override
    public ResResult createConfiguration(@RequestBody List<ConfigureCreateDTO> params) {
        int cid = configureService.createConfiguration(params.get(0));
        return ResUtils.suc(String.valueOf(cid));
    }

    @Override
    public ResResult updateConfiguration(@RequestBody List<ConfigureUpdateDTO> params) {
        return configureService.updateConfiguration(params.get(0));
    }

    @Override
    public ResResult deleteConfiguration(@RequestBody List<ConfigureDeleteDTO> params) {
        return configureService.deleteConfiguration(params.get(0));
    }

    @Override
    public ResResult<List<ConfigureSearchVO>> searchConfiguration(@RequestBody List<ConfigureSearchDTO> params) {
        return ResUtils.data(configureService.searchConfiguration(params.get(0)));
    }
}

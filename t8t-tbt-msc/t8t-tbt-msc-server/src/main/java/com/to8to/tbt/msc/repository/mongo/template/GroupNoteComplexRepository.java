package com.to8to.tbt.msc.repository.mongo.template;

import com.to8to.tbt.msc.dto.GroupNoteSearchDTO;
import com.to8to.tbt.msc.vo.GroupNoteSearchVO;

/**
 * @author juntao.guo
 */
public interface GroupNoteComplexRepository {

    /**
     * 搜索群发短信模板
     *
     * @param groupNoteSearchDTO
     * @return
     */
    GroupNoteSearchVO searchGroupNote(GroupNoteSearchDTO groupNoteSearchDTO);
}

package com.to8to.tbt.msc.service;

import com.alibaba.fastjson.JSONArray;
import com.to8to.tbt.msc.BaseApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@RunWith(value = SpringRunner.class)
public class NoteSyncServiceTest extends BaseApplication {

    @Autowired
    private NoteSyncService noteSyncService;

    @Test
    public void syncAppRecord() {
        JSONArray data = JSONArray.parseArray("[\"msg_center\",\"msgc_app_record\",\"INSERT\",1571896682000,[{\"is_read\":[\"0\",\"tinyint(1) unsigned\",true],\"uid\":[\"11283096\",\"int(10) unsigned\",true],\"send_time\":[\"1571896682\",\"int(10) unsigned\",true],\"create_time\":[\"1571896682\",\"int(10) unsigned\",true],\"sender\":[\"php\",\"varchar(50)\",true],\"send_status\":[\"1\",\"tinyint(1) unsigned\",true],\"id\":[\"118736\",\"int(10) unsigned\",true],\"app_content\":[\"赞了你的回答\",\"varchar(500)\",true],\"biz_param\":[\"{\\\"triggerAccountId\\\":0,\\\"moduleCode\\\":\\\"ugcAnswer\\\",\\\"scheme\\\":\\\"\\\",\\\"schemeUrl\\\":\\\"\\\",\\\"remark\\\":\\\"\\\",\\\"subUid\\\":0,\\\"triggerUserType\\\":0,\\\"type\\\":13,\\\"title\\\":\\\"\\\",\\\"originContentPic\\\":\\\"\\\",\\\"url\\\":\\\"https://mapp.to8to.com/answer/info/55050\\\",\\\"content\\\":\\\"\\\",\\\"extraDataParams\\\":{\\\"cover\\\":\\\"empty\\\"},\\\"triggerUid\\\":\\\"172174517\\\",\\\"originContentDesc\\\":\\\"哦红米磨破上YY是也破也我\\\",\\\"objectId\\\":\\\"55050\\\"}\",\"varchar(1500)\",true],\"tid\":[\"986\",\"int(10) unsigned\",true]}],1571896682824]");
        noteSyncService.execute(data);
    }

    @Test
    public void syncMessageRecord() {
        JSONArray data = JSONArray.parseArray("[\"msg_center\",\"msgc_message_record\",\"INSERT\",1571902048000,[{\"send_time\":[\"1571902048\",\"int(11)\",true],\"msg_content\":[\"您正在进行短信验证，验证码为:131428，请在1分钟内输入验证码。\",\"varchar(300)\",true],\"phoneid\":[\"8547034\",\"int(11)\",true],\"send_status\":[\"1\",\"tinyint(2)\",true],\"error_code\":[\"0\",\"smallint(11)\",true],\"id\":[\"121559\",\"int(11)\",true],\"tid\":[\"686\",\"int(11)\",true]}],1571902048557]");
        noteSyncService.execute(data);
    }

    @Test
    public void syncUpdateAppRecord() {
        JSONArray data = JSONArray.parseArray("[\"msg_center\",\"msgc_app_record\",\"UPDATE\",1571988937000,[{\"is_read\":[\"1\",\"tinyint(1) unsigned\",true,\"0\"],\"uid\":[\"1676660\",\"int(10) unsigned\",false],\"send_time\":[\"1571910155\",\"int(10) unsigned\",false],\"create_time\":[\"1571910155\",\"int(10) unsigned\",false],\"sender\":[\"com.diary\",\"varchar(50)\",false],\"send_status\":[\"1\",\"tinyint(1) unsigned\",false],\"id\":[\"118817\",\"int(10) unsigned\",false],\"app_content\":[\"您的日记本《6万穷装89平温馨家》被删除！\",\"varchar(500)\",false],\"biz_param\":[\"{\\\"scheme\\\":\\\"to8to://tbtrouter/common/webview?url=https://mapp.to8to.com/wap/t8tapp/diaryErrorMsg?diaryid=7000410&diarytype=diarybook\\\",\\\"schemeUrl\\\":\\\"to8to://tbtrouter/common/webview?url=https://mapp.to8to.com/wap/t8tapp/diaryErrorMsg?diaryid=7000410&diarytype=diarybook\\\",\\\"subUid\\\":0,\\\"triggerUserType\\\":0,\\\"type\\\":14,\\\"title\\\":\\\"审核结果通知\\\",\\\"triggerUid\\\":1676660,\\\"objectId\\\":7000410,\\\"extraDataParams\\\":{\\\"jumpType\\\":0,\\\"sceneId\\\":7000410,\\\"isTopNotice\\\":0}}\",\"varchar(1500)\",false],\"tid\":[\"1114\",\"int(10) unsigned\",false]}],1571988937866]");
        noteSyncService.execute(data);
    }
}

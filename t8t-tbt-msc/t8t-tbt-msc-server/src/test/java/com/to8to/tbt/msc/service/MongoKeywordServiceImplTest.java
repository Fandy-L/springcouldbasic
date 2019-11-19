package com.to8to.tbt.msc.service;

import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.entity.response.Response;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.mongo.MongoKeyword;
import com.to8to.tbt.msc.repository.mongo.KeywordRepository;
import com.to8to.tbt.msc.service.impl.KeywordServiceImpl;
import com.to8to.tbt.msc.vo.KeywordVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author edmund.yu
 */
public class MongoKeywordServiceImplTest extends BaseApplication {

    @Autowired
    private KeywordService keyWordService;

    @Mock
    private KeywordRepository keyWordRepository;

    @InjectMocks
    private KeywordService keywordServiceMock = new KeywordServiceImpl();

    @Before
    public void setUp(){
        MongoKeyword keyWord = new MongoKeyword();
        keyWord.setKeyword("倾听");
        List<MongoKeyword> list = new ArrayList<>();
        list.add(keyWord);
        when(keyWordRepository.findFirstByKeyword("FF")).thenReturn(null);
        when(keyWordRepository.findFirstByKeyword("AA")).thenReturn(keyWord);
        Pattern pattern = Pattern.compile("^.*"+""+".*$",Pattern.CASE_INSENSITIVE);
        when(keyWordRepository.findAllByKeywordRegexOrderByCreateTimeDesc(pattern)).thenReturn(list);
        when(keyWordRepository.findFirstById("1551")).thenReturn(keyWord);
        doThrow(new RuntimeException("Exception")).when(keyWordRepository).findFirstByKeyword("天下第一");
        doThrow(new RuntimeException("Exception")).when(keyWordRepository).deleteById("aa");
        doThrow(new RuntimeException("Exception")).when(keyWordRepository).findAll();
        Pattern pattern1 = Pattern.compile("^.*"+"any"+".*$",Pattern.CASE_INSENSITIVE);
        doThrow(new RuntimeException("Exception")).when(keyWordRepository).findAllByKeywordRegexOrderByCreateTimeDesc(pattern1);
        doThrow(new RuntimeException("Exception")).when(keyWordRepository).findFirstById("11");
    }

    @Test
    public void addKeyWords() {

        MsgCenterResponse response1 = keywordServiceMock.addKeywords("FF,AA");
        Assert.assertEquals(Response.SUCCESS, response1);

        try {
            keywordServiceMock.addKeywords("天下第一");
        }catch (RuntimeException e){
            Assert.assertEquals("Exception", e.getMessage());
        }

    }

    @Test
    public void deleteById(){

        MsgCenterResponse response = keywordServiceMock.deleteById("5d7f4844997ffe35741fbd2c");
        Assert.assertEquals(Response.SUCCESS, response);

    }

    @Test
    public void getKeyWordAll(){

        List<KeywordVO> tWordList = keyWordService.getKeywordAll();
        Assert.assertNotNull(tWordList.get(0));
        System.out.println(tWordList);

        List<KeywordVO> tWordList1 = keywordServiceMock.getKeywordAll();
        Assert.assertEquals(0, tWordList1.size());
    }

    @Test
    public void searchKeyWord(){

        List<KeywordVO> tWordList1 = keywordServiceMock.searchKeyword("any");
        Assert.assertEquals(0, tWordList1.size());

        List<KeywordVO> tWordList2 = keywordServiceMock.searchKeyword(null);
        Assert.assertEquals(0, tWordList2.size());
    }

    @Test
    public void updateKeyWord(){
        List<String> list1 = new ArrayList<>();
        list1.add("1551");
        list1.add("AA");
        try {
            keywordServiceMock.updateKeyword(list1);
        }catch (RPCException e){
            Assert.assertEquals(MyExceptionStatus.KEYWORD_IS_EXISTS, e.getStatus());
        }

        list1.set(1,"FF");
        try {
            keywordServiceMock.updateKeyword(list1);
        }catch (RuntimeException e){
            Assert.assertEquals("Exception", e.getMessage());
        }
        list1.set(0, "1552");
        MsgCenterResponse response2 = keywordServiceMock.updateKeyword(list1);
        Assert.assertEquals(Response.SUCCESS,response2);
    }

    @Test
    public void getWord(){

        KeywordVO word2 = keywordServiceMock.getWord("1551");
        Assert.assertEquals("倾听", word2.getKeyword());

        try {
            keywordServiceMock.getWord("11");
        }catch (RuntimeException e){
            Assert.assertEquals("Exception", e.getMessage());
        }
        }
}
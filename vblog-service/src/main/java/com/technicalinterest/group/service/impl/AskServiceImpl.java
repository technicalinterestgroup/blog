package com.technicalinterest.group.service.impl;

import com.github.pagehelper.PageHelper;
import com.technicalinterest.group.dao.Ask;
import com.technicalinterest.group.dto.AskDTO;
import com.technicalinterest.group.mapper.AskMapper;
import com.technicalinterest.group.mapper.ReplyMapper;
import com.technicalinterest.group.service.AskService;
import com.technicalinterest.group.service.Enum.ResultEnum;
import com.technicalinterest.group.service.UserService;
import com.technicalinterest.group.service.dto.AskDTOParam;
import com.technicalinterest.group.service.dto.PageBean;
import com.technicalinterest.group.service.dto.ReturnClass;
import com.technicalinterest.group.service.exception.VLogException;
import com.technicalinterest.group.service.util.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: AskServiceImpl
 * @Author: shuyu.wang
 * @Description:
 * @Date: 2020/3/31 16:28
 * @Version: 1.0
 */
@Service
@Slf4j
public class AskServiceImpl implements AskService {
    @Autowired
    private AskMapper askMapper;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private UserService userService;

    @Value("${submit_length}")
    private Integer ARTICLE_LENGTH;

    /**
     * 新增或编辑
     *
     * @param ask
     * @return
     */
    @Override
    public ReturnClass saveOrUpdateAsk(Ask ask) {
        String userName=userService.getUserNameByLoginToken();
        int flag=0;
        //文章摘要
        String summaryText = HtmlUtil.cleanHtmlTag(ask.getContentFormat());
        ask.setDescription(summaryText.length() > ARTICLE_LENGTH ? summaryText.substring(0, ARTICLE_LENGTH - 1) : summaryText);
        if (Objects.isNull(ask.getId())){
            ask.setState((short)0);
            ask.setUserName(userName);
            ask.setCreateTime(new Date());
            ask.setUpdateTime(new Date());
            ask.setIsDel((short)0);
            flag=askMapper.insertSelective(ask);
        }else {
            Ask askById = askMapper.getAskById(ask.getId());
            if (Objects.isNull(askById)){
                throw new VLogException(ResultEnum.NO_DATA);
            }
            if (!askById.getUserName().equals(ask.getUserName())){
                throw new VLogException(ResultEnum.NO_AUTH);
            }
            ask.setUpdateTime(new Date());
            flag=askMapper.update(ask);
        }
        if (flag>0){
            return ReturnClass.success(ask.getId());
        }
        return ReturnClass.fail();
    }

    @Override
    public ReturnClass<PageBean<com.technicalinterest.group.dto.AskDTO>> getAskPage(AskDTOParam askDTOParam) {
        Ask query=new Ask();
        BeanUtils.copyProperties(askDTOParam,query);
        Integer askListCount = askMapper.getAskListCount(query);
        if (askListCount<1){
            return ReturnClass.fail(ResultEnum.NO_DATA.getMsg());
        }
        PageHelper.startPage(askDTOParam.getCurrentPage(), askDTOParam.getPageSize());
        List<com.technicalinterest.group.dto.AskDTO> askList = askMapper.getAskList(query);
        PageBean<com.technicalinterest.group.dto.AskDTO> pageBean = new PageBean<>(askList, askDTOParam.getCurrentPage(), askDTOParam.getPageSize(), askListCount);
        return ReturnClass.success(pageBean);
    }

    @Override
    public ReturnClass<PageBean<com.technicalinterest.group.dto.AskDTO>> getAskPageByToken(AskDTOParam askDTOParam) {
        Ask query=new Ask();
        BeanUtils.copyProperties(askDTOParam,query);
        query.setUserName(userService.getUserNameByLoginToken());
        Integer askListCount = askMapper.getAskListCount(query);
        if (askListCount<1){
            return ReturnClass.fail(ResultEnum.NO_DATA.getMsg());
        }
        PageHelper.startPage(askDTOParam.getCurrentPage(), askDTOParam.getPageSize());
        List<com.technicalinterest.group.dto.AskDTO> askList = askMapper.getAskList(query);
        PageBean<com.technicalinterest.group.dto.AskDTO> pageBean = new PageBean<>(askList, askDTOParam.getCurrentPage(), askDTOParam.getPageSize(), askListCount);
        return ReturnClass.success(pageBean);
    }

    @Override
    public ReturnClass<AskDTO> getAskDetailById(Long id, String userName) {
        AskDTO askById = askMapper.getAskDTOById(id,userName);
        if (Objects.isNull(askById)){

            return ReturnClass.fail(ResultEnum.NO_DATA.getMsg());
        }
        return ReturnClass.success(askById);
    }

    @Override
    public ReturnClass<Ask> getAskDetailByToken(Long id) {
        String userNameByLoginToken = userService.getUserNameByLoginToken();
        Ask query=new Ask();
        query.setId(id);
        query.setUserName(userNameByLoginToken);
        Ask askById = askMapper.getAsk(query);
        if (Objects.isNull(askById)){
            return ReturnClass.fail(ResultEnum.NO_DATA.getMsg());
        }
        return ReturnClass.success(askById);
    }

    @Override
    public ReturnClass<String> updateState(Ask askDTO) {
        Ask askById = askMapper.getAskById(askDTO.getId());
        if (Objects.isNull(askById)){
            throw new VLogException(ResultEnum.NO_DATA);
        }
        if (!askById.getUserName().equals(askDTO.getUserName())){
            throw new VLogException(ResultEnum.NO_AUTH);
        }
        if (askById.getState()==1){
            throw new VLogException("已经采纳过答案，不能重复操作！");
        }
        int update = askMapper.update(askDTO);
        if (update>0){
            return ReturnClass.success();
        }
        return ReturnClass.fail();
    }

    @Override
    @Async("vblog")
    public void updateReadCount(Long id) {
        int i = askMapper.updateReadCount(id);
        if (i>0){
            log.info("阅读数增加成功");
        }else {
            log.error("阅读数增加失败,id={}",id);
        }
    }

    /**
     * 增加回答数
     *
     * @param id
     */
    @Override
    @Async("vblog")
    public void updateReplayCount(Long id) {
        askMapper.updateReplayCount(id);
    }

    @Override
    public ReturnClass<List<com.technicalinterest.group.dto.AskDTO>> getTopAskList(String userName,Integer type) {
        List<com.technicalinterest.group.dto.AskDTO> askTopList = askMapper.getAskTopList(userName,type, 5);
        return ReturnClass.success(askTopList);
    }
}

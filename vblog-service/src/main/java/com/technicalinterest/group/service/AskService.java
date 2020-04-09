package com.technicalinterest.group.service;

import com.technicalinterest.group.dao.Ask;
import com.technicalinterest.group.dto.AskDTO;
import com.technicalinterest.group.service.dto.AskDTOParam;
import com.technicalinterest.group.service.dto.PageBean;
import com.technicalinterest.group.service.dto.ReturnClass;

import java.util.List;

/**
 * @ClassName: AskService
 * @Author: shuyu.wang
 * @Description:
 * @Date: 2020/3/31 16:27
 * @Version: 1.0
 */
public interface AskService {
    /**
     * 新增或编辑
     * @param ask
     * @return
     */
    ReturnClass saveOrUpdateAsk(Ask ask);

    ReturnClass<PageBean<com.technicalinterest.group.dto.AskDTO>> getAskPage(AskDTOParam askDTOParam);

    ReturnClass<PageBean<com.technicalinterest.group.dto.AskDTO>> getAskPageByToken(AskDTOParam askDTOParam);

    ReturnClass<AskDTO> getAskDetailById(Long id, String userName);

    ReturnClass<Ask> getAskDetailByToken(Long id);

    ReturnClass<String> updateState(Ask askDTO);

    /**
     * 增加阅读数
     * @param id
     */
    void updateReadCount(Long id);

    /**
     * 增加回答数
     * @param id
     */
    void updateReplayCount(Long id);

    ReturnClass<List<com.technicalinterest.group.dto.AskDTO>> getTopAskList(String userName,Integer type);

}

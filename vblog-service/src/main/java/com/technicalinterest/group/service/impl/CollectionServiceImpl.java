package com.technicalinterest.group.service.impl;

import com.github.pagehelper.PageHelper;
import com.technicalinterest.group.dao.Collection;
import com.technicalinterest.group.dao.PageBase;
import com.technicalinterest.group.dao.WebsiteNotice;
import com.technicalinterest.group.dto.ArticlesDTO;
import com.technicalinterest.group.dto.CollectionDTO;
import com.technicalinterest.group.mapper.ArticleMapper;
import com.technicalinterest.group.mapper.CollectionMapper;
import com.technicalinterest.group.mapper.WebsiteNoticeMapper;
import com.technicalinterest.group.service.CollectionService;
import com.technicalinterest.group.service.UserService;
import com.technicalinterest.group.service.constant.CollectionConstant;
import com.technicalinterest.group.service.Enum.ResultEnum;
import com.technicalinterest.group.service.dto.LikeDTO;
import com.technicalinterest.group.service.dto.PageBean;
import com.technicalinterest.group.service.dto.ReturnClass;
import com.technicalinterest.group.service.exception.VLogException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author wangshuyu
 */
@Service
public class CollectionServiceImpl implements CollectionService {
	@Autowired
	private CollectionMapper collectionMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private ArticleMapper articleMapper;
	@Autowired
	private WebsiteNoticeMapper websiteNoticeMapper;

	@Override
	public ReturnClass insert(LikeDTO likeDTO) {
		String userName= userService.getUserNameByLoginToken();
		WebsiteNotice websiteNotice=null;
		//博客
		if (likeDTO.getType()==1) {
			ArticlesDTO articleInfo = articleMapper.getArticleInfo(likeDTO.getSourceId(), null);
			if (Objects.isNull(articleInfo)) {
				throw new VLogException(ResultEnum.NO_URL);
			}
		//通知
	} else if (likeDTO.getType() == 3) {
		websiteNotice = websiteNoticeMapper.websiteNoticeById(likeDTO.getSourceId());
		if (Objects.isNull(websiteNotice)) {
			throw new VLogException(ResultEnum.NO_URL);
		}
	}
		Collection collectionPa = Collection.builder().userName(userName).type(likeDTO.getType()).sourceId(likeDTO.getSourceId()).build();
		Collection collection2 = collectionMapper.queryCollection(collectionPa);
		if (Objects.nonNull(collection2)) {
			return ReturnClass.success(CollectionConstant.ADD_REPAT);
		}
		int insert = collectionMapper.insertSelective(collectionPa);
		if (insert > 0) {
			return ReturnClass.success(CollectionConstant.SUS_ADD);
		}
		return ReturnClass.fail(CollectionConstant.FAIL_ADD);
	}

	@Override
	public ReturnClass del(LikeDTO likeDTO) {
		String userName= userService.getUserNameByLoginToken();
		Collection collectionPa = Collection.builder().userName(userName).type(likeDTO.getType()).sourceId(likeDTO.getSourceId()).build();
		Collection collection = collectionMapper.queryCollection(collectionPa);
		if (Objects.isNull(collection)) {
			throw new VLogException(ResultEnum.NO_URL);
		}
		if (!StringUtils.equals(userName, collection.getUserName())) {
			throw new VLogException(ResultEnum.NO_AUTH);
		}
		Integer integer = collectionMapper.delCollection(collection.getId());
		if (integer > 0) {
			return ReturnClass.success(CollectionConstant.SUS_DEL);
		}
		return ReturnClass.success(CollectionConstant.FAIL_DEL);
	}

	/**
	 * @Description:查询收藏列表
	 * @author: shuyu.wang
	 * @date: 2019/8/21 22:50
	 * @param
	 * @return com.technicalinterest.group.service.dto.ReturnClass
	 */
	@Override
	public ReturnClass queryListCollection(PageBase pageBase) {
		String userName = userService.getUserNameByLoginToken();
		Integer integer = collectionMapper.queryCountCollectionByUserName(userName);
		if (integer > 0) {
			PageHelper.startPage(pageBase.getCurrentPage(), pageBase.getPageSize());
			List<CollectionDTO> collectionDTOS = collectionMapper.queryListCollectionByUserName(userName);
			PageBean<CollectionDTO> pageBean = new PageBean<>(collectionDTOS, pageBase.getCurrentPage(), pageBase.getPageSize(), integer);
			return ReturnClass.success(pageBean);
		}
		return ReturnClass.fail(CollectionConstant.NO_DATA);
	}
}

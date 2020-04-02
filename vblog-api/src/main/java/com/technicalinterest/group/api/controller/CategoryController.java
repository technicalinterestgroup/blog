package com.technicalinterest.group.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.blackshadowwalker.spring.distributelock.annotation.DistributeLock;
import com.technicalinterest.group.api.param.EditCategoryParam;
import com.technicalinterest.group.api.param.NewCategoryParam;
import com.technicalinterest.group.api.vo.ApiResult;
import com.technicalinterest.group.api.vo.ArticleTitleVO;
import com.technicalinterest.group.api.vo.CategoryVO;
import com.technicalinterest.group.api.vo.TagVO;
import com.technicalinterest.group.dto.CategoryDTO;
import com.technicalinterest.group.dto.TagDTO;
import com.technicalinterest.group.service.CategoryService;
import com.technicalinterest.group.service.TagService;
import com.technicalinterest.group.service.annotation.BlogOperation;
import com.technicalinterest.group.service.dto.EditCategoryDTO;
import com.technicalinterest.group.service.dto.ReturnClass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @package: com.technicalinterest.group.api.controller
 * @className: CategoryController
 * @description: 博客分类controller
 * @author: Shuyu.Wang
 * @date: 2019-08-15 17:15
 * @since: 0.1
 **/
@Api(tags = "博客分类")
@RestController
@RequestMapping("category")
@Slf4j
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;


	/**
	 * @Description: 博客分类列表
	 * @author: shuyu.wang
	 * @date: 2019-08-15 17:39
	 * @return null
	 */
	@ApiOperation(value = "博客分类", notes = "博客分类")
	@GetMapping(value = "/list")
	@BlogOperation(value = "博客分类")
	public ApiResult<List<CategoryVO>> listCategory(@RequestParam(value = "name",required = false)String name) {
		ApiResult apiResult = new ApiResult();
		ReturnClass listCategory = categoryService.categoryListPage(name);
		if (listCategory.isSuccess()) {
			List<CategoryVO> list = new ArrayList<CategoryVO>();
			List<CategoryDTO> categoryDTOList = (List<CategoryDTO>) listCategory.getData();
			for (CategoryDTO entity : categoryDTOList) {
				CategoryVO categoryVO = new CategoryVO();
				BeanUtils.copyProperties(entity, categoryVO);
				list.add(categoryVO);
			}
			apiResult.success(list);
		} else {
			apiResult.setMsg(listCategory.getMsg());
		}
		return apiResult;
	}
	/**
	 * @Description: 博客分类列表
	 * @author: shuyu.wang
	 * @date: 2019-08-15 17:39
	 * @return null
	 */
	@ApiOperation(value = "博客分类下拉接口")
	@GetMapping(value = "/list/dic")
	@BlogOperation(value = "博客分类下拉接口")
	public ApiResult<List<CategoryVO>>categoryDic() {
		ApiResult apiResult = new ApiResult();
		ReturnClass listCategory = categoryService.listCategoryByUser();
		if (listCategory.isSuccess()) {
			List<CategoryVO> list = new ArrayList<CategoryVO>();
			List<CategoryDTO> categoryDTOList = (List<CategoryDTO>) listCategory.getData();
			for (CategoryDTO entity : categoryDTOList) {
				CategoryVO categoryVO = new CategoryVO();
				BeanUtils.copyProperties(entity, categoryVO);
				list.add(categoryVO);
			}
			apiResult.success(list);
		} else {
			apiResult.setMsg(listCategory.getMsg());
		}
		return apiResult;
	}
	/**
	 * @Description: 博客标签列表
	 * @author: shuyu.wang
	 * @date: 2019-08-15 17:39
	 * @return null
	 */
	@ApiOperation(value = "博客标签列表下拉选择")
	@GetMapping(value = "/tag/dic")
	@BlogOperation(value = "博客标签列表")
	public ApiResult<List<TagVO>> listTagDics() {
		ApiResult apiResult = new ApiResult();
		ReturnClass listCategory = tagService.allTagListDic();
		if (listCategory.isSuccess()) {
			List<TagVO> list = new ArrayList<TagVO>();
			List<TagDTO> tagDTOS = (List<TagDTO>) listCategory.getData();
			for (TagDTO entity : tagDTOS) {
				TagVO tagVO = new TagVO();
				BeanUtils.copyProperties(entity, tagVO);
				list.add(tagVO);
			}
			apiResult.success(list);
		} else {
			apiResult.setMsg(listCategory.getMsg());
		}
		return apiResult;
	}
	/**
	 * @Description: 编辑博客分类
	 * @author: shuyu.wang
	 * @date: 2019-08-15 17:39
	 * @param editCategoryParam
	 * @return null
	 */
	@ApiOperation(value = "编辑博客分类", notes = "更新")
	@PostMapping(value = "/edit")
	@BlogOperation(value = "编辑博客分类")
	@DistributeLock( key = "#editCategoryParam.id", timeout = 2, expire = 1, errMsg = "00000")
	public ApiResult<String> editCategory(@Valid @RequestBody EditCategoryParam editCategoryParam) {
		ApiResult apiResult = new ApiResult();
		EditCategoryDTO editCategoryDTO = new EditCategoryDTO();
		BeanUtils.copyProperties(editCategoryParam, editCategoryDTO);
		ReturnClass update = categoryService.update(editCategoryDTO);
		if (update.isSuccess()) {
			apiResult.success(update.getMsg(),null);
		} else {
			apiResult.setMsg(update.getMsg());
		}
		return apiResult;
	}

	/**
	 * @Description: 新增博客分类
	 * @author: shuyu.wang
	 * @date: 2019-08-15 17:39
	 * @param newCategoryParam
	 * @return null
	 */
	@ApiOperation(value = "新增博客分类", notes = "新增")
	@PostMapping(value = "/new")
	@BlogOperation(value = "新增博客分类")
//	@DistributeLock( key = "#newCategoryParam.name", timeout = 2, expire = 1, errMsg = "00000")
	public ApiResult<String> newCategory(@Valid @RequestBody NewCategoryParam newCategoryParam) {
		ApiResult apiResult = new ApiResult();
		EditCategoryDTO editCategoryDTO = new EditCategoryDTO();
		BeanUtils.copyProperties(newCategoryParam, editCategoryDTO);
		ReturnClass update = categoryService.insertSelective(editCategoryDTO);
		if (update.isSuccess()) {
			apiResult.success(update.getMsg(),null);
		} else {
			apiResult.setMsg(update.getMsg());
		}
		return apiResult;
	}

	/**
	 * @Description: 分类删除
	 * @author: shuyu.wang
	 * @date: 2019-08-16 23:18
	 * @param id
	 * @return null
	*/
	@ApiOperation(value = "博客分类删除", notes = "删除")
	@GetMapping(value = "/del/{id}")
	@BlogOperation(value = "博客分类删除")
	public ApiResult<String> delCategory(@PathVariable("id") Long id) {
		ApiResult apiResult = new ApiResult();
		ReturnClass delCategory = categoryService.delCategory(id);
		if (delCategory.isSuccess()) {
			apiResult.success(delCategory.getMsg(),null);
		} else {
			apiResult.fail(delCategory.getMsg());
		}
		return apiResult;
	}
}

package com.technicalinterest.group.dao;

import lombok.Data;

/**
 * @package: com.technicalinterest.group.dao
 * @className: Article
 * @description: 文章表
 * @author: Shuyu.Wang
 * @date: 2019-07-28 17:01
 * @since: 0.1
 **/
@Data
public class Article extends BaseDao {
	/** 
	* 标题
	*/ 
	private String title;
	/**
	 * 摘要
	 */
	private String submit;
	/**
	 * 是否置顶
	 */
	private Short isTop=0;
	/**
	 * 分类id
	 */
	private Long categoryId;
	/**
	 * 是否审核
	 */
	private Short isReview;
	/**
	 * 标签id
	 */
	private Long  tagId;
	
	/** 
	* 文章作者
	*/ 
	private String userName;
	/**
	 * 文章描述
	 */
	private String description;
	/**
	 * 文章状态 0：草稿，1：发布
	 */
	private Short state;
	/**
	 * 是否为系统推荐文章 0：不是，1：是
	 */
	private Short recommend;
	/**
	 * 文章状态阅量
	 */
	private Integer readCount;

	/**
	 * 文章点赞量
	 */
	private Integer likeCount;

	/**
	 * 文章评论量
	 */
	private Integer commentCount;






}

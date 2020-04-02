package com.technicalinterest.group.api.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


/**
 * @package: com.technicalinterest.group.service.dto
 * @className: ArticleContentDTO
 * @description: 新增文章
 * @author: Shuyu.Wang
 * @date: 2019-08-04 15:08
 * @since: 0.1
 **/
@Data
@ApiModel(description = "新增文章参数")
public class NewArticleContentParam {

	/**
	 * 标题
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 标题
	 */
	@ApiModelProperty(value = "文章标题")
	@NotBlank(message = "文章标题不能为空！")
	private String title;
	/**
	 * 是否置顶
	 */
	@ApiModelProperty(value = "是否置顶",allowableValues = "1:置顶,0:不置顶",example = "1")
	private Short isTop;
	/**
	 * 分类id
	 */
	@ApiModelProperty(value = "文章关联分类id")
	private Long categoryId;
	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "文章关联标签id")
	private Long  tagId;

	/**
	 * 文章内容html格式
	 */
	@ApiModelProperty(value = "文章内容html格式")
	@NotNull(message = "文章内容不能为空")
	private String contentFormat;

	/**
	 * 文章内容markdown格式
	 */
	@ApiModelProperty(value = "文章内容markdown格式")
	@NotNull(message = "文章内容不能为空")
	private String content;
	/**
	 * 文章状态 0：草稿，1：发布
	 */
	@ApiModelProperty(value = "文章状态",allowableValues = "0：草稿，1：发布")
	@NotNull(message = "文章状态不能为空")
	private Short state;

	@NotNull(message = "文章描述不能为空")
	private String description;

}

package com.technicalinterest.group.service;

import com.technicalinterest.group.service.dto.EditCategoryDTO;
import com.technicalinterest.group.service.dto.ReturnClass;


public interface CategoryService{



     ReturnClass insertSelective(EditCategoryDTO pojo);

     ReturnClass update(EditCategoryDTO pojo);

     ReturnClass listCategoryByUser(String userName);

     ReturnClass delCategory(Long id);

     ReturnClass listCategorySByUser();
}

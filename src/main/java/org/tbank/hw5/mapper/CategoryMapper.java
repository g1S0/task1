package org.tbank.hw5.mapper;

import org.mapstruct.Mapper;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    Category toCategory(CategoryDto categoryDto);

    List<CategoryDto> toCategoryDtoList(List<Category> categories);

    List<Category> toCategoryList(List<CategoryDto> categoryDtoList);
}

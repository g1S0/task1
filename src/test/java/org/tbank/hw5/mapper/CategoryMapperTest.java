package org.tbank.hw5.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.model.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryMapperTest {

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    public void testToCategoryDto() {
        Category category = new Category(1L, "slug1", "Category 1");

        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);

        assertNotNull(categoryDto);
        assertEquals(1L, categoryDto.getId());
        assertEquals("slug1", categoryDto.getSlug());
        assertEquals("Category 1", categoryDto.getName());
    }

    @Test
    public void testToCategory() {
        CategoryDto categoryDto = new CategoryDto(1L, "slug1", "Category 1");

        Category category = categoryMapper.toCategory(categoryDto);

        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("slug1", category.getSlug());
        assertEquals("Category 1", category.getName());
    }

    @Test
    public void testToCategoryDtoList() {
        List<Category> categories = List.of(
                new Category(1L, "slug1", "Category 1"),
                new Category(2L, "slug2", "Category 2")
        );

        List<CategoryDto> categoryDtoList = categoryMapper.toCategoryDtoList(categories);

        assertNotNull(categoryDtoList);
        assertEquals(2, categoryDtoList.size());
        assertEquals("Category 1", categoryDtoList.get(0).getName());
        assertEquals("Category 2", categoryDtoList.get(1).getName());
    }

    @Test
    public void testToCategoryList() {
        List<CategoryDto> categoryDtoList = List.of(
                new CategoryDto(1L, "slug1", "Category 1"),
                new CategoryDto(2L, "slug2", "Category 2")
        );

        List<Category> categoryList = categoryMapper.toCategoryList(categoryDtoList);

        assertNotNull(categoryList);
        assertEquals(2, categoryList.size());
        assertEquals("Category 1", categoryList.get(0).getName());
        assertEquals("Category 2", categoryList.get(1).getName());
    }
}
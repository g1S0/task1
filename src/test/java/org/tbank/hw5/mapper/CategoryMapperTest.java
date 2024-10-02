package org.tbank.hw5.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.model.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryMapperTest {

    private static final Long CATEGORY_ID_1 = 1L;
    private static final Long CATEGORY_ID_2 = 2L;
    private static final String CATEGORY_SLUG_1 = "airports";
    private static final String CATEGORY_SLUG_2 = "Airports";
    private static final String CATEGORY_NAME_1 = "amusement";
    private static final String CATEGORY_NAME_2 = "Amusement parks (Entertainment)";

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    private Category createCategory(Long id, String slug, String name) {
        return new Category(id, slug, name);
    }

    private CategoryDto createCategoryDto(Long id, String slug, String name) {
        return new CategoryDto(id, slug, name);
    }

    @Test
    public void testToCategoryDto() {
        Category category = createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);

        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);

        assertNotNull(categoryDto);
        assertEquals(CATEGORY_ID_1, categoryDto.getId());
        assertEquals(CATEGORY_SLUG_1, categoryDto.getSlug());
        assertEquals(CATEGORY_NAME_1, categoryDto.getName());
    }

    @Test
    public void testToCategory() {
        CategoryDto categoryDto = createCategoryDto(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);

        Category category = categoryMapper.toCategory(categoryDto);

        assertNotNull(category);
        assertEquals(CATEGORY_ID_1, category.getId());
        assertEquals(CATEGORY_SLUG_1, category.getSlug());
        assertEquals(CATEGORY_NAME_1, category.getName());
    }

    @Test
    public void testToCategoryDtoList() {
        List<Category> categories = List.of(
                createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1),
                createCategory(CATEGORY_ID_2, CATEGORY_SLUG_2, CATEGORY_NAME_2)
        );

        List<CategoryDto> categoryDtoList = categoryMapper.toCategoryDtoList(categories);

        assertNotNull(categoryDtoList);
        assertEquals(2, categoryDtoList.size());
        assertEquals(CATEGORY_NAME_1, categoryDtoList.get(0).getName());
        assertEquals(CATEGORY_NAME_2, categoryDtoList.get(1).getName());
    }

    @Test
    public void testToCategoryList() {
        List<CategoryDto> categoryDtoList = List.of(
                createCategoryDto(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1),
                createCategoryDto(CATEGORY_ID_2, CATEGORY_SLUG_2, CATEGORY_NAME_2)
        );

        List<Category> categoryList = categoryMapper.toCategoryList(categoryDtoList);

        assertNotNull(categoryList);
        assertEquals(2, categoryList.size());
        assertEquals(CATEGORY_NAME_1, categoryList.get(0).getName());
        assertEquals(CATEGORY_NAME_2, categoryList.get(1).getName());
    }
}
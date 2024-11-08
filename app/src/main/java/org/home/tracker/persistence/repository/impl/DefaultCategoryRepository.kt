package org.home.tracker.persistence.repository.impl

import org.home.tracker.dto.CategoryDto
import org.home.tracker.persistence.dao.CategoryDao
import org.home.tracker.persistence.entity.Category
import org.home.tracker.persistence.repository.CategoryRepository

class DefaultCategoryRepository(private val dao: CategoryDao) : CategoryRepository {

    override suspend fun findAll(): List<CategoryDto> {
        return dao.findAll().map { CategoryDto(it.id, it.name) }
    }

    override suspend fun save(category: CategoryDto): CategoryDto {
        val entity = Category(category.id, category.name)
        if (category.id == null) {
            val id = dao.insert(entity)
            return CategoryDto(id, category.name)
        }

        dao.update(entity)
        
        return CategoryDto(entity.id, entity.name)
    }
}
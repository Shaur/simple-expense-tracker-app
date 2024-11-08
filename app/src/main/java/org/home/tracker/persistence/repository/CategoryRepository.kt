package org.home.tracker.persistence.repository

import org.home.tracker.dto.CategoryDto

interface CategoryRepository {

    suspend fun findAll(): List<CategoryDto>

    suspend fun save(category: CategoryDto): CategoryDto

}
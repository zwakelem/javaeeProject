package com.library.app.category.services.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.library.app.category.exception.CategoryExistantException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesImpl implements CategoryServices {

	Validator validator;
	CategoryRepository categoryRepository;

	@Override
	public Category add(final Category category) throws FieldNotValidException {

		validateCategory(category);
		return categoryRepository.add(category);
	}

	private void validateCategory(final Category category) {
		validateCategoryFields(category);

		if (categoryRepository.alreadyExists(category)) {
			throw new CategoryExistantException();
		}
	}

	private void validateCategoryFields(final Category category) {
		final Set<ConstraintViolation<Category>> errors = validator.validate(category);
		final Iterator<ConstraintViolation<Category>> itErrors = errors.iterator();
		if (itErrors.hasNext()) {
			final ConstraintViolation<Category> violation = itErrors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}
	}

	@Override
	public void update(final Category category) throws FieldNotValidException {
		validateCategory(category);

		if (!categoryRepository.existsById(category.getId())) {
			throw new CategoryNotFoundException();
		}
		categoryRepository.update(category);

	}

	@Override
	public Category findById(final Long id) throws CategoryNotFoundException {
		final Category category = categoryRepository.findById(id);
		if (category == null) {
			throw new CategoryNotFoundException();
		}
		return category;
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll("name");
	}

}

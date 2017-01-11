package com.library.app.category.services.impl;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.exception.CategoryExistantException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesUTest {

	private CategoryServices categoryServices;
	private Validator val;
	private CategoryRepository categoryRepository;

	@Before
	public void initTestCase() {
		val = Validation.buildDefaultValidatorFactory().getValidator();

		categoryRepository = mock(CategoryRepository.class);

		categoryServices = new CategoryServicesImpl();
		((CategoryServicesImpl) categoryServices).validator = val;
		((CategoryServicesImpl) categoryServices).categoryRepository = categoryRepository;
	}

	@Test
	public void addCategoryWithNullName() {
		addCategoryWithInvalidName(null);
	}

	@Test
	public void addCategoryWithShortName() {
		addCategoryWithInvalidName("s");
	}

	@Test
	public void addCategoryWithLongName() {
		addCategoryWithInvalidName("Category with an unusually long name to throw exception");
	}

	@Test(expected = CategoryExistantException.class)
	public void addCategoryWithExistantName() {
		when(categoryRepository.alreadyExists(java())).thenReturn(true);
		categoryServices.add(java());
	}

	@Test
	public void addValidCategory() {
		when(categoryRepository.alreadyExists(java())).thenReturn(false);
		when(categoryRepository.add(java())).thenReturn(categoryWithId(java(), 1L));

		final Category categoryAdded = categoryServices.add(java());
		assertThat(categoryAdded.getId(), is(equalTo(1l)));
	}

	private void addCategoryWithInvalidName(final String name) {
		try {
			categoryServices.add(new Category(name));
			fail("Expecting an error");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

	@Test
	public void updateCategoryWithNullName() {
		updateCategoryWithInvalidName(null);
	}

	@Test
	public void updateCategoryWithShortName() {
		updateCategoryWithInvalidName("s");
	}

	@Test
	public void updateCategoryWithLongName() {
		updateCategoryWithInvalidName("Category with an unusually long name to throw exception");
	}

	@Test(expected = CategoryExistantException.class)
	public void updateCategoryWithExistantName() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1l))).thenReturn(true);

		categoryServices.update(categoryWithId(java(), 1l));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void updateCategoryNotFound() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1l))).thenReturn(false);
		when(categoryRepository.existsById(1l)).thenReturn(false);

		categoryServices.update(categoryWithId(java(), 1l));
	}

	@Test
	public void updateValidCategory() {
		when(categoryRepository.alreadyExists(categoryWithId(java(), 1l))).thenReturn(false);
		when(categoryRepository.existsById(1l)).thenReturn(true);

		categoryServices.update(categoryWithId(java(), 1l));

		verify(categoryRepository).update(categoryWithId(java(), 1l));
	}

	@Test
	public void findCategoryById() {
		when(categoryRepository.findById(1l)).thenReturn(categoryWithId(java(), 1l));

		final Category category = categoryServices.findById(1l);
		assertThat(category, is(notNullValue()));
		assertThat(category.getId(), is(equalTo(1l)));
		assertThat(category.getName(), is(equalTo(java().getName())));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByIdNotFound() {
		when(categoryRepository.findById(1l)).thenReturn(null);

		categoryServices.findById(1l);
	}

	@Test
	public void findAllNoCategories() {
		when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>());

		final List<Category> categories = categoryServices.findAll();
		assertThat(categories.isEmpty(), is(equalTo(true)));
	}

	@Test
	public void findAllCategories() {
		when(categoryRepository.findAll("name"))
				.thenReturn(Arrays.asList(categoryWithId(java(), 1l), categoryWithId(networks(), 2l)));

		final List<Category> categories = categoryServices.findAll();
		assertThat(categories.size(), is(equalTo(2)));
		assertThat(categories.get(0).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(networks().getName())));
	}

	private void updateCategoryWithInvalidName(final String name) {
		try {
			categoryServices.update(new Category(name));
			fail("An error should have been thrown");
		} catch (final FieldNotValidException e) {
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
	}

}

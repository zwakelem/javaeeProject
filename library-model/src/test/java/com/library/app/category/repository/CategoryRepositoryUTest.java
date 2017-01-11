package com.library.app.category.repository;

import static com.library.app.commontest.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.resource.spi.IllegalStateException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.commontests.utils.DBCommandTransactionalExecutor;

public class CategoryRepositoryUTest {

	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepository categoryRepository;
	private DBCommandTransactionalExecutor dbCommandTransactionalExecutor;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();

		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;

		dbCommandTransactionalExecutor = new DBCommandTransactionalExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addCategoryAndFindIt() throws IllegalStateException {
		final Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		final Category category = categoryRepository.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
		assertThat(categoryAddedId, is(notNullValue()));
	}

	@Test
	public void addCategoryByIdNotFound() {
		final Category category = categoryRepository.findById(999l);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void addCategoryByIdWithNullId() {
		final Category category = categoryRepository.findById(null);
		assertThat(category, is(nullValue()));
	}

	@Test
	public void updateCategory() throws IllegalStateException {
		final Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		final Category categoryAfterAdd = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));

		categoryAfterAdd.setName(cleanCode().getName());
		dbCommandTransactionalExecutor.executeCommand(() -> {
			categoryRepository.update(categoryAfterAdd);
			return null;
		});

		final Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
	}

	@Test
	public void findAllCategories() throws IllegalStateException {
		dbCommandTransactionalExecutor.executeCommand(() -> {
			allCategories().forEach(categoryRepository::add);
			return null;
		});
		final List<Category> categories = categoryRepository.findAll("name");
		assertThat(categories.size(), is(equalTo(4)));
		assertThat(categories.get(0).getName(), is(equalTo(architecture().getName())));
	}

	@Test
	public void alreadyExistsForAdd() throws IllegalStateException {
		dbCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(java());
		});
		assertThat(categoryRepository.alreadyExists(java()), is(equalTo(true)));
		assertThat(categoryRepository.alreadyExists(cleanCode()), is(equalTo(false)));
	}

	@Test
	public void existsById() throws IllegalStateException {
		final Long categoryAddedId = dbCommandTransactionalExecutor.executeCommand(() -> {
			return categoryRepository.add(java()).getId();
		});
		assertThat(categoryRepository.existsById(categoryAddedId), is(equalTo(true)));
		assertThat(categoryRepository.existsById(999l), is(equalTo(false)));
	}

}

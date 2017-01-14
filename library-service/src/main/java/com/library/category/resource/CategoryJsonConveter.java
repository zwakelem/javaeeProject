package com.library.category.resource;

import com.google.gson.JsonObject;
import com.library.app.category.model.Category;
import com.library.category.common.json.JsonReader;

public class CategoryJsonConveter {

	/**
	 * reads a String json and converts it to generic Json object
	 * 
	 * @param json
	 * @return
	 */
	public Category convertFrom(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

		final Category category = new Category();
		category.setName(JsonReader.getStringOrNull(jsonObject, "name"));

		return category;
	}

}

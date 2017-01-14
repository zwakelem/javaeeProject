package com.library.category.resource;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.category.common.json.JsonUtils;
import com.library.category.common.json.OperationResultJsonWriter;
import com.library.category.common.model.HttpCode;
import com.library.category.common.model.OperationResult;

public class CategoryResource {

	private Logger log = LoggerFactory.getLogger(getClass());

	CategoryServices categoryServices;
	CategoryJsonConveter categoryJsonConveter;

	public Response add(final String body) {
		log.debug("Adding category with body {}", body);

		// will receive json from front end to add to database
		Category category = categoryJsonConveter.convertFrom(body);

		category = categoryServices.add(category);
		final OperationResult result = OperationResult.success(JsonUtils.getJsonElementWithId(category.getId()));

		log.debug("Returning operation result after adding category: {}", result);

		return Response.status(HttpCode.CREATED.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
	}

}

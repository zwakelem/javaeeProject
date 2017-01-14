package com.library.category.common.json;

import com.google.gson.Gson;
import com.library.category.common.model.OperationResult;

public class OperationResultJsonWriter {

	private OperationResultJsonWriter() {
	}

	public static String toJson(final OperationResult operationResult) {
		if (operationResult.getEntity() == null) {
			return "";
		}

		final Gson gson = new Gson();
		return gson.toJson(operationResult.getEntity());
	}

}

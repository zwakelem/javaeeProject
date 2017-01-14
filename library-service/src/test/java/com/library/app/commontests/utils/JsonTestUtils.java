package com.library.app.commontests.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONException;
import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

@Ignore
public class JsonTestUtils {

	public static final String BASE_JSON_DIR = "json/";
	public static final String BASE_PATH = "/home/zwakelem/git/library-app/library-service/src/test/resources/";

	private JsonTestUtils() {
	}

	public static String readJsonFile(final String relativePath) {
		InputStream is = null;
		try {
			is = new FileInputStream(BASE_PATH + BASE_JSON_DIR + relativePath);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		try (Scanner s = new Scanner(is)) {
			return s.useDelimiter("\\A").hasNext() ? s.next() : "";
		}
	}

	public static void assertJsonMatchesExpectedJson(final String actualJson, final String expectedJson) {
		try {
			JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
		} catch (final JSONException e) {
			throw new IllegalArgumentException(e);
		}
	}

}

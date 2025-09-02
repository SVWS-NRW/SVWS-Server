package de.svws_nrw.api.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerUIResourceUtilsTest {

	private static Stream<Arguments> extractAPIDefinitionNameArguments() {
		return Stream.of(
				Arguments.of("path/to/openapi.json", "openapi"),
				Arguments.of("openapi.json", "openapi"),
				Arguments.of("openapi", "openapi"),
				Arguments.of("openapi-test", "openapi-test"),
				Arguments.of("openapi123/456.yaml", "456"),
				Arguments.of(null, null)
		);
	}

	@MethodSource("extractAPIDefinitionNameArguments")
	@ParameterizedTest
	void extractAPIDefinitionName(final String openApiFilePath, final String expected) {
		final String result = SwaggerUIResourceUtils.extractAPIDefinitionName(openApiFilePath);
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void customizeSwaggerInitializerJs() throws IOException {
		final InputStream defaultInput = ClassLoader.getSystemResourceAsStream("swagger-initializer_default.js");
		assertThat(defaultInput).as("Test-Resource swagger-initializer_default.js nicht gefunden").isNotNull();
		final String defaultContent = IOUtils.toString(defaultInput, StandardCharsets.UTF_8);

		final InputStream customInput = ClassLoader.getSystemResourceAsStream("swagger-initializer_custom.js");
		assertThat(customInput).as("Test-Resource swagger-initializer_custom.js nicht gefunden").isNotNull();
		final String customContent = IOUtils.toString(customInput, StandardCharsets.UTF_8);

		final String result = SwaggerUIResourceUtils.customizeSwaggerInitializerJs(defaultContent, List.of("path/to/test1.json", "test2.json"));
		assertThat(result).isEqualTo(customContent);
	}

}

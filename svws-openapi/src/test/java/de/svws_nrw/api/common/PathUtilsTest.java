package de.svws_nrw.api.common;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class PathUtilsTest {

	private static Stream<Arguments> checkIsInPathSpecificationArguments() {
		return Stream.of(
				// ohne Wildcard 1. Beispiel
				Arguments.of(new String[] { "/app" }, "/app", true),
				Arguments.of(new String[] { "/app" }, "/", false),
				Arguments.of(new String[] { "/app" }, "/app/", false),
				Arguments.of(new String[] { "/app" }, "/foo/app", false),
				Arguments.of(new String[] { "/app" }, "/app/abc", false),
				Arguments.of(new String[] { "/app" }, "/foo/app/abc", false),
				// ohne Wildcard 2. Beispiel
				Arguments.of(new String[] { "/app/file.json" }, "/app/file.json", true),
				Arguments.of(new String[] { "/app/file.json" }, "/app/file.pdf", false),
				Arguments.of(new String[] { "/app/file.json" }, "/app/file", false),
				// mehrere ohne Wildcard
				Arguments.of(new String[] { "/app", "/app/file.json" }, "/app", true),
				Arguments.of(new String[] { "/app", "/app/file.json" }, "/app/file.json", true),
				Arguments.of(new String[] { "/app", "/app/file.json" }, "/", false),
				Arguments.of(new String[] { "/app", "/app/file.json" }, "/app/", false),
				Arguments.of(new String[] { "/app", "/app/file.json" }, "/app/abc", false),
				Arguments.of(new String[] { "/app", "/app/file.json" }, "/app/file.pdf", false),
				Arguments.of(new String[] { "/app", "/app/file.json" }, "/app/file", false),
				Arguments.of(new String[] { "/app", "/app/file.json" }, "/foo/app/file", false),
				// mit Wildcard
				Arguments.of(new String[] { "/app/*" }, "/app", true),
				Arguments.of(new String[] { "/app/*" }, "/app/", true),
				Arguments.of(new String[] { "/app/*" }, "/app/abc", true),
				Arguments.of(new String[] { "/app/*" }, "/app/abc/def", true),
				Arguments.of(new String[] { "/app/*" }, "/", false),
				Arguments.of(new String[] { "/app/*" }, "/abc", false),
				Arguments.of(new String[] { "/app/*" }, "/abc/def", false),
				Arguments.of(new String[] { "/app/*" }, "/foo/abc/def", false),
				// mit Wildcard mit mehreren Levels
				Arguments.of(new String[] { "/app/abc/*" }, "/app/abc/def", true),
				Arguments.of(new String[] { "/app/abc/*" }, "/app/abc/", true),
				Arguments.of(new String[] { "/app/abc/*" }, "/app/abc", true),
				Arguments.of(new String[] { "/app/abc/*" }, "/app/def", false),
				Arguments.of(new String[] { "/app/abc/*" }, "/app", false),
				Arguments.of(new String[] { "/app/abc/*" }, "/foo/app", false),
				// mehrere mit Wildcard
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/app", true),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/app/", true),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/app/abc", true),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/app/abc/def", true),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/test", true),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/test/", true),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/test/abc", true),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/test/abc/def", true),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/", false),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/abc", false),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/abc/def", false),
				Arguments.of(new String[] { "/app/*", "/test/*" }, "/foo/abc/def", false)
		);
	}

	@MethodSource("checkIsInPathSpecificationArguments")
	@ParameterizedTest
	void checkIsInPathSpecification(final String[] pathSpecs, final String path, final boolean expected) {
		final boolean result = PathUtils.checkIsInPathSpecification(pathSpecs, path);
		assertThat(result).isEqualTo(expected);
	}
}

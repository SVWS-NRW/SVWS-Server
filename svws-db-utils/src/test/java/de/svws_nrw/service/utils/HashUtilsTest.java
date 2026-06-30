package de.svws_nrw.service.utils;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testklasse für HashUtils
 */
class HashUtilsTest {

	// -------------------------------------------------------------------------
	// Bekannte SHA-256 Hashes (z. B. via: echo -n "input" | sha256sum)
	// -------------------------------------------------------------------------

	@Test
	void sha256AsHex_emptyByteArray_returnsKnownHash() {
		final String expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

		final String actual = HashUtils.sha256AsHex(new byte[0]);

		assertThat(actual).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("knownHashes")
	void sha256AsHex_knownInputs_returnExpectedHash(final String input, final String expectedHash) {
		final byte[] bytes = input.getBytes(StandardCharsets.UTF_8);

		final String actual = HashUtils.sha256AsHex(bytes);

		assertThat(actual).isEqualTo(expectedHash);
	}

	static Stream<Arguments> knownHashes() {
		return Stream.of(
				Arguments.of("The quick brown fox jumps over the lazy dog",
						"d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592"),
				Arguments.of("",
						"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
		);
	}

	// -------------------------------------------------------------------------
	// Format-Prüfungen
	// -------------------------------------------------------------------------

	@Test
	void sha256AsHex_returnValue_isLowercase() {
		final byte[] input = "test".getBytes(StandardCharsets.UTF_8);

		final String actual = HashUtils.sha256AsHex(input);

		assertThat(actual).isLowerCase();
	}

	@Test
	void sha256AsHex_returnValue_hasLength64() {
		final byte[] input = "test".getBytes(StandardCharsets.UTF_8);

		final String actual = HashUtils.sha256AsHex(input);

		assertThat(actual).hasSize(64);
	}

	@Test
	void sha256AsHex_returnValue_containsOnlyHexChars() {
		final byte[] input = "test".getBytes(StandardCharsets.UTF_8);

		final String actual = HashUtils.sha256AsHex(input);

		assertThat(actual).matches("[0-9a-f]{64}");
	}

	// -------------------------------------------------------------------------
	// Determinismus & Konsistenz
	// -------------------------------------------------------------------------

	@Test
	void sha256AsHex_sameInput_returnsSameHash() {
		final byte[] input = "deterministic".getBytes(StandardCharsets.UTF_8);

		final String first = HashUtils.sha256AsHex(input);
		final String second = HashUtils.sha256AsHex(input);

		assertThat(first).isEqualTo(second);
	}

	@Test
	void sha256AsHex_differentInputs_returnDifferentHashes() {
		final byte[] input1 = "foo".getBytes(StandardCharsets.UTF_8);
		final byte[] input2 = "bar".getBytes(StandardCharsets.UTF_8);

		final String hash1 = HashUtils.sha256AsHex(input1);
		final String hash2 = HashUtils.sha256AsHex(input2);

		assertThat(hash1).isNotEqualTo(hash2);
	}

	@Test
	void sha256AsHex_similarInputs_returnCompletelyDifferentHashes() {
		final byte[] input1 = "password1".getBytes(StandardCharsets.UTF_8);
		final byte[] input2 = "password2".getBytes(StandardCharsets.UTF_8);

		final String hash1 = HashUtils.sha256AsHex(input1);
		final String hash2 = HashUtils.sha256AsHex(input2);

		assertThat(hash1).isNotEqualTo(hash2);
	}

	// -------------------------------------------------------------------------
	// Utility-Klasse: nicht instanziierbar
	// -------------------------------------------------------------------------

	@Test
	void constructor_isPrivate() throws NoSuchMethodException {
		// Avalanche-Effekt: minimale Inputänderung → völlig anderer Hash
		final Constructor<HashUtils> constructor = HashUtils.class.getDeclaredConstructor();

		assertThat(constructor.canAccess(null)).isFalse();
	}

	@Test
	void constructor_throwsException_whenInvokedViaReflection() {
		assertThatThrownBy(() -> {
			final Constructor<HashUtils> constructor = HashUtils.class.getDeclaredConstructor();
			constructor.newInstance();
		}).isInstanceOf(IllegalAccessException.class);
	}
}

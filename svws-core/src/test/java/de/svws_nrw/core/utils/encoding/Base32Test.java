package de.svws_nrw.core.utils.encoding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


@DisplayName("Tests für die Base32-Kodierung (RFC 4648)")
class Base32Test {

	@Nested
	@DisplayName("Tests für encode")
	class EncodeTests {

		@Test
		@DisplayName("Sollte leere Eingaben korrekt als leeren String kodieren")
		void encodeEmpty() {
			assertThat(Base32.encode(new byte[0])).isEmpty();
			assertThat(Base32.encode(null)).isEmpty();
		}

		@ParameterizedTest(name = "Input: \"{0}\" -> Output: {1}")
		@CsvSource({
				"'',           ''",
				"'f',          'MY======'",
				"'fo',         'MZXQ===='",
				"'foo',        'MZXW6==='",
				"'foob',       'MZXW6YQ='",
				"'fooba',      'MZXW6YTB'",
				"'foobar',     'MZXW6YTBOI======'"
		})
		@DisplayName("Prüfe die RFC 4648 Testvektoren für encoding")
		void rfcTestVectors(final String input, final String expected) {
			final byte[] data = input.getBytes(StandardCharsets.UTF_8);
			assertThat(Base32.encode(data)).isEqualTo(expected);
		}
	}

	@Nested
	@DisplayName("Tests für decode")
	class DecodeTests {

		@Test
		@DisplayName("Sollte leere oder null Strings als leeres Byte-Array dekodieren")
		void decodeEmpty() {
			assertThat(Base32.decode("")).isEmpty();
			assertThat(Base32.decode(null)).isEmpty();
		}

		@ParameterizedTest(name = "Input: {0} -> Output: \"{1}\"")
		@CsvSource({
				"'MY======',          'f'",
				"'MZXQ====',          'fo'",
				"'MZXW6===',          'foo'",
				"'MZXW6YQ=',          'foob'",
				"'MZXW6YTB',          'fooba'",
				"'MZXW6YTBOI======',  'foobar'"
		})
		@DisplayName("Prüfe die RFC 4648 Testvektoren für decoding")
		void rfcTestVectors(final String input, final String expected) {
			final byte[] result = Base32.decode(input);
			assertThat(new String(result, StandardCharsets.UTF_8)).isEqualTo(expected);
		}

		@Test
		@DisplayName("Sollte Case-Insensitivity und fehlendes Padding unterstützen")
		void decodeRobustness() {
			final byte[] result = Base32.decode("mzxw6ytboi");
			assertThat(new String(result, StandardCharsets.UTF_8)).isEqualTo("foobar");
		}

		@Test
		@DisplayName("Sollte bei ungültigen Zeichen eine IllegalArgumentException werfen")
		void decodeInvalidChars() {
			// '1' ist nicht im Base32 Alphabet -> IllegalArgumentException
			assertThatThrownBy(() -> Base32.decode("MZXW61"))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining("Ungültiges Zeichen");

			// Sonderzeichen sind nicht im Base32 Alphabet -> IllegalArgumentException
			assertThatThrownBy(() -> Base32.decode("MZXW6!"))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

}

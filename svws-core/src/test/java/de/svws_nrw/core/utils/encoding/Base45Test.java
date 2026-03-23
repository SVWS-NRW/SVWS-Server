package de.svws_nrw.core.utils.encoding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Tests für die Base45-Kodierung (RFC 9285)")
class Base45Test {

	@Nested
	@DisplayName("Tests für encode")
	class EncodeTests {

		@Test
		@DisplayName("Sollte leere Eingaben korrekt als leeren String kodieren")
		void encodeEmpty() {
			assertThat(Base45.encode(new byte[0])).isEmpty();
			assertThat(Base45.encode(null)).isEmpty();
		}

		@ParameterizedTest(name = "Input: \"{0}\" -> Output: {1}")
		@CsvSource({
				"'AB',          'BB8'",
				"'Hello!!',     '%69 VD92EX0'",
				"'base-45',     'UJCLQE7W581'"
		})
		@DisplayName("Prüfe die RFC 9285 Testvektoren für encoding")
		void rfcTestVectors(final String input, final String expected) {
			final byte[] data = input.getBytes(StandardCharsets.UTF_8);
			assertThat(Base45.encode(data)).isEqualTo(expected);
		}

		@Test
		@DisplayName("Sollte ungerade Anzahl an Bytes (Padding-Ersatz) korrekt kodieren")
		void encodeOddLength() {
			// 'J' (74) -> 74 % 45 = 29 (T), 74 / 45 = 1 (1) -> "T1"
			assertThat(Base45.encode("J".getBytes(StandardCharsets.UTF_8))).isEqualTo("T1");
		}
	}

	@Nested
	@DisplayName("Tests für decode")
	class DecodeTests {

		@Test
		@DisplayName("Sollte leere oder null Strings als leeres Byte-Array dekodieren")
		void decodeEmpty() {
			assertThat(Base45.decode("")).isEmpty();
			assertThat(Base45.decode(null)).isEmpty();
		}

		@ParameterizedTest(name = "Input: {0} -> Output: \"{1}\"")
		@CsvSource({
				"'BB8',          'AB'",
				"'%69 VD92EX0',  'Hello!!'",
				"'UJCLQE7W581',  'base-45'"
		})
		@DisplayName("Prüfe die RFC 9285 Testvektoren für decoding")
		void rfcTestVectors(final String input, final String expected) {
			final byte[] result = Base45.decode(input);
			assertThat(new String(result, StandardCharsets.UTF_8)).isEqualTo(expected);
		}

		@Test
		@DisplayName("Sollte 2-Zeichen-Blöcke (einzelne Bytes) korrekt dekodieren")
		void decodeShortBlock() {
			final byte[] result = Base45.decode("T1");
			assertThat(new String(result, StandardCharsets.UTF_8)).isEqualTo("J");
		}

		@Test
		@DisplayName("Sollte bei ungültiger Länge eine IllegalArgumentException werfen")
		void decodeInvalidLength() {
			// Base45 Blöcke sind immer 2 oder 3 Zeichen lang. Längen mit (len % 3 == 1) sind ungültig.
			assertThatThrownBy(() -> Base45.decode("A"))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining("Ungültige Base45-String-Länge");
		}

		@Test
		@DisplayName("Sollte bei ungültigen Zeichen eine IllegalArgumentException werfen")
		void decodeInvalidChars() {
			assertThatThrownBy(() -> Base45.decode("abc"))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining("Ungültiges Zeichen");
		}

		@Test
		@DisplayName("Sollte Überlauf-Werte (> 0xFFFF) abfangen")
		void decodeOverflow() {
			// "ZZZ" wäre 44 + (44*45) + (44*2025) = 91124, somit größer als 65535 und nicht mit 2 Bytes kodierbar
			assertThatThrownBy(() -> Base45.decode("ZZZ"))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining("außerhalb des gültigen Bereichs");
		}
	}

}

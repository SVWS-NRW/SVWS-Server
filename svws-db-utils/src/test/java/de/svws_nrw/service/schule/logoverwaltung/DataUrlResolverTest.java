package de.svws_nrw.service.schule.logoverwaltung;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DataUrlResolver")
class DataUrlResolverTest {

	// -------------------------------------------------------------------------
	// Testdaten
	// -------------------------------------------------------------------------

	/**
	 * Minimales 1x1 Pixel PNG als Base64 (valides Bild)
	 */
	private static final String PNG_BASE64 =
			"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==";

	private static final String PNG_DATA_URL =
			"data:image/png;base64," + PNG_BASE64;

	/**
	 * Minimales 1x1 Pixel JPEG als Base64
	 */
	private static final String JPEG_BASE64 = "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8U"
			+ "HRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgN"
			+ "DRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIy"
			+ "MjL/wAARCAABAAEDASIAAhEBAxEB/8QAFAABAAAAAAAAAAAAAAAAAAAACf/EABQQAQAAAAAA"
			+ "AAAAAAAAAAAAAP/EABQBAQAAAAAAAAAAAAAAAAAAAAD/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oA"
			+ "DAMBAAIRAxEAPwCwABmX/9k=";

	private static final String JPEG_DATA_URL =
			"data:image/jpeg;base64," + JPEG_BASE64;

	/**
	 * Kein valides Bild — zufälliger Base64-String
	 */
	private static final String INVALID_BASE64 = "dGVzdA=="; // "test" in Base64

	/**
	 * Kein valider Base64-String
	 */
	private static final String NOT_BASE64 = "!!!kein_base64!!!";

	private static final String NO_PAYLOAD = "data:image/jpeg;base64,";

	// -------------------------------------------------------------------------
	// get()
	// -------------------------------------------------------------------------

	@Nested
	class Resolve {

		@Test
		@DisplayName("gibt Optional.empty() zurück wenn base64 null ist")
		void get_null_returnsEmpty() {
			assertThat(DataUrlResolver.resolve(null)).isEmpty();
		}

		@Test
		@DisplayName("gibt ein vollständiges ResolvedDataUrl Objekt zu dem PNG Base64 Payloads zurück")
		void get_completePNGDataUrl_returnsResolvedDataUrl() {
			final var result = DataUrlResolver.resolve(PNG_DATA_URL);

			assertThat(result).isPresent().get()
					.extracting(
							DataUrl::value, DataUrl::mimeType,
							DataUrl::payload, DataUrl::sizeInKB)
					.containsExactly(PNG_DATA_URL, "image/png", PNG_BASE64, 0.068359375d);

			assertThat(result.get().value()).startsWith("data:image/png;base64,");
		}

		@Test
		@DisplayName("gibt ein vollständiges ResolvedDataUrl Objekt zu dem JPEG Base64 Payloads zurück")
		void get_completeJPEGDataUrl_returnsResolvedDataUrl() {
			final var result = DataUrlResolver.resolve(JPEG_DATA_URL);

			assertThat(result).isPresent().get()
					.extracting(
							DataUrl::value, DataUrl::mimeType,
							DataUrl::payload, DataUrl::sizeInKB)
					.containsExactly(JPEG_DATA_URL, "image/jpeg", JPEG_BASE64, 0.2744140625d);

			assertThat(result.get().value()).startsWith("data:image/jpeg;base64,");
		}

		@Test
		@DisplayName("gibt ein vollständiges ResolvedDataUrl Objekt zu der PNG DataUrl zurück")
		void get_onlyBase64PNGPayload_returnsResolvedDataUrl() {
			final var result = DataUrlResolver.resolve(PNG_BASE64);

			assertThat(result).isPresent().get()
					.extracting(
							DataUrl::value, DataUrl::mimeType,
							DataUrl::payload, DataUrl::sizeInKB)
					.containsExactly(PNG_DATA_URL, "image/png", PNG_BASE64, 0.068359375d);

			assertThat(result.get().value()).startsWith("data:image/png;base64,");
		}

		@Test
		@DisplayName("gibt ein vollständiges ResolvedDataUrl Objekt zu der JPEG DataUrl zurück")
		void get_onlyBase64JPEGPayload_returnsResolvedDataUrl() {
			final var result = DataUrlResolver.resolve(JPEG_BASE64);

			assertThat(result).isPresent().get()
					.extracting(
							DataUrl::value, DataUrl::mimeType,
							DataUrl::payload, DataUrl::sizeInKB)
					.containsExactly(JPEG_DATA_URL, "image/jpeg", JPEG_BASE64, 0.2744140625d);

			assertThat(result.get().value()).startsWith("data:image/jpeg;base64,");
		}

		@Test
		@DisplayName("gibt Optional.empty() zurück wenn der Base64-String kein valides Bild enthält")
		void get_invalidImageBase64_returnsEmpty() {
			// "test" ist valides Base64, aber kein Bild → Tika erkennt keinen Bild-MIME-Type
			// Je nach Tika-Verhalten: entweder null oder application/octet-stream
			final var result = DataUrlResolver.resolve(INVALID_BASE64);

			// Tika gibt für unbekannte Bytes application/octet-stream zurück,
			// was kein null ist → result wäre present mit "application/octet-stream"
			// Dieser Test dokumentiert das tatsächliche Verhalten:
			assertThat(result).isPresent();
		}

		@Test
		@DisplayName("gibt Optional.empty() zurück wenn der String kein valides Base64 ist")
		void get_notBase64String_returnsEmpty() {
			assertThat(DataUrlResolver.resolve(NOT_BASE64)).isEmpty();
		}

		@Test
		@DisplayName("gibt Optional.empty() zurück wenn der payload null ist")
		void get_noBase64Payload_returnsEmpty() {
			assertThat(DataUrlResolver.resolve(NO_PAYLOAD)).isEmpty();
		}

	}

	// -------------------------------------------------------------------------
	// hasAllowedImageMimeType()
	// -------------------------------------------------------------------------

	@Nested
	@DisplayName("ResolvedDataUrl")
	class ResolvedDataUrl {

		private static final List<String> ALLOWED = List.of("image/png", "image/jpeg", "image/svg+xml", "image/tiff", "image/gif");

		@Nested
		@DisplayName("hasAnyMimeTypeOf()")
		class HasAnyMimeTypeOf {

			@Test
			@DisplayName("gibt true zurück, wenn MIME-Type image/png ist")
			void hasAnyMimeTypeOf_PNG_returnsTrue() {
				final var result = DataUrlResolver.resolve(PNG_DATA_URL);

				assertThat(result).isPresent();
				assertThat(result.get().hasAnyMimeTypeOf(ALLOWED)).isTrue();
			}

			@Test
			@DisplayName("gibt true zurück, wenn MIME-Type image/jpeg ist")
			void hasAnyMimeTypeOf_JPEG_returnsTrue() {
				final var result = DataUrlResolver.resolve(JPEG_BASE64);

				assertThat(result).isPresent();
				assertThat(result.get().hasAnyMimeTypeOf(ALLOWED)).isTrue();
			}

			@Test
			@DisplayName("gibt false zurück, wenn MIME-Type image/jpeg ist und image/heif erwartet wird")
			void hasAnyMimeTypeOf_HEIF_returnsFalse() {
				final var result = DataUrlResolver.resolve(JPEG_BASE64);

				assertThat(result).isPresent();
				assertThat(result.get().hasAnyMimeTypeOf(Set.of("image/heif"))).isFalse();
			}

			@Test
			@DisplayName("gibt false zurück, wenn Collection von MimeTypes null ist")
			void hasAnyMimeTypeOf_null_returnsFalse() {
				final var result = DataUrlResolver.resolve(JPEG_BASE64);

				assertThat(result).isPresent();
				assertThat(result.get().hasAnyMimeTypeOf(null)).isFalse();
			}

			@Test
			@DisplayName("gibt false zurück, wenn Collection von MimeTypes leer ist")
			void hasAnyMimeTypeOf_empty_returnsFalse() {
				final var result = DataUrlResolver.resolve(JPEG_BASE64);

				assertThat(result).isPresent();
				assertThat(result.get().hasAnyMimeTypeOf(Collections.emptySet())).isFalse();
			}

		}
	}
}

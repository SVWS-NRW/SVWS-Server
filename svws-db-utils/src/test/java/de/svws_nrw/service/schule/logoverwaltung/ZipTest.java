package de.svws_nrw.service.schule.logoverwaltung;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.svws_nrw.base.compression.CreateZipArchiveException;
import de.svws_nrw.base.compression.Zip;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZipTest {

	@Test
	void createArchive_shouldReturnValidZipWithSingleEntry() throws Exception {
		final byte[] content = "Hallo Welt".getBytes(StandardCharsets.UTF_8);
		final Map<String, byte[]> input = Map.of("test.txt", content);

		final byte[] result = Zip.createArchive(input);

		assertThat(result).isNotEmpty();
		try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(result))) {
			final ZipEntry entry = zis.getNextEntry();
			assertThat(entry).isNotNull();
			assertThat(entry.getName()).isEqualTo("test.txt");
			assertThat(zis.readAllBytes()).isEqualTo(content);
			assertThat(zis.getNextEntry()).isNull();
		}
	}

	@Test
	void createArchive_shouldReturnValidZipWithMultipleEntries() throws Exception {
		final Map<String, byte[]> input = new HashMap<>();
		input.put("logo_a.png", new byte[] { 1, 2, 3 });
		input.put("logo_b.png", new byte[] { 4, 5, 6 });

		final byte[] result = Zip.createArchive(input);

		final Map<String, byte[]> extractedEntries = extractZipEntries(result);
		assertThat(extractedEntries)
				.containsEntry("logo_a.png", new byte[] { 1, 2, 3 })
				.containsEntry("logo_b.png", new byte[] { 4, 5, 6 });
	}

	@Test
	void createArchive_shouldReturnValidZipForEmptyMap() throws Exception {
		final byte[] result = Zip.createArchive(Map.of());

		assertThat(result).isNotEmpty();
		try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(result))) {
			assertThat(zis.getNextEntry()).isNull();
		}
	}

	@Test
	void createArchive_shouldHandleEmptyFileContent() throws Exception {
		final Map<String, byte[]> input = Map.of("empty.txt", new byte[0]);

		final byte[] result = Zip.createArchive(input);

		try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(result))) {
			final ZipEntry entry = zis.getNextEntry();
			assertThat(entry).isNotNull();
			assertThat(entry.getName()).isEqualTo("empty.txt");
			assertThat(zis.readAllBytes()).isEmpty();
		}
	}

	@Test
	void createArchive_shouldPreserveFileNamesWithSpecialCharacters() throws Exception {
		final String fileName = "logo äöü & spezial.png";
		final Map<String, byte[]> input = Map.of(fileName, new byte[] { 1, 2, 3 });

		final byte[] result = Zip.createArchive(input);

		final Map<String, byte[]> extractedEntries = extractZipEntries(result);
		assertThat(extractedEntries).containsKey(fileName);
	}

	@Test
	void createArchive_shouldStartWithZipMagicBytes() {
		final Map<String, byte[]> input = Map.of("file.txt", "content".getBytes(StandardCharsets.UTF_8));

		final byte[] result = Zip.createArchive(input);

		// ZIP Magic Bytes: PK (0x50 0x4B)
		assertThat(result[0]).isEqualTo((byte) 0x50);
		assertThat(result[1]).isEqualTo((byte) 0x4B);
	}

	@Test
	void createArchive_shouldThrowCreateZipArchiveException_whenEntryNameIsNull() {
		final Map<String, byte[]> input = new HashMap<>();
		input.put(null, new byte[] { 1, 2, 3 });

		assertThatThrownBy(() -> Zip.createArchive(input))
				.isInstanceOf(CreateZipArchiveException.class);
	}

	// --- Hilfsmethoden ---

	private Map<String, byte[]> extractZipEntries(final byte[] zipBytes) throws Exception {
		final Map<String, byte[]> entries = new HashMap<>();
		try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				entries.put(entry.getName(), zis.readAllBytes());
				zis.closeEntry();
			}
		}
		return entries;
	}

}

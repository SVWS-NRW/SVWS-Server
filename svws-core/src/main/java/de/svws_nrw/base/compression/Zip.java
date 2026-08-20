package de.svws_nrw.base.compression;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Diese Utility Klasse stellt Funktionalität zum Erstellen von ZIP-Archiven zur Verfügung.
 */
public final class Zip {

	private Zip() {

	}

	/**
	 * Erzeugt ein Zip-Archiv aus einer Map mit Dateinamen als Key und den Daten als Value.
	 *
	 * @param fileNameToByteContent Map mit Dateinamen als Key und den Daten als Value
	 *
	 * @return ZIP-Archiv als Byte-Array
	 */
	public static byte[] createArchive(final Map<String, byte[]> fileNameToByteContent) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ZipOutputStream zos = new ZipOutputStream(baos)) {
			for (final Map.Entry<String, byte[]> fileEntry : fileNameToByteContent.entrySet()) {
				final ZipEntry zipEntry = new ZipEntry(fileEntry.getKey());
				zos.putNextEntry(zipEntry);
				zos.write(fileEntry.getValue());
				zos.closeEntry();
			}
		} catch (final Exception e) {
			throw new CreateZipArchiveException(e);
		}

		return baos.toByteArray();
	}
}

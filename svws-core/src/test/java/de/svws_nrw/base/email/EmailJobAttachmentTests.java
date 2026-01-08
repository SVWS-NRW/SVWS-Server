package de.svws_nrw.base.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Testklasse für {@link EmailJobAttachment}
 */
class EmailJobAttachmentTests {

	@Test
	@DisplayName("Konstruktor erstellt gültiges Attachment mit allen Parametern")
	void testConstructorValid() {
		final String filename = "test.pdf";
		final byte[] data = new byte[] { 1, 2, 3, 4, 5 };
		final String mimetype = "application/pdf";

		final EmailJobAttachment attachment = new EmailJobAttachment(filename, data, mimetype);

		assertNotNull(attachment);
		assertEquals(filename, attachment.filename);
		assertArrayEquals(data, attachment.data);
		assertEquals(mimetype, attachment.mimetype);
	}

	@Test
	@DisplayName("Konstruktor mit verschiedenen Mime-Types")
	void testConstructorWithDifferentMimeTypes() {
		final byte[] data = new byte[] { 1, 2, 3 };

		final EmailJobAttachment pdfAttachment = new EmailJobAttachment("document.pdf", data, "application/pdf");
		assertEquals("application/pdf", pdfAttachment.mimetype);

		final EmailJobAttachment imageAttachment = new EmailJobAttachment("image.png", data, "image/png");
		assertEquals("image/png", imageAttachment.mimetype);

		final EmailJobAttachment textAttachment = new EmailJobAttachment("text.txt", data, "text/plain");
		assertEquals("text/plain", textAttachment.mimetype);
	}

	@Test
	@DisplayName("Konstruktor mit großen Dateien")
	void testConstructorWithLargeData() {
		final byte[] largeData = new byte[1024 * 1024]; // 1 MB
		for (int i = 0; i < largeData.length; i++) {
			largeData[i] = (byte) (i % 256);
		}

		final EmailJobAttachment attachment = new EmailJobAttachment("large.bin", largeData, "application/octet-stream");

		assertNotNull(attachment);
		assertEquals(1024 * 1024, attachment.data.length);
		assertArrayEquals(largeData, attachment.data);
	}

	@Test
	@DisplayName("Konstruktor wirft Exception bei null filename")
	void testConstructorNullFilename() {
		final byte[] data = new byte[] { 1, 2, 3 };
		final String mimetype = "application/pdf";

		assertThrows(IllegalArgumentException.class,	() -> new EmailJobAttachment(null, data, mimetype));
	}

	@Test
	@DisplayName("Konstruktor wirft Exception bei null data")
	void testConstructorNullData() {
		final String filename = "test.pdf";
		final String mimetype = "application/pdf";

		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment(filename, null, mimetype));
	}

	@Test
	@DisplayName("Konstruktor wirft Exception bei null mimetype")
	void testConstructorNullMimetype() {
		final String filename = "test.pdf";
		final byte[] data = new byte[] { 1, 2, 3 };

        assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment(filename, data, null));
	}

	@Test
	@DisplayName("Konstruktor wirft Exception bei leerem filename")
	void testConstructorBlankFilename() {
		final byte[] data = new byte[] { 1, 2, 3 };
		final String mimetype = "application/pdf";

		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment("", data, mimetype));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment("   ", data, mimetype));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment("\t\n", data, mimetype));
	}

	@Test
	@DisplayName("Konstruktor wirft Exception bei leerem mimetype")
	void testConstructorBlankMimetype() {
		final String filename = "test.pdf";
		final byte[] data = new byte[] { 1, 2, 3 };

		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment(filename, data, ""));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment(filename, data, "   "));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment(filename, data, "\t\n"));
	}

	@Test
	@DisplayName("Konstruktor wirft Exception bei leerem Byte-Array")
	void testConstructorEmptyData() {
		final String filename = "test.pdf";
		final byte[] emptyData = new byte[0];
		final String mimetype = "application/pdf";

		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment(filename, emptyData, mimetype));
	}

	@Test
	@DisplayName("Konstruktor wirft Exception bei allen null Parametern")
	void testConstructorAllNull() {
		assertThrows(IllegalArgumentException.class, () -> new EmailJobAttachment(null, null, null));
	}

	@Test
	@DisplayName("Attachment mit Einzelbyte-Daten")
	void testAttachmentWithSingleByteData() {
		final byte[] singleByte = new byte[] { 42 };
		final EmailJobAttachment attachment = new EmailJobAttachment("single.dat", singleByte, "application/octet-stream");

		assertNotNull(attachment);
		assertEquals(1, attachment.data.length);
		assertEquals(42, attachment.data[0]);
	}

}

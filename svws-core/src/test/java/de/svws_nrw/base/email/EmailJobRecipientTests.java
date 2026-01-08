package de.svws_nrw.base.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailJobRecipientTests {

	@Test
	@DisplayName("Recipient-Constructor mit unzulässigen Werten initialisieren")
	void testConstructorWithNullEmptyValues() {
		assertThrows(IllegalArgumentException.class, () -> new EmailJobRecipient(""));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobRecipient("   "));
		assertThrows(IllegalArgumentException.class, () -> new EmailJobRecipient("\t\n "));
	}

	@Test
	@DisplayName("Teste equals/hashCode Methoden, welche auf der E-Mail-Adresse basieren.")
	void testEqualsHashCode() {
		final EmailJobRecipient r1 = new EmailJobRecipient("to_a@test");
		final EmailJobRecipient r2 = new EmailJobRecipient("to_a@test");
		final EmailJobRecipient r3 = new EmailJobRecipient("to_bb@test");

		assertEquals(r1, r2);
		assertEquals(r1.hashCode(), r2.hashCode());

		assertNotEquals(r1, r3);
	}
}
